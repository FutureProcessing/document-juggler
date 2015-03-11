ToDo in Mongo Juggler
=======================

## Multiple arguments types for updating collections:
- [ ] Updating sub-document in collection

```java
carRepo.update(car -> car.withId("abc"))
    .with(car -> car.whell(2, wheel -> wheel.withTyre("Pirelli")))
```

ObjectId instead of Strings
---------------------------
- [ ] repo.insert(...) should throw exception with information what went wrong
```java
String retursString = repo.insert();
```

Bulk insert
-----------
Repo method for inserting multiple documents in one mongo command, for example:
```java
carsRepository.bulkInsert(car -> car.addPassengerName(newPassenger), car -> car.addPassengerName(newPassenger2));
```

Update modifiers
----------------
Add support for other update modifiers. For instance:
- [ ] $currentDate

Update with {upsert: true}
----------------
- [ ] Add createOrUpdate method (with {upsert: true} behind the scenes).

findAndModify() support
----------------
- [ ] Add a possibility to return documents during an update (before or after update - this is configurable in mongo).
