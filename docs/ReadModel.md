Read Model
==========

@Id
---
Shortcut for @DbField("_id"). Only possible way of use:
```java
@Id
String getId();
```

@DbField
--------
All methods in read interface need to be annotated, field name is required.
```java
@DbField("name")
String getName();
```
```java
@DbField("count")
int getCount();
```
```java
@DbField("count")
Integer getCount();
```
