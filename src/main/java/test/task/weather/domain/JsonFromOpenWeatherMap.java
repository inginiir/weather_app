package test.task.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import test.task.weather.domain.parameters.WeatherParametersForOpenWeather;
import test.task.weather.domain.parameters.WindForOpenWeather;
import test.task.weather.sevices.exceptions.CityNotFoundException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "weather_list")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonFromOpenWeatherMap extends WeatherNote {

    @JsonAlias("main")
    @Transient
    private WeatherParametersForOpenWeather mainWeatherParameters;
    @Transient
    private WindForOpenWeather wind;

    public JsonFromOpenWeatherMap() {
    }

    public WeatherParametersForOpenWeather getMainWeatherParameters() {
        return mainWeatherParameters;
    }

    public void setMainWeatherParameters(WeatherParametersForOpenWeather mainWeatherParameters) {
        this.mainWeatherParameters = mainWeatherParameters;
    }

    public WindForOpenWeather getWind() {
        return wind;
    }

    public void setWind(WindForOpenWeather wind) {
        this.wind = wind;
    }

    @Override
    public void fillWeatherParameters() throws CityNotFoundException {
        if (mainWeatherParameters != null && wind != null) {
            setTemperature(mainWeatherParameters.getTemperature());
            setFeelsLikeTemperature(mainWeatherParameters.getFeelsLikeTemperature());
            setPressure(mainWeatherParameters.getPressure());
            setHumidity(mainWeatherParameters.getHumidity());
            setWindDegree(wind.getDegree());
            setWindSpeed(wind.getSpeed());
            setSource("Open Weather Map");
        } else {
            throw new CityNotFoundException();
        }
    }
}
