package test.task.weather.sevices;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import test.task.weather.domain.City;
import test.task.weather.domain.JsonFromOpenWeatherMap;
import test.task.weather.domain.JsonFromWeatherBit;
import test.task.weather.domain.WeatherNote;
import test.task.weather.domain.parameters.WeatherParametersForOpenWeather;
import test.task.weather.domain.parameters.WeatherParametersForWeatherBit;
import test.task.weather.domain.parameters.WindForOpenWeather;
import test.task.weather.repos.WeatherNoteRepo;
import test.task.weather.sevices.exceptions.BaseOfCitiesNotLoadToDatabase;
import test.task.weather.sevices.exceptions.CityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class WeatherNoteServiceTest {

    private RestTemplate restTemplate = mock(RestTemplate.class);
    private CityService cityService = mock(CityService.class);
    private WeatherNoteRepo weatherNoteRepo = mock(WeatherNoteRepo.class);
    private WeatherNoteService weatherNoteService = new WeatherNoteService(restTemplate, cityService, weatherNoteRepo);

    @Test
    void shouldThrowExceptionWhenAnswerFromApiIsNotReceived() {
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "q=city" +
                "&units=metric" +
                "&lang=ru" +
                "&appid=998c8e4bc85ec5c0bf096643987428b5";
        Mockito.doReturn(null)
                .when(restTemplate).getForObject(url, JsonFromOpenWeatherMap.class);
        assertThrows(CityNotFoundException.class, () -> weatherNoteService.getWeatherMap("city", "source"));
    }

    @Test
    void shouldThrowExceptionWhenCityTypedWithError() {
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "q=ваыпыар" +
                "&units=metric" +
                "&lang=ru" +
                "&appid=998c8e4bc85ec5c0bf096643987428b5";
        JsonFromOpenWeatherMap weatherMap = new JsonFromOpenWeatherMap();
        Mockito.doReturn(weatherMap)
                .when(restTemplate).getForObject(url, JsonFromOpenWeatherMap.class);
        assertThrows(CityNotFoundException.class, () -> weatherNoteService
                .getWeatherMap("ваыпыар", "Open Weather Map"));
    }

    @Test
    void shouldFillObjectParameters() {
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "q=Москва" +
                "&units=metric" +
                "&lang=ru" +
                "&appid=null";
        JsonFromOpenWeatherMap weatherMap = new JsonFromOpenWeatherMap();
        weatherMap.setMainWeatherParameters(new WeatherParametersForOpenWeather());
        weatherMap.setWind(new WindForOpenWeather());
        Mockito.doReturn(weatherMap)
                .when(restTemplate).getForObject(url, JsonFromOpenWeatherMap.class);
        WeatherNote weatherMapFromJson = weatherNoteService.getWeatherMap("Москва", "Open Weather Map");
        assertEquals(weatherMap, weatherMapFromJson);
    }

    @Test
    void shouldUseRightServiceDependsOnSourceField() {
        String url = "http://api.weatherbit.io/v2.0/current?" +
                "key=null" +
                "&lang=ru" +
                "&city=Челябинск";
        JsonFromWeatherBit weatherMap = new JsonFromWeatherBit();
        WeatherParametersForWeatherBit[] parametersForWeatherBits = new WeatherParametersForWeatherBit[1];
        parametersForWeatherBits[0] = new WeatherParametersForWeatherBit();
        weatherMap.setWeatherParameters(parametersForWeatherBits);
        Mockito.doReturn(weatherMap)
                .when(restTemplate).getForObject(url, JsonFromWeatherBit.class);
        weatherNoteService.getWeatherMap("Челябинск", "Weather Bit");
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForObject(url, JsonFromWeatherBit.class);
    }

    @Test
    void failSaveCitiesToDatabase() {
        String url = "http://api.travelpayouts.com/data/ru/cities.json";
        Mockito.doReturn(null)
                .when(restTemplate).getForObject(url, City.class);
        assertThrows(BaseOfCitiesNotLoadToDatabase.class, () -> weatherNoteService.saveCitiesToDatabase());
        Mockito.verify(cityService, Mockito.never()).saveAll(any());
    }

    @Test
    void successSaveCitiesToDatabase() {
        String url = "http://api.travelpayouts.com/data/ru/cities.json";
        City[] cities = new City[1];
        cities[0] = new City();
        Mockito.doReturn(cities)
                .when(restTemplate).getForObject(url, City[].class);
        assertDoesNotThrow(() -> weatherNoteService.saveCitiesToDatabase());
        Mockito.verify(cityService, Mockito.times(1)).saveAll(any());
    }

    @Test
    void saveNotes() {
        weatherNoteService.saveNotes(new JsonFromOpenWeatherMap());
        weatherNoteService.saveNotes(new JsonFromWeatherBit());
        Mockito.verify(weatherNoteRepo, Mockito.times(2)).save(any(WeatherNote.class));
    }

    @Test
    void deleteById() {
        weatherNoteService.deleteById(1L);
        Mockito.verify(weatherNoteRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void find() {
        weatherNoteService.find();
        Mockito.verify(weatherNoteRepo, Mockito.times(1)).findOpenWeather();
        Mockito.verify(weatherNoteRepo, Mockito.times(1)).findWeatherBit();
    }
}