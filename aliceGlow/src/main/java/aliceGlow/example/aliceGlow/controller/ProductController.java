package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listProducts(){
        return ResponseEntity.ok(productService.listProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody CreateProductDTO dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDTO dto
    ){
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}