ToDo in Mongo Juggler
=======================

Integration tests on real mongo
-------------------------------
Use Embedded Mongo

Reader Set support
------------------
Reader should return Set object, converting underlying list to it.


Validation of interfaces
------------------------
Reader, Updater and Query interfaces should be validated during Repository creation.
- [ ] Reader
- [ ] Updater
- [ ] Query


Interface for limit, skip and all
---------------------------------
Querying should allow paging, by limit/skip methods.
Query should have method to return all results matching query.
- [ ] limit
- [ ] skip
- [ ] all


ObjectId instead of Strings
---------------------------
- [ ] In database Id should be stored as ObjectId.
- [X] repo.insert(...) should return String representation of ObjectId.
- [ ] repo.insert(...) should throw exception with information what went wrong
```java
String retursString = repo.insert();
```

Update should return update result object
-----------------------------------------
- [X] UpdateResult object should have methods to validate if update was succesfull.
- [X] It should throw exceptions.
- [X] It should also allow user to specify his own validation logic.
```java
UpdateResult result = repo.update();
result.ensureOneUpdated();
result.ensureUpdated(20);
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
- $inc
- $currentDate

Update with {upsert: true}
----------------
Add createOrUpdate method (with {upsert: true} behind the scenes).

findAndModify() support
----------------
Add a possibility to return documents during an update (before or after update - this is configurable in mongo).

Possibility to choose between save() and update()
----------------
update() can crete or update document, while save() can create or replace whole existing document.
