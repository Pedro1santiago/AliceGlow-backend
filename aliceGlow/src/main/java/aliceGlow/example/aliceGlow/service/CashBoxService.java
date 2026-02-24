package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.CashBox;
import aliceGlow.example.aliceGlow.domain.CashOutflow;
import aliceGlow.example.aliceGlow.dto.cashBox.CashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.CreateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashBox.UpdateCashBoxDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CashOutflowDTO;
import aliceGlow.example.aliceGlow.dto.cashOutflow.CreateCashOutflowDTO;
import aliceGlow.example.aliceGlow.exception.CashBoxAlreadyExistsForDateException;
import aliceGlow.example.aliceGlow.exception.CashBoxNotFoundException;
import aliceGlow.example.aliceGlow.repository.CashBoxRepository;
import aliceGlow.example.aliceGlow.repository.CashOutflowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CashBoxService {

    private final CashBoxRepository cashBoxRepository;
    private final CashOutflowRepository cashOutflowRepository;

    public CashBoxService(CashBoxRepository cashBoxRepository, CashOutflowRepository cashOutflowRepository) {
        this.cashBoxRepository = cashBoxRepository;
        this.cashOutflowRepository = cashOutflowRepository;
    }

    @Transactional
    public CashBoxDTO create(CreateCashBoxDTO dto) {
        if (cashBoxRepository.existsByBusinessDate(dto.businessDate())) {
            throw new CashBoxAlreadyExistsForDateException(dto.businessDate());
        }

        CashBox cashBox = new CashBox();
        cashBox.setBusinessDate(dto.businessDate());
        cashBox.setBalance(dto.balance());

        return CashBoxDTO.toDTO(cashBoxRepository.save(cashBox));
    }

    public CashBoxDTO findById(Long id) {
        CashBox cashBox = cashBoxRepository.findById(id)
                .orElseThrow(() -> new CashBoxNotFoundException(id));
        return CashBoxDTO.toDTO(cashBox);
    }

    public CashBoxDTO findByBusinessDate(LocalDate businessDate) {
        CashBox cashBox = cashBoxRepository.findByBusinessDate(businessDate)
                .orElseThrow(CashBoxNotFoundException::new);
        return CashBoxDTO.toDTO(cashBox);
    }

    @Transactional
    public CashBoxDTO updateBalance(Long id, UpdateCashBoxDTO dto) {
        CashBox cashBox = cashBoxRepository.findById(id)
                .orElseThrow(() -> new CashBoxNotFoundException(id));
        cashBox.setBalance(dto.balance());
        return CashBoxDTO.toDTO(cashBoxRepository.save(cashBox));
    }

    @Transactional
    public CashOutflowDTO addOutflow(Long cashBoxId, CreateCashOutflowDTO dto) {
        CashBox cashBox = cashBoxRepository.findById(cashBoxId)
                .orElseThrow(() -> new CashBoxNotFoundException(cashBoxId));

        CashOutflow outflow = new CashOutflow();
        outflow.setCashBox(cashBox);
        outflow.setDescription(dto.description());
        outflow.setAmount(dto.amount());
        outflow.setOccurredAt(dto.occurredAt());

        BigDecimal newBalance = cashBox.getBalance().subtract(dto.amount());
        cashBox.setBalance(newBalance);
        cashBoxRepository.save(cashBox);

        return CashOutflowDTO.toDTO(cashOutflowRepository.save(outflow));
    }
}
