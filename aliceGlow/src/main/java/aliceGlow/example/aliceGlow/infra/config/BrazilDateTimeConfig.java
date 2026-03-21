package aliceGlow.example.aliceGlow.infra.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class BrazilDateTimeConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        Locale.setDefault(new Locale("pt", "BR"));
    }
}
