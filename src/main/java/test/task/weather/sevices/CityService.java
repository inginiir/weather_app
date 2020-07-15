package test.task.weather.sevices;

import org.springframework.stereotype.Service;
import test.task.weather.domain.City;
import test.task.weather.repos.CityRepo;

@Service
public class CityService {

    private CityRepo cityRepo;

    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    void saveAll(Iterable<City> cities) {
        cityRepo.saveAll(cities);
    }

    public Iterable<City> findAll() {
        return cityRepo.findByOrderByNameAsc();
    }
}
