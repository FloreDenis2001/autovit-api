package ro.mycode.autovitapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.autovitapi.dto.MasinaDTO;
import ro.mycode.autovitapi.exceptii.EmptyDatabaseMasiniException;
import ro.mycode.autovitapi.exceptii.MasinaAlreadyExistsException;
import ro.mycode.autovitapi.exceptii.MasinaDoesntExistException;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.service.MasinaService;

import java.util.List;

@RestController
@CrossOrigin
public class CarResource {


    private MasinaService masinaService;

    public CarResource(MasinaService masinaService) {
        this.masinaService = masinaService;
    }


    @GetMapping("api/v1/masini/all")
    public ResponseEntity<List<Masina>> getAllMasini() {


        List<Masina> masinas = masinaService.getAllCars();
        return new ResponseEntity<>(masinas, HttpStatus.OK);


    }

    @GetMapping("api/v1/masini/culoare/{color}")
    public ResponseEntity<List<Masina>> getAllMasiniByColor(@PathVariable String color) {
            List<Masina> masinas = masinaService.getAllCarsByColor(color);
            return new ResponseEntity<>(masinas, HttpStatus.OK);
    }

    @PostMapping("api/v1/masini/add")
    public ResponseEntity<Masina> addCar(@RequestBody Masina m) {
            this.masinaService.add(m);
            return new ResponseEntity<>(m, HttpStatus.CREATED);
    }

    @DeleteMapping("api/v1/masini/{model}")
    public ResponseEntity<Masina> deleteCar(@PathVariable String model) {
        this.masinaService.remove(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("api/v1/masini/update")
    public ResponseEntity<MasinaDTO> updateCar(@RequestBody MasinaDTO masina) {
        this.masinaService.update(masina);
        return new ResponseEntity<>(masina,HttpStatus.OK);
    }

    @GetMapping("api/v1/masini/filter")
    public ResponseEntity<MasinaDTO> filterCar(@RequestBody MasinaDTO masina){
        this.masinaService.filter(masina);
        return new ResponseEntity<>(masina,HttpStatus.OK);
    }



}
