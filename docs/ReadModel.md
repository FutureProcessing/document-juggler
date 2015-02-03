Read Model
==========

@Id
---
Shortcut for [@DbField("_id")](#dbfield). Only possible way of use:
```java
public interface Read {
    @Id
    String getId();
}
```

@DbField
--------
All methods in read interface need to be annotated with **@DbField**, field name is required.
```java
public interface Read {
    @DbField("name")
    String getName();
}
```
```java
public interface Read {
    @DbField("count")
    int getCount();
}
```
```java
public interface Read {
    @DbField("count")
    Integer getCount();
}
```

@DbEmbeddedDocument
-------------------
Methods returning embedded documents needs to be annotated **@DbEmbeddedDocument**.
Embedded document class is required only when methods return Set or List of embedded documents.
```java
public interface Read {
    @DbField("address")
    @DbEmbeddedDocument
    Address getAddress();
}
```
Not implemented yet:
```java
public interface Read {
    @DbField("addresses")
    @DbEmbeddedDocument
    Set<Address> getAddresses();
}
```
```java
public interface Read {
    @DbField("addresses")
    @DbEmbeddedDocument
    List<Address> getAddresses();
}
```
