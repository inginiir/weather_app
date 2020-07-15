package test.task.weather.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.task.weather.domain.City;
import test.task.weather.domain.WeatherNote;
import test.task.weather.sevices.CityService;
import test.task.weather.sevices.WeatherNoteService;
import test.task.weather.sevices.exceptions.BaseOfCitiesNotLoadToDatabase;
import test.task.weather.sevices.exceptions.CityNotFoundException;

@Controller
public class WeatherController {

    private WeatherNoteService weatherNoteService;
    private CityService cityService;

    public WeatherController(WeatherNoteService weatherNoteService, CityService cityService) {
        this.weatherNoteService = weatherNoteService;
        this.cityService = cityService;
    }

    @GetMapping("/")
    public String main(
            Model model
    ) {
        Iterable<City> allCities = cityService.findAll();
        if (!allCities.iterator().hasNext()) {
            try {
                weatherNoteService.saveCitiesToDatabase();
            } catch (BaseOfCitiesNotLoadToDatabase baseOfCitiesNotLoadToDatabase) {
                model.addAttribute("citiesError", "Города не загружены из базы, просьба связаться с разработчиками");
                model.addAttribute("weatherList", weatherNoteService.find());
                return "main";
            }
            return "redirect:/";
        }
        model.addAttribute("cities", allCities);
        model.addAttribute("weatherList", weatherNoteService.find());
        return "main";
    }

    @PostMapping("/")
    public String getWeather(
            @RequestParam("city") String city,
            @RequestParam("weatherService") String weatherService,
            Model model
    ) {
        if (StringUtils.isEmpty(city)) {
            model.addAttribute("cityError", "Поле город не может быть пустым!");
            model.addAttribute("cities", cityService.findAll());
            model.addAttribute("weatherList", weatherNoteService.find());
            return "main";
        }
        WeatherNote weatherMap;
        try {
            weatherMap = weatherNoteService.getWeatherMap(city, weatherService);
        } catch (CityNotFoundException e) {
            model.addAttribute("cityError", "Город не найден в базе!");
            model.addAttribute("cities", cityService.findAll());
            model.addAttribute("weatherList", weatherNoteService.find());
            return "main";
        }
        weatherNoteService.saveNotes(weatherMap);
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("weatherList", weatherNoteService.find());
        return "main";
    }

    @GetMapping("/{id}")
    public String deleteNote(@PathVariable String id) {
        Long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "redirect:/";
        }
        weatherNoteService.deleteById(longId);
        return "redirect:/";
    }
}

