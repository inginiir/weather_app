package test.task.weather.repos;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.task.weather.domain.JsonFromOpenWeatherMap;
import test.task.weather.domain.JsonFromWeatherBit;
import test.task.weather.domain.WeatherNote;

import java.util.Collection;
import java.util.List;

@Repository
public interface WeatherNoteRepo extends CrudRepository<WeatherNote, Long> {

    <S extends WeatherNote> S save(S entity);

    @Query(value = "select * from weather_list where source='Open Weather Map'", nativeQuery = true)
    Collection<JsonFromOpenWeatherMap> findOpenWeather();

    @Query(value = "select * from weather_list where source='Weather Bit'", nativeQuery = true)
    Collection<JsonFromWeatherBit> findWeatherBit();

    @Modifying
    @Query(value = "delete from  weather_list where id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
