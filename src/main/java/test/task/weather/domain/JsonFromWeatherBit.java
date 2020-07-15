package test.task.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import test.task.weather.domain.parameters.WeatherParametersForWeatherBit;
import test.task.weather.sevices.exceptions.CityNotFoundException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "weather_list")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonFromWeatherBit extends WeatherNote {

    @Transient
    @JsonAlias("data")
    private WeatherParametersForWeatherBit[] weatherParameters;

    public JsonFromWeatherBit() {
    }

    public WeatherParametersForWeatherBit[] getWeatherParameters() {
        return weatherParameters;
    }

    public void setWeatherParameters(WeatherParametersForWeatherBit[] weatherParameters) {
        this.weatherParameters = weatherParameters;
    }


    @Override
    public void fillWeatherParameters() throws CityNotFoundException {
        if (weatherParameters[0] != null) {
            setTemperature(weatherParameters[0].getTemperature());
            setFeelsLikeTemperature(weatherParameters[0].getFeelsLikeTemperature());
            setSource("Weather Bit");
            setHumidity(weatherParameters[0].getHumidity());
            setPressure(weatherParameters[0].getPressure());
            setWindDegree(weatherParameters[0].getWindDegree());
            setWindSpeed(weatherParameters[0].getWindSpeed());
        } else {
            throw new CityNotFoundException();
        }
    }
}
