package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.query.Not;
import com.futureprocessing.documentjuggler.assertions.JugglerAssertions;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.futureprocessing.documentjuggler.query.QueryMapper;
import com.futureprocessing.documentjuggler.query.command.ComparisonOperatorsCommand;
import com.futureprocessing.documentjuggler.query.command.ForbiddenQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComparisonOperatorsTest {

    @Mock
    private DBCollection collection;

    private interface Model {
        @DbField("number")
        int getNumber();

        @DbField("number")
        Model withNumber(int number);

        @DbField("number")
        Model withNumber(Comparison<Integer> comparison);

        @Not
        @DbField("number")
        Model withNumberNot(Comparison<Integer> comparison);
    }

    @Test
    public void queryMapperShouldReturnComparisonOperatorsCommand() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumber", Comparison.class);
        QueryMapper mapper = QueryMapper.map(Model.class);

        //when
        QueryCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ComparisonOperatorsCommand.class);
    }

    @Test
    public void queryMapperShouldReturnForbiddenCommandForNegatedComparison() throws NoSuchMethodException {
        //given
        Method method = Model.class.getMethod("withNumberNot", Comparison.class);
        QueryMapper mapper = QueryMapper.map(Model.class);

        //when
        QueryCommand command = mapper.get(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenQueryCommand.class);
    }

    @Test
    public void shouldSearchForNumberGreaterThan() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find(model -> model.withNumber(n -> n.greaterThan(20))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$gt", 20));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumberWithComplexQuery() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find(model -> model.withNumber(n -> n.greaterThan(5)
                .greaterThanEquals(10)
                .lessThan(15)
                .lessThanEquals(20))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$gt", 5)
                .append("$gte", 10)
                .append("$lt", 15)
                .append("$lte", 20));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersInList() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);
        final List<Integer> numbers = Collections.unmodifiableList(Arrays.asList(1, 2, 3, 4));

        //when

        repository.find(model -> model.withNumber(n -> n.in(numbers))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$in", numbers));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersInVarArg() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when

        repository.find(model -> model.withNumber(n -> n.in(1, 2, 3, 4))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$in", new int[]{1, 2, 3, 4}));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersInArray() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);
        Integer[] array = {1, 2, 3, 4};

        //when

        repository.find(model -> model.withNumber(n -> n.in(array))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$in", new int[]{1, 2, 3, 4}));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersNotInList() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);
        final List<Integer> numbers = Collections.unmodifiableList(Arrays.asList(1, 2, 3, 4));

        //when

        repository.find(model -> model.withNumber(n -> n.notIn(numbers))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$nin", numbers));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersNotInVarArg() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find(model -> model.withNumber(n -> n.notIn(1, 2, 3, 4))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$nin", new int[]{1, 2, 3, 4}));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersNotInArray() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);
        Integer[] array = {1, 2, 3, 4};

        //when
        repository.find(model -> model.withNumber(n -> n.notIn(array))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$nin", new int[]{1, 2, 3, 4}));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldSearchForNumbersNotEqual() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        //when
        repository.find(model -> model.withNumber(n -> n.notEquals(20))).first();

        //then
        DBObject expectedQuery = new BasicDBObject("number", new BasicDBObject("$ne", 20));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldThrowExceptionForComparisonMethodWihtNotAnnotation() {
        //given
        Repository<Model> repository = new Repository<>(collection, Model.class);

        try {
            //when
            repository.find(model -> model.withNumberNot(n -> n.greaterThan(20))).first();
        } catch (Exception e) {
            //then
            assertThat(e).isInstanceOf(ForbiddenOperationException.class);
            return;
        }
        JugglerAssertions.failExpectedException();
    }


}
