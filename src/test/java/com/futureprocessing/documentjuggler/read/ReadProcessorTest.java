package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ReadProcessorTest {

    @Mock
    private DBCollection dbCollectionMock;
    @Mock
    private DBCursor dbCursorMock;

    private interface SmallModel {

        @DbField("fieldA")
        String getFieldA();

        @DbField("fieldB")
        String getFieldB();
    }

    @Test
    public void shouldFetchOnlyFieldsSpecifiedInModelWhenFindingOne() {
        //given
        Repository<SmallModel> repository = new Repository<>(dbCollectionMock, SmallModel.class);

        //when
        repository.find().first();

        //then
        ArgumentCaptor<DBObject> projectionCaptor = ArgumentCaptor.forClass(DBObject.class);
        Mockito.verify(dbCollectionMock).findOne(any(), projectionCaptor.capture());

        BasicDBObject expectedProjection = new BasicDBObject("fieldA", 1).append("fieldB", 1);
        assertThat(projectionCaptor.getValue()).isEqualTo(expectedProjection);
    }

    @Test
    public void shouldFetchOnlyFieldsSpecifiedInModelWhenFindingAll() {
        //given
        Repository<SmallModel> repository = new Repository<>(dbCollectionMock, SmallModel.class);
        BDDMockito.given(dbCollectionMock.find(any(), any())).willReturn(dbCursorMock);

        //when
        repository.find().all();

        //then
        ArgumentCaptor<DBObject> projectionCaptor = ArgumentCaptor.forClass(DBObject.class);
        Mockito.verify(dbCollectionMock).find(any(), projectionCaptor.capture());

        BasicDBObject expectedProjection = new BasicDBObject("fieldA", 1).append("fieldB", 1);
        assertThat(projectionCaptor.getValue()).isEqualTo(expectedProjection);
    }
}
