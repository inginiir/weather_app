package test.task.weather.sevices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.task.weather.domain.City;
import test.task.weather.domain.JsonFromOpenWeatherMap;
import test.task.weather.domain.JsonFromWeatherBit;
import test.task.weather.domain.WeatherNote;
import test.task.weather.repos.WeatherNoteRepo;
import test.task.weather.sevices.exceptions.BaseOfCitiesNotLoadToDatabase;
import test.task.weather.sevices.exceptions.CityNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class WeatherNoteService {

    @Value("${openweathermap.token}")
    private String openWeatherToken;
    @Value("${weatherbit.token}")
    private String weatherBitToken;
    private RestTemplate restTemplate;
    private CityService cityService;
    private WeatherNoteRepo weatherNoteRepo;

    public WeatherNoteService(RestTemplate restTemplate, CityService cityService, WeatherNoteRepo weatherNoteRepo) {
        this.restTemplate = restTemplate;
        this.cityService = cityService;
        this.weatherNoteRepo = weatherNoteRepo;
    }

    public WeatherNote getWeatherMap(
            String city, String source
    ) throws CityNotFoundException {
        String uri = "";
        WeatherNote weatherNote = null;
        if (source.equals("Open Weather Map")) {
            uri = "http://api.openweathermap.org/data/2.5/weather?" +
                    "q=" + city +
                    "&units=metric" +
                    "&lang=ru" +
                    "&appid=" + openWeatherToken;
            weatherNote = restTemplate.getForObject(uri, JsonFromOpenWeatherMap.class);
        } else if (source.equals("Weather Bit")) {
            uri = "http://api.weatherbit.io/v2.0/current?" +
                    "key=" + weatherBitToken +
                    "&lang=ru" +
                    "&city=" + city;
            weatherNote = restTemplate.getForObject(uri, JsonFromWeatherBit.class);
            if (weatherNote != null) {
                weatherNote.setCityName(city);
            }
        }
        if (weatherNote != null) {
            weatherNote.fillWeatherParameters();
        } else {
            throw new CityNotFoundException();
        }
        return weatherNote;
    }

    public void saveCitiesToDatabase() throws BaseOfCitiesNotLoadToDatabase {
        City[] cities = restTemplate.getForObject("http://api.travelpayouts.com/data/ru/cities.json",
                City[].class);
        if (cities != null) {
            Iterable<City> processedCities = Arrays.stream(cities)
                    .filter(city -> city.getName() != null && city.getCountryCode() != null && city.getCountryCode().equals("RU"))
                    .collect(Collectors.toSet());
            cityService.saveAll(processedCities);
        } else {
            throw new BaseOfCitiesNotLoadToDatabase();
        }
    }

    public void saveNotes(WeatherNote weatherMap) {
        weatherNoteRepo.save(weatherMap);
    }

    public void deleteById(Long id) {
        weatherNoteRepo.deleteById(id);
    }

    public Collection<WeatherNote> find() {
        Collection<JsonFromOpenWeatherMap> openWeather = weatherNoteRepo.findOpenWeather();
        Collection<JsonFromWeatherBit> weatherBits = weatherNoteRepo.findWeatherBit();
        Collection<WeatherNote> weatherNotes = new ArrayList<>();
        weatherNotes.addAll(openWeather);
        weatherNotes.addAll(weatherBits);
        return weatherNotes.stream()
                .sorted(Comparator.comparing(WeatherNote::getDateOfRequest).reversed())
                .collect(Collectors.toList());
    }
}
