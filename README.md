mongo-juggler
=============
Fluent Lambda base Java Mongo API

Defining Model
--------------

### Document
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

### Query interface
```java
@DbDocument("cars")
public interface CarQuery extends AbstractQuery<CarQuery> {

    @DbField("brand")
    CarQuery withBrand(String brand);
}
```

### Read interface
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

### Embedded read interface
```java
public interface Engine {

    @DbField("fuel")
    String getFuel();

    @DbField("cylinder_number")
    int getCylindersNumber();
}
```

### Update interface
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

### Embedded update interface
```java
public interface EngineUpdater {

    @DbField("fuel")
    EngineUpdater withFuel(String fuel);

    @DbField("cylinder_number")
    EngineUpdater withCylindersNumber(int cylindersNumber);
}
```

### Repository
```java
public class CarsRepository extends Repository<Car, CarUpdater, CarQuery> {

    public CarsRepository(MongoDBProvider dbProvider) {
        super(Car.class, CarUpdater.class, CarQuery.class, dbProvider);
    }
}
```

Usage examples
--------------

### Read field from document
```java
public class Example {

    @Inject
    private CarRepository repo;

    public String read() {
        Car car = repo.find(car -> car.withId("12")).one();
        return car.getBrand();
    }
}
```

### Read field from embedded document
```java
public class Example {

    @Inject
    private CarRepository repo;

    public int read() {
        Car car = repo.find(car -> car.withId("12")).one();
        return car.getEngine().getCylindersNumber();
    }
}
```

### Insert document
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

### Update document
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
