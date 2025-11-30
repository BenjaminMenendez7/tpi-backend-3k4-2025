package utnfc.isi.back.clientesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@ComponentScan({
        "utnfc.isi.back.security",       // 🟢 Módulo compartido
        "utnfc.isi.back.clientesservice" // 🟢 Solo el código propio
})
@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class ClientesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientesServiceApplication.class, args);
    }

}
