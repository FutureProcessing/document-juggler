package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.BaseRepository;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.ObjectId;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdFieldTypesIntegrationTest extends BaseIntegrationTest {

    private static final String COLLECTION_STRING_ID = "PersonsWithStringId";
    private static final String COLLECTION = "Persons";
    private static final String COLLECTION_OBJECT_ID_ID = "PersonsWithObjectIdId";

    private static final String SURNAME = "Kowalski";

    @CollectionName(COLLECTION)
    interface Person {
        @ObjectId
        @DbField("_id")
        Person withId(String id);

        @DbField("_id")
        String getId();

        @DbField("surname")
        Person withSurname(String surname);
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

    @CollectionName(COLLECTION_OBJECT_ID_ID)
    interface PersonWithObjectIdIdModel {

        @DbField("_id")
        PersonWithObjectIdIdModel withId(org.bson.types.ObjectId id);

        @DbField("_id")
        String getId();

        @DbField("surname")
        PersonWithObjectIdIdModel withSurname(String surname);
    }

    @Test
    public void shouldInsertDocumentWithDefaultDocumentId() {
        //given
        BaseRepository<Person> repository = new BaseRepository<>(db(), Person.class);
        String ID = new org.bson.types.ObjectId().toHexString();

        //when
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION).findOne();
        assertThat(fromDb.getString("_id")).isEqualTo(ID);
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }

    @Test
    public void shouldInsertDocumentWithStringDocumentId() {
        //given
        BaseRepository<PersonWithStringIdModel> repository = new BaseRepository<>(db(), PersonWithStringIdModel.class);
        String ID = "personID";

        //when
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION_STRING_ID).findOne();
        assertThat(fromDb.getString("_id")).isEqualTo(ID);
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }

    @Test
    public void shouldInsertDocumentWithObjectIdDocumentId() {
        //given
        BaseRepository<PersonWithObjectIdIdModel> repository = new BaseRepository<>(db(), PersonWithObjectIdIdModel.class);
        org.bson.types.ObjectId ID = new org.bson.types.ObjectId();

        //when
        repository.insert(p -> p.withId(ID).withSurname(SURNAME));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION_OBJECT_ID_ID).findOne();
        assertThat(fromDb.getString("_id")).isEqualTo(ID.toHexString());
        assertThat(fromDb.getString("surname")).isEqualTo(SURNAME);
    }
}
