package com.futureprocessing.documentjuggler.update;

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
import com.futureprocessing.documentjuggler.update.command.UpdateArraysOperatorsCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateOperatorsCommand;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;

@RunWith(MockitoJUnitRunner.class)
public class UpdateArraysOperatorsTest {

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
        @DbField("names")
        List<String> getNames();

        @DbField("names")
        Model withNames(UpdateArrays<String> names);
    }

    @Test
    public void updateMapperShouldReturnUpdateArraysOperatorsCommand() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNames", UpdateArrays.class);
        UpdateMapper mapper = UpdateMapper.map(Model.class);

        //when
        UpdateCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(UpdateArraysOperatorsCommand.class);
    }

    @Test
    public void queryMapperMapperShouldReturnForbiddenQueryCommandForUpdateArraysParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNames", UpdateArrays.class);
        QueryMapper mapper = QueryMapper.map(Model.class);

        //when
        QueryCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenQueryCommand.class);
    }

    @Test
    public void insertMapperMapperShouldReturnForbiddenInsertCommandForUpdateArraysParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNames", UpdateArrays.class);
        InsertMapper mapper = InsertMapper.map(Model.class);

        //when
        InsertCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    @Test
    public void readMapperMapperShouldReturnForbiddenReadCommandForUpdateArraysParameter() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNames", UpdateArrays.class);
        ReadMapper mapper = ReadMapper.map(Model.class);

        //when
        ReadCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenReadCommand.class);
    }


}
