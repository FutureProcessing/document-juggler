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
- [ ] UpdateResult object should have methods to validate if update was succesfull.
- [ ] It should throw exceptions.
- [ ] It should also allow user to specify his own validation logic.
```java
UpdateResult result = repo.update();
result.ensureOneUpdated();
result.ensureUpdated(20);
```



