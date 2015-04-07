package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.internal.MightBeNegated;
import com.futureprocessing.documentjuggler.annotation.internal.QueryCommandProvider;
import com.futureprocessing.documentjuggler.annotation.query.*;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.*;
import com.mongodb.QueryBuilder;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;

public class QueryMapperNotCommandTest {

    private static QueryCommand getOperandCommand(QueryCommand command) {
        return FieldSupport.EXTRACTION.fieldValue("operandCommand", QueryCommand.class, command);
    }

    @MightBeNegated
    @Retention(RUNTIME)
    @Target({METHOD, ANNOTATION_TYPE})
    @QueryCommandProvider(OtherCommandSupportingNotProvider.class)
    public @interface OtherAnnotationSupportingNot {
    }

    public static class OtherCommandSupportingNot implements QueryCommand {
        @Override
        public void query(QueryBuilder builder, Object[] args) {
        }
    }

    public static class OtherCommandSupportingNotProvider implements CommandProvider<QueryCommand> {
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            return new OtherCommandSupportingNot();
        }
    }

    private interface ModelWithNot {
        @Not
        @DbField("forbidden")
        ModelWithNot withoutForbidden(String field);

        @Not
        @LessThan
        @DbField("lessThan")
        ModelWithNot lessThan(int value);

        @Not
        @LessThanEqual
        @DbField("lessThanEqual")
        ModelWithNot lessThanEqual(int value);


        @Not
        @OtherAnnotationSupportingNot
        @DbField("otherSupportedField")
        ModelWithNot withSomeOtherAnnotation(String otherValue);
    }

    @Test
    public void shouldReturnForbiddenQueryCommnadForBasicQueryCommand() throws NoSuchMethodException {
        // given
        Method method = ModelWithNot.class.getMethod("withoutForbidden", String.class);

        // when
        QueryMapper mapper = QueryMapper.map(ModelWithNot.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenQueryCommand.class);
    }

    @Test
    public void shouldReturnNotQueryCommandForMightBeNegated() throws NoSuchMethodException {
        // given
        Method method = ModelWithNot.class.getMethod("withSomeOtherAnnotation", String.class);

        // when
        QueryMapper mapper = QueryMapper.map(ModelWithNot.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(NotQueryCommand.class);

        QueryCommand innerCommand = getOperandCommand(command);
        assertThat(innerCommand).isInstanceOf(OtherCommandSupportingNot.class);
    }

    @Test
    public void shouldReturnNotQueryCommandForLessThanCommand() throws NoSuchMethodException {
        // given
        Method method = ModelWithNot.class.getMethod("lessThan", int.class);

        // when
        QueryMapper mapper = QueryMapper.map(ModelWithNot.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(NotQueryCommand.class);

        QueryCommand innerCommand = getOperandCommand(command);
        assertThat(innerCommand).isInstanceOf(LessThanQueryCommand.class);
    }

    @Test
    public void shouldReturnNotQueryCommandForLessThanEqualCommand() throws NoSuchMethodException {
        // given
        Method method = ModelWithNot.class.getMethod("lessThanEqual", int.class);

        // when
        QueryMapper mapper = QueryMapper.map(ModelWithNot.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(NotQueryCommand.class);

        QueryCommand innerCommand = getOperandCommand(command);
        assertThat(innerCommand).isInstanceOf(LessThanEqualsQueryCommand.class);
    }

}
