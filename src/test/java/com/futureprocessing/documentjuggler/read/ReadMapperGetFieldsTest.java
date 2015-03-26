package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.Context;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReadMapperGetFieldsTest {

    private interface OnlyReadModel {
        @DbField("fieldA")
        String getFieldA();

        @DbField("fieldB")
        String getFieldB();
    }

    @Test
    public void shouldReturnAllFieldsFromModel() {
        //given
        ReadMapper mapper = ReadMapper.map(OnlyReadModel.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsExactly("fieldA", "fieldB");
    }

    private interface WriteAndReadModel {
        @DbField("fieldC")
        WriteAndReadModel withFieldC(String value);

        @DbField("fieldA")
        String getFieldA();

        @DbField("fieldB")
        String getFieldB();
    }

    @Test
    public void shouldReturnOnlyReadFieldsFromModel() {
        //given
        ReadMapper mapper = ReadMapper.map(WriteAndReadModel.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsExactly("fieldA", "fieldB");
    }

    private interface ForbiddenFieldModel {
        @DbField("fieldA")
        String getFieldA();

        @Forbidden(Context.READ)
        @DbField("fieldB")
        String getFieldB();
    }

    @Test
    public void shouldReturnOnlyNotForbiddenReadFieldsFromModel() {
        //given
        ReadMapper mapper = ReadMapper.map(ForbiddenFieldModel.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsExactly("fieldA");
    }

    private interface ModelWithEmbeddedDocument {

        @DbField("fieldA")
        String getFieldA();

        @DbField("fieldB")
        String getFieldB();

        @DbEmbeddedDocument
        @DbField("embeddedDoc")
        EmbeddedModel embeddedDoc();
    }

    private interface EmbeddedModel {
        @DbField("fieldC")
        String getFieldC();

        @DbField("fieldD")
        String getFieldD();
    }

    @Test
    public void shouldReturnOnlyReadFieldsFromEmbeddedModel() {
        //given
        ReadMapper mapper = ReadMapper.map(ModelWithEmbeddedDocument.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsOnly("fieldA", "fieldB", "embeddedDoc.fieldC", "embeddedDoc.fieldD");
    }

    private interface ModelWithTwiceEmbeddedDocuments {
        @DbField("fieldA")
        String getFieldA();

        @DbEmbeddedDocument
        @DbField("firstLevel")
        EmbeddedModelWithEmbedded firstLevel();
    }

    private interface EmbeddedModelWithEmbedded {
        @DbField("fieldB")
        String getFieldB();

        @DbEmbeddedDocument
        @DbField("mostEmbedded")
        MostEmbeddedModel mostEmbedded();
    }

    private interface MostEmbeddedModel {
        @DbField("fieldC")
        String getFieldC();
    }

    @Test
    public void shouldReturnOnlyReadFieldsFromTwiceEmbeddedModel() {
        //given
        ReadMapper mapper = ReadMapper.map(ModelWithTwiceEmbeddedDocuments.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsOnly("fieldA", "firstLevel.fieldB", "firstLevel.mostEmbedded.fieldC");
    }

    private interface ModelWithTwoSameEmbeddedDocuments {
        @DbField("fieldA")
        String getFieldA();

        @DbEmbeddedDocument
        @DbField("firstEmbedded")
        EmbeddedModel getEmbeddedModelA();

        @DbEmbeddedDocument
        @DbField("secondEmbedded")
        EmbeddedModel getEmbeddedModelB();
    }

    @Test
    public void shouldReturnAllReadFieldsFromAllEmbeddedDocuments() {
        //given
        ReadMapper mapper = ReadMapper.map(ModelWithTwoSameEmbeddedDocuments.class);

        //when
        Set<String> fieldNames = mapper.getSupportedFields();

        //then
        assertThat(fieldNames).containsOnly("fieldA", "firstEmbedded.fieldC", "firstEmbedded.fieldD",
                "secondEmbedded.fieldC", "secondEmbedded.fieldD");
    }
}
