package ro.mycode.autovitapi.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.autovitapi.AutovitApiApplication;
import ro.mycode.autovitapi.dto.MasinaDTO;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.repository.MasinaRepository;
import ro.mycode.autovitapi.service.MasinaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = AutovitApiApplication.class)
public class CarResourceITTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CarResource carResource;


    @Autowired
    private MasinaService masinaService;

    @MockBean
    private MasinaRepository masinaRepository;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllCarsTest() throws Exception {
        Faker faker = new Faker();
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            masini.add(new Masina(faker.lorem().sentence(), faker.lorem().sentence(), faker.number().numberBetween(2015, 2022), "verde"));
        }
        masini.add(new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu"));
        doReturn(masini).when(masinaRepository).findAll();
        mockMvc.perform(get("/api/v1/masini/all")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(masini)));
    }


    @Test
    public void getAllCarsTestException() throws Exception {
        doReturn(new ArrayList<>()).when(masinaRepository).findAll();
        mockMvc.perform(get("/api/v1/masini/all")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getAllCarsByColor() throws Exception {
        Faker faker = new Faker();
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            masini.add(new Masina(faker.lorem().sentence(), faker.lorem().sentence(), faker.number().numberBetween(2015, 2022), "verde"));
        }
        Optional<List<Masina>> masinas = Optional.of(masini);
        doReturn(masinas).when(masinaRepository).getAllCarsByColor("verde");
        mockMvc.perform(get("/api/v1/masini/culoare/verde")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(masini)));
    }

    @Test
    public void getAllCarsByColorException() throws Exception {
        doReturn(Optional.empty()).when(masinaRepository).getAllCarsByColor("verde");
        mockMvc.perform(get("/api/v1/masini/culoare/verde")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void addCars() throws Exception {
        Masina masina = new Masina("MasinaSpeciala", "superman", 2005, "rosu");
        Optional<Masina> masinas=Optional.of(masina);
        doReturn(Optional.empty()).when(masinaRepository).findByModel(masina.getModel());
        mockMvc.perform(post("/api/v1/masini/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(masina))).andExpect(status().isCreated());
    }

    @Test
    public void addCarsException() throws Exception{
        Masina masina = new Masina("MasinaSpeciala", "superman", 2005, "rosu");
        Optional<Masina> masinas=Optional.of(masina);
        doReturn(masinas).when(masinaRepository).findByModel(masina.getModel());
        mockMvc.perform(post("/api/v1/masini/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(masina))).andExpect(status().isBadRequest());
    }

    @Test
    public void removeCar() throws Exception {
        Masina masina = new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        Optional<Masina> masinas=Optional.of(masina);
        doReturn(masinas).when(masinaRepository).findByModel(masina.getModel());
        doNothing().when(masinaRepository).removeMasinaByModel(masina.getModel());
        mockMvc.perform(delete("/api/v1/masini/ModelSpecial").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsBytes(masina))).andExpect(status().isOk());
    }

    @Test
    public void removeCarException() throws Exception{
        Masina masina = new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        doReturn(Optional.empty()).when(masinaRepository).findByModel(masina.getModel());
        mockMvc.perform(delete("/api/v1/masini/ModelSpecial").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsBytes(masina))).andExpect(status().isBadRequest());
    }

    @Test
    public void updateCar() throws Exception{
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        Masina masina = new Masina("Mercedes Benz", "S", 2015, "black");
        Optional<Masina> masinas=Optional.of(masina);
        doNothing().when(masinaRepository).updateAn(masinaDTO.getAn(),masinaDTO.getModel());
        doNothing().when(masinaRepository).updateColor(masinaDTO.getCuloare(),masinaDTO.getModel());
        doNothing().when(masinaRepository).updateMarca(masinaDTO.getMarca(),masinaDTO.getModel());
        doReturn(masinas).when(masinaRepository).findByModel(masinaDTO.getModel());
        mockMvc.perform(put("/api/v1/masini/update").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsBytes(masinaDTO))).andExpect(status().isOk());
    }

    @Test
    public void updateCarException() throws Exception{
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        doReturn(Optional.empty()).when(masinaRepository).findByModel(masinaDTO.getModel());
        mockMvc.perform(put("/api/v1/masini/update").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsBytes(masinaDTO))).andExpect(status().isBadRequest());
    }

    @Test
    public void filterCar() throws Exception{
        MasinaDTO masinaDTO = new MasinaDTO("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        Faker faker=new Faker();
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            masini.add(new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu"));
        }
        doReturn(masini).when(masinaRepository).findAll();
        mockMvc.perform(get("/api/v1/masini/filter")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(objectMapper.writeValueAsBytes(masinaDTO)))
                .andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(masinaDTO)));
    }

    @Test
    public void filterExceptionCar() throws Exception{
        MasinaDTO masinaDTO = new MasinaDTO("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        doReturn(new ArrayList<>()).when(masinaRepository).findAll();
        mockMvc.perform(get("/api/v1/masini/filter")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(objectMapper.writeValueAsBytes(masinaDTO)))
                .andExpect(status().isBadRequest());
    }
}
