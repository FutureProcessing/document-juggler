package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.annotation.internal.MightBeNegated;
import com.futureprocessing.documentjuggler.annotation.query.Not;
import com.futureprocessing.documentjuggler.commons.AbstractMapper;
import com.futureprocessing.documentjuggler.query.command.ComparisonOperatorsCommand;
import com.futureprocessing.documentjuggler.query.command.ForbiddenQueryCommand;
import com.futureprocessing.documentjuggler.query.command.NotQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.command.providers.DefaultQueryCommandProvider;
import com.futureprocessing.documentjuggler.query.operators.Comparison;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.futureprocessing.documentjuggler.Context.QUERY;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class QueryMapper extends AbstractMapper<QueryCommand> {

    public static <MODEL> QueryMapper map(Class<MODEL> modelClass) {
        QueryMapper mapper = new QueryMapper();
        mapper.createMapping(modelClass);
        return mapper;
    }

    private QueryMapper() {
        super(QUERY, new DefaultQueryCommandProvider(), new ForbiddenQueryCommand.Provider());
    }

    @Override
    protected boolean isForbidden(Method method) {
        return !hasCorrectReturnType(method) || !hasCorrectParameters(method);
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> model = method.getDeclaringClass();

        return model.isAssignableFrom(returnType) || returnType.equals(Void.TYPE);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 1;
    }

    @Override
    protected QueryCommand postProcessCommand(QueryCommand command, Method method) {
        if (from(method).isPresent(Not.class)) {
            if (from(method).isPresent(MightBeNegated.class)) {
                return new NotQueryCommand(getFieldName(method), command);
            }
            return getForbidden(method);
        }
        return super.postProcessCommand(command, method);
    }

    @Override
    protected Optional<QueryCommand> getCommandBeforeAnnotationsReading(Method method) {
        Class<?> parameterType = method.getParameterTypes()[0];

        if (Comparison.class.isAssignableFrom(parameterType)) {
            return Optional.of(new ComparisonOperatorsCommand(getFieldName(method)));
        }

        return Optional.empty();
    }
}
