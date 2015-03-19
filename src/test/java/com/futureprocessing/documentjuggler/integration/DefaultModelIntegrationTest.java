package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.BaseRepository;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.model.DefaultModel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultModelIntegrationTest extends BaseIntegrationTest {

    BaseRepository<Document> repository = new BaseRepository<>(db(), Document.class);

    @CollectionName("docs")
    private interface Document extends DefaultModel<Document> {

        @DbField("field")
        String getField();

        @DbField("field")
        Document withField(String field);
    }

    @Test
    public void shouldFindDocumentWithId() {
        //given
        String id = repository.insert(doc -> doc.withField("Abc"));
        String id2 = repository.insert(doc -> doc.withField("Def"));

        //when
        Document document = repository.find(doc -> doc.withId(id)).first();
        Document document2 = repository.find(doc -> doc.withId(id2)).first();

        //then
        assertThat(document.getId()).isEqualTo(id);
        assertThat(document.getField()).isEqualTo("Abc");

        assertThat(document2.getId()).isEqualTo(id2);
        assertThat(document2.getField()).isEqualTo("Def");
    }

}
