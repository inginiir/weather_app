package test.task.weather.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/weather-list-before.sql", "/load-city-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/weather-list-after.sql", "/load-city-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoad() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Данный погодный сервис позволяет искать")));
    }

    @Test
    void weatherAndCityListExist() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id='weather-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='citiesList']/option").nodeCount(1));
    }

    @Test
    void shouldWarningFieldIfCityEmpty() throws Exception {
        this.mockMvc.perform(post("/")
                .param("city", "")
                .param("weatherService", "Open Weather Map"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='cityErr']/div").string("Поле город не может быть пустым!"));
    }

    @Test
    void shouldWarningFieldIfCityNotFound() throws Exception {
        this.mockMvc.perform(post("/")
                .param("city", "fakeCity")
                .param("weatherService", "Open Weather Map"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='cityErr']/div").string("Город не найден в базе!"));
    }

    @Test
    void addWeatherNoteOpenWeather() throws Exception {
        this.mockMvc.perform(post("/")
                .param("city", "Челябинск")
                .param("weatherService", "Open Weather Map"))
                .andDo(print())
                .andExpect(xpath("//div[@id='weather-list']/div").nodeCount(3))
                .andExpect(xpath("//div[@id='weather-list']/div[@data-id='3']").exists());
    }

    @Test
    void addWeatherNoteWeatherBit() throws Exception {
        this.mockMvc.perform(post("/")
                .param("city", "Челябинск")
                .param("weatherService", "Weather Bit"))
                .andDo(print())
                .andExpect(xpath("//div[@id='weather-list']/div").nodeCount(3))
                .andExpect(xpath("//div[@id='weather-list']/div[@data-id='3']").exists());
    }

    @Test
    void successDeleteNote() throws Exception {
        this.mockMvc.perform(get("/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void failDeleteNote() throws Exception {
        this.mockMvc.perform(get("/fhd"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}