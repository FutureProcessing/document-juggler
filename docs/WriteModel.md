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
```java
public interface Write {
    @Id
    void setId(String id);
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
    @DbField("name")
    void setName(String name);
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
    void setCount(int count);
}
```
```java
public interface Write {
    @DbField("count")
    Write withCount(Integer count);
}
```
```java
public interface Write {
    @DbField("count")
    void setCount(Integer count);
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
```java
public interface Write {
    @DbField("address")
    @DbEmbeddedDocument(Address.class)
    void setAddress(Consumer<Address> address);
}
```
Not implemented yet:
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    Write withAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    void setAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    Write withAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    void setAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```
```java
public interface Write {
    @DbField("address")
    @DbEmbeddedDocument(Address.class)
    Write withAddress(int index, Consumer<Address>... address);
}
```
```java
public interface Write {
    @DbField("address")
    @DbEmbeddedDocument(Address.class)
    void setAddress(int index, Consumer<Address>... address);
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
```java
public interface Write {
    @DbField("names")
    @AddToSet
    void addName(String name);
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
    @DbEmbeddedDocument(Address.class)
    @AddToSet
    Write addAddress(Consumer<Address> address);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    @AddToSet
    Write addAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
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
```java
public interface Write {
    @DbField("names")
    @Push
    void addName(String name);
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
    @DbEmbeddedDocument(Address.class)
    @Push
    Write addAddress(Consumer<Address> address);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    @Push
    Write addAddresses(Consumer<Address>... addresses);
}
```
```java
public interface Write {
    @DbField("addresses")
    @DbEmbeddedDocument(Address.class)
    @Push
    Write addAddresses(Consumer<Address> address1, Consumer<Address> address2);
}
```
