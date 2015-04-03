package com.futureprocessing.documentjuggler.update;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.helper.Sets;
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
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;
import com.futureprocessing.documentjuggler.update.operators.UpdateArraysOperators;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.assertThat;
import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;

@RunWith(MockitoJUnitRunner.class)
public class UpdateArraysOperatorsTest {

    private static final String FIELD_NAME = "fieldName";
    private static final String VALUE_1 = "SomeValue";
    private static final String VALUE_2 = "AnotherValue";

    @Mock
    private DBCollection collection;
    @Mock
    private WriteResult writeResult;

    private UpdateBuilder updateBuilder;

    @Before
    public void before() {
        given(collection.update(any(), any(), anyBoolean(), anyBoolean())).willReturn(writeResult);
        given(writeResult.getN()).willReturn(1);
        updateBuilder = new RootUpdateBuilder();
    }

    private interface Model {
        @DbField(FIELD_NAME)
        List<String> getNames();

        @DbField(FIELD_NAME)
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


    @Test
    //FIXME duplicates AddToSetSingleUpdateCommandTest
    public void shouldAddToSetSingleValue() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);

        // when
        operators.addToSet(VALUE_1);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }

    @Test
    //FIXME duplicates AddToSetManyUpdateCommandTest
    public void shouldAddToSetVarArg() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);

        // when
        operators.addToSet(VALUE_1, VALUE_2);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }

    @Test
    //FIXME duplicates AddToSetArrayUpdateCommandTest
    public void shouldAddToSetArray() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);
        String[] arrayOfStrings = {VALUE_1, VALUE_2};

        // when
        operators.addToSet(arrayOfStrings);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }

    @Test
    //FIXME duplicates AddToSetCollectionUpdateCommandTest
    public void shouldAddToSetList() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);
        List<String> listOfStrings = asList(VALUE_1, VALUE_2);

        // when
        operators.addToSet(listOfStrings);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }

    @Test
    //FIXME duplicates AddToSetCollectionUpdateCommandTest
    public void shouldAddToSetSet() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);
        Set<String> setOfStrings = asSet(VALUE_1, VALUE_2);

        // when
        operators.addToSet(setOfStrings);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }

    @Test
    //FIXME duplicates AddToSetCollectionUpdateCommandTest
    public void shouldAddToSetCollection() {
        // given
        UpdateArraysOperators<String> operators = new UpdateArraysOperators<>(FIELD_NAME, updateBuilder);
        Collection<String> collectionOfStrings = asList(VALUE_1, VALUE_2);

        // when
        operators.addToSet(collectionOfStrings);

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD_NAME, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(updateBuilder).hasDocumentEqualTo(expected);
    }
}
