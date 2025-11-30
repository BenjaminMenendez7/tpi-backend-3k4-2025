package utnfc.isi.back.camionesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan({
		"utnfc.isi.back.security",       // 🟢 Módulo compartido
		"utnfc.isi.back.camionesservice" // 🟢 Solo el código propio
})
@SpringBootApplication
public class CamionesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamionesServiceApplication.class, args);
	}

}
