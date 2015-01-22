mongo-juggler
=============
Fluent Lambda base Java Mongo API

Collaboration
-------------
1. DO NOT COMMIT TO master or develop
2. CREATE FEATURE BRANCH
3. ONLY Szymon Sobocik OR Bartosz Niziołek ARE ALLOWED TO MERGE PULL REQUESTS

Model example
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

Inserting
---------

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

### How to

#### Define update interface
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

#### Define embedded document update interface
```java
public interface EngineUpdater {

    @DbField("fuel")
    EngineUpdater withFuel(String fuel);

    @DbField("cylinder_number")
    EngineUpdater withCylindersNumber(int cylindersNumber);
}
```

#### Create repository
```java
public class CarsRepository extends Repository<Car, CarUpdater, CarQuery> {

    public CarsRepository(MongoDBProvider dbProvider) {
        super(Car.class, CarUpdater.class, CarQuery.class, dbProvider);
    }
}
```

Reading
-------

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

### How to

#### Define query interface
```java
@DbDocument("cars")
public interface CarQuery extends AbstractQuery<CarQuery> {

    @DbField("brand")
    CarQuery withBrand(String brand);
}
```

#### Define read interface
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

#### Define embedded document read interface
```java
public interface Engine {

    @DbField("fuel")
    String getFuel();

    @DbField("cylinder_number")
    int getCylindersNumber();
}
```

#### Create repository
[see Inserting > How to > 3. Create repository](#create-repository)

Updating
--------

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

### How to

#### Define query interface
[see Reading > How to > 1. Define query interface](#define-query-interface)

#### Define update interface
[see Inserting > How to > 1. Define update interface](#define-update-interface)

#### Define embedded document update interface
[see Inserting > 2. Define embedded document update interface](#define-embedded-document-update-interface)

#### Create repository
[see Insert > 3. Create repository](#create-repository)
