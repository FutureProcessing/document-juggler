Write Model
==========

@Id
---
Shortcut for [@DbField("_id")](#dbfield). Will throe exception when used during update, only for inserts.
Only possible ways of use:
```java
public interface Write {
    @Id
    Write withId(String id);
}
```

@DbField
--------
All methods in write interface need to be annotated with **@DbField**, field name is required.
```java
public interface Write {
    @DbField("name")
    Write withName(String name);
}
```
```java
public interface Write {
    @DbField("count")
    Write withCount(int count);
}
```
```java
public interface Write {
    @DbField("count")
    Write withCount(Integer count);
}
```

@DbEmbeddedDocument
-------------------
Methods updating embedded documents needs to be annotated **@DbEmbeddedDocument**. Embedded document class is required.
```java
public interface Write {
    @DbField("address")
    @DbEmbeddedDocument(Address.class)
    Write withAddress(Consumer<Address> address);
}
```
Not implemented yet:
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    Write withAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    Write withAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```
```java
public interface Write {
    @DbField("address")
    @DbEmbeddedDocument
    Write withAddress(int index, Consumer<Address>... address);
}
```

@AddToSet
---------
Performs **$addToSet** operation.
```java
public interface Write {
    @DbField("names")
    @AddToSet
    Write addName(String name);
}
```
Not implemented yet
```java
public interface Write {
    @DbField("names")
    @AddToSet
    Write addNames(String... names);
}
```
```java
public interface Write {
    @DbField("names")
    @AddToSet
    Write addNames(String[] names);
}
```
```java
public interface Write {
    @DbField("names")
    @AddToSet
    Write addNames(Collection<String> names);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @AddToSet
    Write addAddress(Consumer<Address> address);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @AddToSet
    Write addAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @AddToSet
    Write addAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```

@Push
-----
Performs **$push** operation.
```java
public interface Write {
    @DbField("names")
    @Push
    Write addName(String name);
}
```
Not implemented yet
```java
public interface Write {
    @DbField("names")
    @Push
    Write addNames(String... names);
}
```
```java
public interface Write {
    @DbField("names")
    @Push
    Write addNames(String[] names);
}
```
```java
public interface Write {
    @DbField("names")
    @Push
    Write addNames(Collection<String> names);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @Push
    Write addAddress(Consumer<Address> address);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @Push
    Write addAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument
    @Push
    Write addAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```
