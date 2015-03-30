package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdFieldTypesIntegrationTest extends BaseIntegrationTest {

    private static final String COLLECTION_STRING_ID = "PersonsWithStringId";
    private static final String COLLECTION = "Persons";
    private static final String COLLECTION_OBJECT_ID_AS_ID = "PersonsWithObjectIdId";

    private static final String SURNAME = "Kowalski";

    @CollectionName(COLLECTION)
    interface Person {
        @AsObjectId
        @DbField("_id")
        Person withId(String id);

        @AsObjectId
        @DbField("_id")
        String getId();

        @DbField("_id")
        ObjectId getIdAsObjectID();

        @DbField("surname")
        Person withSurname(String surname);
    }

    @Test
    public void shouldInsertDocumentWithDefaultDocumentId() {
        //given
        Repository<Person> repository = new Repository<>(db(), Person.class);
        final ObjectId ID = new ObjectId();
        final String idAsString = ID.toHexString();

        //when
        repository.insert(p -> p.withId(idAsString).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION).findOne();
        assertThat(fromDb.get("_id")).isEqualTo(ID);
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }

    @Test
    public void shouldReadIdSavedAsObjectId() {
        //given
        Repository<Person> repository = new Repository<>(db(), Person.class);
        final String idAsString = new ObjectId().toHexString();
        repository.insert(p -> p.withId(idAsString).withSurname(SURNAME));

        //when
        Person person = repository.find(p -> p.withId(idAsString)).first();

        //then
        assertThat(person.getId()).isEqualTo(idAsString);
    }

    @Test
    public void shouldReadObjectIdSavedAsObjectIdWithinTheSameModel() {
        //given
        Repository<Person> repository = new Repository<>(db(), Person.class);
        final ObjectId ID = new ObjectId();
        final String idAsString = ID.toHexString();
        repository.insert(p -> p.withId(idAsString).withSurname(SURNAME));

        //when
        Person person = repository.find(p -> p.withId(idAsString)).first();

        //then
        assertThat(person.getIdAsObjectID()).isEqualTo(ID);
    }

    @CollectionName(COLLECTION_STRING_ID)
    interface PersonWithStringIdModel {

        @DbField("_id")
        PersonWithStringIdModel withId(String id);

        @DbField("_id")
        String getId();

        @DbField("surname")
        PersonWithStringIdModel withSurname(String surname);
    }

    @Test
    public void shouldInsertDocumentWithStringDocumentId() {
        //given
        Repository<PersonWithStringIdModel> repository = new Repository<>(db(), PersonWithStringIdModel.class);
        String ID = "personID";

        //when
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION_STRING_ID).findOne();
        assertThat(fromDb.getString("_id")).isEqualTo(ID);
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }

    @Test
    public void shouldReadIdSavedAsString() {
        //given
        Repository<PersonWithStringIdModel> repository = new Repository<>(db(), PersonWithStringIdModel.class);
        final String ID = "personID";
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //when
        PersonWithStringIdModel person = repository.find(p -> p.withId(ID)).first();

        //then
        assertThat(person.getId()).isEqualTo(ID);
    }


    @CollectionName(COLLECTION_OBJECT_ID_AS_ID)
    interface PersonWithObjectIdAsId {

        @DbField("_id")
        PersonWithObjectIdAsId withId(ObjectId id);

        @DbField("_id")
        ObjectId getId();

        @DbField("surname")
        PersonWithObjectIdAsId withSurname(String surname);
    }

    @Test
    public void shouldInsertDocumentWithObjectIdDocumentId() {
        //given
        Repository<PersonWithObjectIdAsId> repository = new Repository<>(db(), PersonWithObjectIdAsId.class);
        final ObjectId ID = new ObjectId();

        //when
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION_OBJECT_ID_AS_ID).findOne();
        assertThat(fromDb.get("_id")).isEqualTo(ID);
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }

    @Test
    public void shouldReadObjectIdSavedAsObjectId() {
        //given
        Repository<PersonWithObjectIdAsId> repository = new Repository<>(db(), PersonWithObjectIdAsId.class);
        final ObjectId ID = new ObjectId();
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //when
        PersonWithObjectIdAsId person = repository.find(p -> p.withId(ID)).first();

        //then
        assertThat(person.getId()).isEqualTo(ID);
    }
}
