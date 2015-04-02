package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.query.expression.QueryExpression.nor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NorTest {

    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;

    @Before
    public void before() {
        given(db.getCollection(any())).willReturn(collection);
        carsRepository = new CarsRepository(db);
    }

    @Test
    public void shouldCreateQueryWithEmbeddedField() {
        // given
        String value1 = "model1";
        String value2 = "model2";

        Consumer<Car> car1 = model -> model.withModel(value1);
        Consumer<Car> car2 = model -> model.withModel(value2);
        given(collection.findOne(any(), any())).willReturn(new BasicDBObject());

        DBObject expectedQuery = new BasicDBObject("$nor", new DBObject[]{new BasicDBObject("model", value1), new BasicDBObject("model", value2)});

        //when
        carsRepository.find(nor(car1, car2)).first();

        //then
        verify(collection).findOne(eq(expectedQuery), any());
    }

}
