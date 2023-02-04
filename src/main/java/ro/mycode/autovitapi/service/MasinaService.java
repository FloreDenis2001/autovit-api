package ro.mycode.autovitapi.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ro.mycode.autovitapi.dto.MasinaDTO;
import ro.mycode.autovitapi.exceptii.EmptyDatabaseMasiniException;
import ro.mycode.autovitapi.exceptii.MasinaAlreadyExistsException;
import ro.mycode.autovitapi.exceptii.MasinaDoesntExistException;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.repository.MasinaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MasinaService {

    private MasinaRepository masinaRepository;

    public MasinaService(MasinaRepository masinaRepository) {
        this.masinaRepository = masinaRepository;
    }

    public List<Masina> getAllCars() throws EmptyDatabaseMasiniException {
        if (masinaRepository.findAll().size() != 0) {
            return masinaRepository.findAll();
        } else {
            throw new EmptyDatabaseMasiniException("Nu sunt masini in tabel");
        }
    }


    public List<Masina> getAllCarsByColor(String color) throws EmptyDatabaseMasiniException {
        Optional<List<Masina>> masinaList = masinaRepository.getAllCarsByColor(color);

        if (Optional.of(masinaList).isEmpty()) {
            throw new EmptyDatabaseMasiniException("Nu sunt masini cu culoarea respectiva");
        } else {
            return masinaList.get();
        }
    }

    @Transactional
    public void add(Masina masina) throws MasinaAlreadyExistsException {


        Optional<Masina> searchedCar = masinaRepository.findByModel(masina.getModel());

        if (searchedCar.isEmpty()) {
            masinaRepository.save(masina);

        } else {
            throw new MasinaAlreadyExistsException("Masina exista deja ! ");
        }
    }

    @Transactional
    public void remove(String model) throws MasinaDoesntExistException {
        Optional<Masina> exits = masinaRepository.findByModel(model);
        if (!exits.isEmpty()) {
            masinaRepository.removeMasinaByModel(model);
        } else {
            throw new MasinaDoesntExistException("Masina nu exista in database ! ");
        }

    }

    @Transactional
    @Modifying
    public void update(MasinaDTO masinaDTO) throws MasinaDoesntExistException {
        Optional<Masina> exits = masinaRepository.findByModel(masinaDTO.getModel());
        System.out.println(masinaRepository.findByModel(masinaDTO.getModel()));
        if (!exits.isEmpty()) {
            if (masinaDTO.getAn() != 0) {
                exits.get().setAn(masinaDTO.getAn());
            }
            if (masinaDTO.getCuloare() != "") {
                exits.get().setCuloare(masinaDTO.getCuloare());
            }
            if (masinaDTO.getMarca()!="") {
                exits.get().setMarca(masinaDTO.getMarca());
            }

            masinaRepository.saveAndFlush(exits.get());
        } else {
            throw new MasinaDoesntExistException("Masina nu exista in database ! ");
        }


    }

    public Optional<List<Masina>> filter(MasinaDTO masinaDTO) throws MasinaDoesntExistException {
        List<Masina> exits = masinaRepository.findAll();
        List<Masina> filter=new ArrayList<>();
        if (!exits.isEmpty()) {
            for (int i = 0; i < exits.size(); i++) {
                if (    exits.get(i).getModel().compareTo(masinaDTO.getModel())==0
                        && exits.get(i).getAn()==masinaDTO.getAn()
                        && exits.get(i).getCuloare().compareTo(masinaDTO.getCuloare())==0
                        && exits.get(i).getMarca().compareTo(masinaDTO.getMarca())==0) {
                    filter.add(exits.get(i));
                }
            }
        } else {
            throw new MasinaDoesntExistException("Masina nu exista in database ! ");
        }
    for (int i=0;i<filter.size();i++){
        System.out.println(filter.get(i));
    }
     return Optional.of(filter);
    }
}
