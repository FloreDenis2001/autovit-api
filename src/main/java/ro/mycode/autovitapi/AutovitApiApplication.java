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
//			masinaService.getAllCars().forEach(System.out::println);

//			view.play();
//            masinaService.update("red","x");
//			masinaRepository.removeMasinaByModel("C");

//			masinaRepository.getAllCarsByColor("Red").get().forEach(System.out::println);

//			System.out.println(masinaRepository.findByModel("929").toString());

//			masinaService.add(new Masina("Mercedes","C",2005,"white"));


			MasinaDTO masinaDTO=new MasinaDTO("Porsche","E",2012,"Puce");
			masinaService.filter(masinaDTO);
		};

	}

}
