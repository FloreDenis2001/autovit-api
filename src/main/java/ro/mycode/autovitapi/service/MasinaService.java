package ro.mycode.autovitapi.service;

import org.springframework.stereotype.Service;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.repository.MasinaRepository;

import java.util.List;


@Service
public class MasinaService {

    private MasinaRepository masinaRepository;

    public MasinaService(MasinaRepository masinaRepository) {
        this.masinaRepository = masinaRepository;
    }

    public List<Masina> getAllCars(){


        return  masinaRepository.findAll();
    }
}
