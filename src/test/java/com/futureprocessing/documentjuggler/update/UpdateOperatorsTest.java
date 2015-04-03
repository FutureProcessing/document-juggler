package com.futureprocessing.documentjuggler.update;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.insert.InsertMapper;
import com.futureprocessing.documentjuggler.insert.command.ForbiddenInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;
import com.futureprocessing.documentjuggler.query.QueryMapper;
import com.futureprocessing.documentjuggler.query.command.ForbiddenQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.read.ReadMapper;
import com.futureprocessing.documentjuggler.read.command.ForbiddenReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateOperatorsCommand;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateOperatorsTest {

    @Mock
    private DBCollection collection;
    @Mock
    private WriteResult writeResult;

    @Before
    public void before() {
        given(collection.update(any(), any(), anyBoolean(), anyBoolean())).willReturn(writeResult);
        given(writeResult.getN()).willReturn(1);
    }


    private interface Model {
        @DbField("number")
        int getNumber();

        @DbField("number")
        Model withNumber(int number);

        @DbField("number")
        Model withNumber(Update<Integer> update);
    }

    @Test
    public void updateMapperShouldReturnUpdateOperatorsCommand() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumber", Update.class);
        UpdateMapper mapper = UpdateMapper.map(Model.class);

        //when
        UpdateCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(UpdateOperatorsCommand.class);
    }

    @Test
    public void queryMapperMapperShouldReturnForbiddenQueryCommandForUpdateParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumber", Update.class);
        QueryMapper mapper = QueryMapper.map(Model.class);

        //when
        QueryCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenQueryCommand.class);
    }

    @Test
    public void insertMapperMapperShouldReturnForbiddenInsertCommandForUpdateParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumber", Update.class);
        InsertMapper mapper = InsertMapper.map(Model.class);

        //when
        InsertCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    @Test
    public void readMapperMapperShouldReturnForbiddenReadCommandForUpdateParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumber", Update.class);
        ReadMapper mapper = ReadMapper.map(Model.class);

        //when
        ReadCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenReadCommand.class);
    }

    @Test
    public void shouldIncrement() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find().update(model -> model.withNumber(n -> n.increment(20)));

        //then
        DBObject expectedUpdate = new BasicDBObject("$inc", new BasicDBObject("number", 20));
        verify(collection).update(any(), eq(expectedUpdate), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldSet() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find().update(model -> model.withNumber(n -> n.set(20)));

        //then
        DBObject expectedUpdate = new BasicDBObject("$set", new BasicDBObject("number", 20));
        verify(collection).update(any(), eq(expectedUpdate), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldSetWithExactValue() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find().update(model -> model.withNumber(20));

        //then
        DBObject expectedUpdate = new BasicDBObject("$set", new BasicDBObject("number", 20));
        verify(collection).update(any(), eq(expectedUpdate), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldUnset() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find().update(model -> model.withNumber(n -> n.unset()));

        //then
        DBObject expectedUpdate = new BasicDBObject("$unset", new BasicDBObject("number", null));
        verify(collection).update(any(), eq(expectedUpdate), anyBoolean(), anyBoolean());
    }

}
