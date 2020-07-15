package test.task.weather.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.task.weather.domain.City;

@Repository
public interface CityRepo extends CrudRepository<City, Long> {

    Iterable<City> findByOrderByNameAsc();

}
