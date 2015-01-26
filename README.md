Mongo Juggler
=============
Fluent MongoDB Java API based on Java 8 lambdas

Using Mongo Juggler
=============

Define MongoDB document schema
--------------
```json
{
    "_id": "1",
    "brand": "Ferrari",
    "model": "F50",
    "release_date": 1421715480,
    "automatic_gearbox": false,
    "passengers_names": [
        "John",
        "Bob"
    ],
    "engine": {
        "fuel": "gas",
        "cylinders_number": 12
    }
}
```

How to insert documents
---------

#### 1. Define update interface
```java
@DbDocument("cars")
public interface CarUpdater {

    @DbField("brand")
    CarUpdater withBrand(String brand);

    @DbField("model")
    CarUpdater withModel(String model);

    @DbField("release_date")
    CarUpdater withReleaseDate(Date releaseDate);

    @DbField("automatic_gearbox")
    CarUpdater withAutomaticGearbox(boolean automaticGearbox);

    @DbField("passengers_names")
    CarUpdater withPassengerNames(List<String> passengerNames);

    @DbField("passengers_names")
    @AddToSet
    CarUpdater addPassengerName(String passengerName);

    @DbField("engine")
    @DbEmbeddedDocument(EngineUpdater.class)
    CarUpdater engine(Consumer<EngineUpdater> consumer);

}
```

#### 2. Define embedded document update interface
```java
public interface EngineUpdater {

    @DbField("fuel")
    EngineUpdater withFuel(String fuel);

    @DbField("cylinder_number")
    EngineUpdater withCylindersNumber(int cylindersNumber);
}
```

#### 3. Create repository
```java
public class CarsRepository extends Repository<Car, CarUpdater, CarQuery> {

    public CarsRepository(MongoDBProvider dbProvider) {
        super(Car.class, CarUpdater.class, CarQuery.class, dbProvider);
    }
}
```

#### 4. Enjoy!
```java
public class Example {

    @Inject
    private CarRepository repo;

    public void insert() {
        repo.insert(car -> car
                            .withBrand("Ford")
                            .engine(engine -> engine
                                                .withFuel("gas")
                                                .withCylindersNumber(4)
                            )
                            .withReleaseDate(new Date())
        );
    }
}
```


How to read documents
-------


#### 1. Define query interface
```java
@DbDocument("cars")
public interface CarQuery extends AbstractQuery<CarQuery> {

    @DbField("brand")
    CarQuery withBrand(String brand);
}
```

#### 2. Define read interface
```java
@DbDocument("cars")
public interface Car {

    @DbField("_id")
    String getId();

    @DbField("brand")
    String getBrand();

    @DbField("model")
    String getModel();

    @DbField("release_date")
    Date getReleaseDate();

    @DbField("automatic_gearbox")
    boolean isAutomaticGearbox();

    @DbField("passengers_names")
    List<String> getPassengersNames();

    @DbField("engine")
    @DbEmbeddedDocument
    Engine getEngine();
}
```

#### 3. Define embedded document read interface
```java
public interface Engine {

    @DbField("fuel")
    String getFuel();

    @DbField("cylinder_number")
    int getCylindersNumber();
}
```

#### 4. Create repository
[see How to insert documents > 3. Create repository](#3-create-repository)

#### 5. Enjoy!
```java
public class Example {

    @Inject
    private CarRepository repo;

    public void read() {
        Car car = repo.find(car -> car.withId("12")).one();

        String brand  = car.getBrand();
        int cylindersNumber = car.getEngine().getCylindersNumber();
    }
}
```

How to update documents
--------

#### 1. Define query interface
[see How to read documents > 1. Define query interface](#1-define-query-interface)

#### 2. Define update interface
[see How to insert documents > 1. Define update interface](#1-define-update-interface)

#### 3. Define embedded document update interface
[see How to insert documents > 2. Define embedded document update interface](#2-define-embedded-document-update-interface)

#### 4. Create repository
[see How to insert documents > 3. Create repository](#3-create-repository)

#### 5. Enjoy!

```java
public class Example {

    @Inject
    private CarRepository repo;

    public void update() {
        repo.update(car -> car.withId("12"))
            .with(car -> car
                        .withModel("F60")
                        .engine(engine -> engine
                                            .withFuel(newFuel)
                                            .withCylindersNumber(10)
                        )
            );
    }
}
```
