package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbCollection;
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


@RunWith(MockitoJUnitRunner.class)
public class DbCollectionAnnotationTest {

    private static final String EXPECTED_COLLECTION = "CollectionName";

    @Mock
    DB mockDB;

    @Mock
    DBCollection mockDBCollection;

    @DbCollection(EXPECTED_COLLECTION)
    interface ModelWithDBCollectionAnnotation {

    }

    @Test
    public void shouldReadCollectionNameFromModel() {
        //given

        //when
        String collectionName = CollectionExtractor.getCollectionName(ModelWithDBCollectionAnnotation.class);

        //then
        assertThat(collectionName).isEqualTo(EXPECTED_COLLECTION);
    }

    @Test
    public void shouldGetCollectionFromModel() {
        //given
        BDDMockito.given(mockDB.getCollection(EXPECTED_COLLECTION)).willReturn(mockDBCollection);

        //when
        DBCollection dbCollection = CollectionExtractor.getDBCollection(mockDB, ModelWithDBCollectionAnnotation.class);

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
            CollectionExtractor.getDBCollection(mockDB, ModelWithoutAnnotation.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidCollectionException.class)
                    .hasMessageContaining(DbCollection.class.getCanonicalName());
            return;
        }
        failExpectedException();
    }


}
