package ro.mycode.autovitapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.mycode.autovitapi.dto.MasinaDTO;
import ro.mycode.autovitapi.exceptii.EmptyDatabaseMasiniException;
import ro.mycode.autovitapi.exceptii.MasinaAlreadyExistsException;
import ro.mycode.autovitapi.exceptii.MasinaDoesntExistException;
import ro.mycode.autovitapi.model.Masina;
import ro.mycode.autovitapi.service.MasinaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CarResourceTest {

    @Mock
    private MasinaService masinaService;


    @InjectMocks
    private CarResource carResource;


    private ObjectMapper mapper = new ObjectMapper();


    private MockMvc restMockMvc;


    @BeforeEach
    void initialConfig() {
        restMockMvc = MockMvcBuilders.standaloneSetup(carResource).build();
    }


    @Test
    public void getAllCarsTest() throws Exception {
        Faker faker = new Faker();
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            masini.add(new Masina(faker.lorem().sentence(), faker.lorem().sentence(), faker.number().numberBetween(2015, 2022), "verde"));
        }
        masini.add(new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu"));
        doReturn(masini).when(masinaService).getAllCars();
        restMockMvc.perform(get("/api/v1/masini/all").contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(masini)));
    }

    @Test
    public void getAllCarsTestBadStatus() throws Exception {
        doThrow(EmptyDatabaseMasiniException.class).when(masinaService).getAllCars();
        restMockMvc.perform(get("/api/v1/masini/all").contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCarsByColorTest() throws Exception {
        Faker faker = new Faker();
        List<Masina> masini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            masini.add(new Masina(faker.lorem().sentence(), faker.lorem().sentence(), faker.number().numberBetween(2015, 2022), "verde"));
        }
        Optional<List<Masina>> masinas = Optional.of(masini);
        doReturn(masinas).when(masinaService).getAllCarsByColor("verde");
        restMockMvc.perform(get("/api/v1/masini/culoare/verde")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(masini)));
    }

    @Test
    public void getAllCarsByColorTestException() throws Exception {
        doThrow(MasinaDoesntExistException.class).when(masinaService).getAllCarsByColor("ROSU");
        restMockMvc.perform(get("/api/v1/masini/culoare/ROSU")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }

    @Test
    public void addCarsTest() throws Exception {

        Masina masina = (new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu"));
        doNothing().when(masinaService).add(masina);
        restMockMvc.perform(post("/api/v1/masini/add").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masina))).andExpect(status().isCreated());
    }

    @Test
    public void addCarsTestException() throws Exception {

        Masina masina = new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        doThrow(MasinaAlreadyExistsException.class).when(masinaService).add(masina);
        restMockMvc.perform(post("/api/v1/masini/add").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masina))).andExpect(status().isBadRequest());
    }


    @Test
    public void removeCarsTest() throws Exception {
        Masina masina = new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        doNothing().when(masinaService).removeByModel(masina.getModel());
        restMockMvc.perform(delete("/api/v1/masini/ModelSpecial").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masina))).andExpect(status().isOk());
    }

    @Test
    public void removeCarsTestException() throws Exception {
        Masina masina = new Masina("MasinaSpeciala", "ModelSpecial", 2005, "rosu");
        doThrow(MasinaDoesntExistException.class).when(masinaService).removeByModel(masina.getModel());
        restMockMvc.perform(delete("/api/v1/masini/ModelSpecial").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masina))).andExpect(status().isBadRequest());
    }


    @Test
    public void updateCarsTest() throws Exception {
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        doNothing().when(masinaService).update(masinaDTO);
        restMockMvc.perform(put("/api/v1/masini/update").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masinaDTO))).andExpect(status().isOk());
    }

    @Test
    public void updateCarsTestException() throws Exception {
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        doThrow(MasinaDoesntExistException.class).when(masinaService).update(masinaDTO);
        restMockMvc.perform(put("/api/v1/masini/update").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(masinaDTO))).andExpect(status().isBadRequest());
    }

    @Test
    public void filterTest() throws Exception {
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        Optional<MasinaDTO> masinaDTO1=Optional.of(masinaDTO);
        doReturn(masinaDTO1).when(masinaService).filter(masinaDTO);
        restMockMvc.perform(get("/api/v1/masini/filter")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(mapper.writeValueAsBytes(masinaDTO)))
                .andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(masinaDTO)));
    }

    @Test
    public void filterTestException() throws Exception {
        MasinaDTO masinaDTO = new MasinaDTO("Mercedes", "S", 2005, "verde");
        doThrow(EmptyDatabaseMasiniException.class).when(masinaService).filter(masinaDTO);
        restMockMvc.perform(get("/api/v1/masini/filter")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(mapper.writeValueAsBytes(masinaDTO)))
                       .andExpect(status().isBadRequest());
    }

}