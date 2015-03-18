package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.exception.validation.InvalidCollectionException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.assertThat;
import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.failExpectedException;
import static com.futureprocessing.documentjuggler.commons.CollectionExtractor.getCollectionName;
import static com.futureprocessing.documentjuggler.commons.CollectionExtractor.getDBCollection;


@RunWith(MockitoJUnitRunner.class)
public class CollectionNameAnnotationTest {

    private static final String EXPECTED_COLLECTION = "CollectionName";

    @Mock
    DB mockDB;

    @Mock
    DBCollection mockDBCollection;

    @CollectionName(EXPECTED_COLLECTION)
    interface ModelWithDBCollectionAnnotation {

    }

    @Test
    public void shouldReadCollectionNameFromModel() {
        //given

        //when
        String collectionName = getCollectionName(ModelWithDBCollectionAnnotation.class);

        //then
        assertThat(collectionName).isEqualTo(EXPECTED_COLLECTION);
    }

    @Test
    public void shouldGetCollectionFromModel() {
        //given
        BDDMockito.given(mockDB.getCollection(EXPECTED_COLLECTION)).willReturn(mockDBCollection);

        //when
        DBCollection dbCollection = getDBCollection(mockDB, ModelWithDBCollectionAnnotation.class);

        //then
        assertThat(dbCollection).isNotNull().isSameAs(mockDBCollection);
    }

    interface ModelWithoutAnnotation {

    }

    @Test
    public void shouldThrowExceptionWhenDbCollectionAnnotationNotPresent() {
        //given

        //when
        try {
            getDBCollection(mockDB, ModelWithoutAnnotation.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidCollectionException.class)
                    .hasMessageContaining(CollectionName.class.getCanonicalName());
            return;
        }
        failExpectedException();
    }


}
