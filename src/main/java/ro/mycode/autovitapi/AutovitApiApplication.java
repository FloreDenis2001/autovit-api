package ro.mycode.autovitapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.mycode.autovitapi.dto.MasinaDTO;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.repository.MasinaRepository;
import ro.mycode.autovitapi.service.MasinaService;

@SpringBootApplication
public class AutovitApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutovitApiApplication.class, args);
	}



	@Bean
	CommandLineRunner commandLineRunner(MasinaService masinaService,MasinaRepository masinaRepository){
		return  args->{
		};

	}

}
