package ro.mycode.autovitapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.autovitapi.exceptii.EmptyDatabaseMasiniException;
import ro.mycode.autovitapi.exceptii.MasinaAlreadyExistsException;
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
    public ResponseEntity<List<Masina>> getAllMasini()  {


        List<Masina> masinas = null;
        try {
            masinas = masinaService.getAllCars();
    return  new ResponseEntity<>(masinas, HttpStatus.OK);
        } catch (EmptyDatabaseMasiniException e) {
            throw new EmptyDatabaseMasiniException(e.getMessage());
        }

    }

    @GetMapping("api/v1/masini/culoare/{color}")
    public ResponseEntity<List<Masina>> getAllMasiniByColor(@PathVariable String color) {

        try {
            List<Masina> masinas=masinaService.getAllCarsByColor(color);
            return new ResponseEntity<>(masinas,HttpStatus.OK);
        } catch (EmptyDatabaseMasiniException e) {
            throw new EmptyDatabaseMasiniException(e.getMessage());
        }
    }

    @PostMapping("api/v1/masini/add")
    public ResponseEntity<Masina> addCar(@RequestBody Masina m){


        try {
            this.masinaService.add(m);
            return new ResponseEntity<>(m,HttpStatus.CREATED);
        } catch (MasinaAlreadyExistsException e) {
            throw new EmptyDatabaseMasiniException(e.getMessage());
        }


    }


}
