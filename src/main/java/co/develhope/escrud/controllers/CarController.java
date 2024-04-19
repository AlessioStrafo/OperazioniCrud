package co.develhope.escrud.controllers;

import co.develhope.escrud.entities.Car;
import co.develhope.escrud.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;
    @PostMapping
    public Car create(@RequestBody Car car){
        car.setId(null);
        Car carsaved = carRepository.save(car);
        return carsaved;
    }
    @GetMapping
    public Page<Car> getCarList(@RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size){
        if (page.isPresent() && size.isPresent()) {
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "modelName"), new Sort.Order(Sort.Direction.DESC, "type"));
            Pageable pageable = PageRequest.of(page.get(), size.get(), sort);
            Page<Car> cars = carRepository.findAll(pageable);
            return cars;
        } else {
            Page<Car> pageCars = Page.empty();
            return pageCars;
        }
    }
    @GetMapping("/{id}")
    public Car getSingleCar(@PathVariable long id) {
        if (carRepository.existsById(id)) {
            Car car = carRepository.findById(id).get();
            return car;
        } else {
            return new Car();
        }
    }
    @PutMapping("/{id}")
    public Car updateCarType(@PathVariable long id, @RequestParam(required = false) String type) {
        if (carRepository.existsById(id)) {
            Car myCar = carRepository.findById(id).get();
            myCar.setType(type);
            return carRepository.saveAndFlush(myCar);
        } else {
            return new Car();
        }
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSingleCar(@PathVariable long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @DeleteMapping("")
    public void deleteAllCars() {
        carRepository.deleteAll();
    }
}
