package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.futureprocessing.documentjuggler.query.expression.QueryExpression.and;
import static com.futureprocessing.documentjuggler.query.expression.QueryExpression.or;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class AndOrIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldQueryDocumentsWithOrStatement() {
        // given
        String model1 = "147";
        String model2 = "159";
        String model3 = "MiTo";

        repo.insert(car -> car.withModel(model1));
        repo.insert(car -> car.withModel(model2));
        repo.insert(car -> car.withModel(model3));

        // when
        List<Car> result = repo.find(or(
                car1 -> car1.withModel(model1),
                car2 -> car2.withModel(model2)
        )).all();

        // then
        assertThat(extractProperty("model").from(result)).containsOnly(model1, model2);
    }

    @Test
    public void shouldAllowCombiningAndOrQueries() {
        // given
        String model1 = "147";
        String model2 = "159";
        String model3 = "MiTo";

        String brand1 = "Alfa";
        String brand2 = "Romeo";

        repo.insert(car -> car.withModel(model1).withBrand(brand1));
        repo.insert(car -> car.withModel(model2).withBrand(brand1));
        repo.insert(car -> car.withModel(model3).withBrand(brand1));

        repo.insert(car -> car.withModel(model1).withBrand(brand2));
        repo.insert(car -> car.withModel(model2).withBrand(brand2));
        repo.insert(car -> car.withModel(model3).withBrand(brand2));

        // when
        List<Car> result = repo.find(
                and(
                        or(
                                car1 -> car1.withModel(model1),
                                car2 -> car2.withModel(model2)
                        ),
                        car -> car.withBrand(brand1)
                )).all();

        // then
        assertThat(extractProperty("brand").from(result)).containsOnly(brand1);
        assertThat(extractProperty("model").from(result)).containsOnly(model1, model2);
    }
}
