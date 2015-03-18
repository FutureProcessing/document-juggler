package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.BaseRepository;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.ObjectId;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionNameAnnotationIntegrationTest extends BaseIntegrationTest {

    private static final String COLLECTION = "Persons";

    @CollectionName(COLLECTION)
    interface PersonModel {

        @ObjectId
        @DbField("_id")
        PersonModel withId(String id);

        @DbField("surname")
        PersonModel withSurname(String surname);

        @DbField("surname")
        String getSurname();
    }

    @Test
    public void shouldInsertDocumentInCorrectCollection() {
        //given
        BaseRepository<PersonModel> repository = new BaseRepository<>(db(), PersonModel.class);

        //when
        repository.insert(p -> p.withSurname("Kowalski"));

        //then
        BasicDBObject fromDb = (BasicDBObject) db().getCollection(COLLECTION).findOne();

        assertThat(fromDb.getString("surname")).isEqualTo("Kowalski");
    }

}
