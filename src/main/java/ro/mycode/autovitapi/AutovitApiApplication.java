package ro.mycode.autovitapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.mycode.autovitapi.service.MasinaService;

@SpringBootApplication
public class AutovitApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutovitApiApplication.class, args);
	}



	@Bean
	CommandLineRunner commandLineRunner(MasinaService masinaService){
		return  args->{



			masinaService.getAllCars().forEach(System.out::println);

		};

	}

}
