package com.futureprocessing.documentjuggler.example;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import com.futureprocessing.documentjuggler.integration.BaseIntegrationTest;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class ExamplesOfUsage extends BaseIntegrationTest {

    @Test
    public void howToUseDocumentJuggler() {
        CarsRepository carsRepository = new CarsRepository(db());

        String fordId = carsRepository.insert(car -> car
                        .withBrand("Ford")
                        .withModel("Focus")
                        .withAutomaticGearbox(true)
                        .engine(engine -> engine
                                .withFuel("Diesel")
                                .withCylindersNumber(4))
                        .withOwners("John", "Caroline")
        );

        String camaroId = carsRepository.insert(car -> car
                        .withBrand("Chevrolet")
                        .withModel("Camaro")
                        .withAutomaticGearbox(true)
                        .engine(engine -> engine
                                .withFuel("Gas")
                                .withCylindersNumber(12))
                        .withOwners("Adam")
        );


        //Find all cars
        List<Car> allCars = carsRepository.find().all();
        assertThat(allCars).hasSize(2);
        assertThat(extractProperty("brand").from(allCars)).containsExactly("Ford", "Chevrolet");


        //Find specific document
        Car camaro = carsRepository.find(car -> car.withId(camaroId)).first();
        assertThat(camaro.getBrand()).isEqualTo("Chevrolet");
        assertThat(camaro.getModel()).isEqualTo("Camaro");


        //Update
        carsRepository.find(car -> car.withBrand("Ford"))
                .update(car -> car.withModel("Mondeo"))
                .ensureOneUpdated();

        Car ford = carsRepository.find(car -> car.withId(fordId)).first();
        assertThat(ford.getModel()).isEqualTo("Mondeo");


        //Remove
        carsRepository.find(car -> car.withId(camaroId))
                .remove()
                .ensureOneDeleted();

        allCars = carsRepository.find().all();
        assertThat(allCars).hasSize(1);
        assertThat(extractProperty("brand").from(allCars)).containsExactly("Ford");
    }

}
