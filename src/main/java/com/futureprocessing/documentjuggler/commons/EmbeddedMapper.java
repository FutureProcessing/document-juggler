package com.futureprocessing.documentjuggler.commons;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EmbeddedMapper<COMMAND_TYPE> implements Mapper<COMMAND_TYPE> {

    private final String field;
    private final Mapper<COMMAND_TYPE> mapper;

    public EmbeddedMapper(String field, Mapper<COMMAND_TYPE> mapper) {
        this.mapper = mapper;
        if (mapper instanceof EmbeddedMapper) {
            EmbeddedMapper<COMMAND_TYPE> embeddedMapper = (EmbeddedMapper<COMMAND_TYPE>) mapper;
            this.field = embeddedMapper.field + "." + field;
        } else {
            this.field = field;

        }
    }

    @Override
    public Map<Method, COMMAND_TYPE> get() {
        return mapper.get();
    }

    @Override
    public void createMapping(Class<?> clazz) {
        this.mapper.createMapping(Optional.ofNullable(field), clazz, this);
    }

    @Override
    public void createMapping(Optional<String> field, Class<?> clazz, Mapper<COMMAND_TYPE> mapper) {
        this.mapper.createMapping(field, clazz, mapper);
    }

    @Override
    public Set<String> getSupportedFields() {
        return mapper.getSupportedFields();
    }

    public static <COMMAND_TYPE> EmbeddedMapper<COMMAND_TYPE> wrap(String field, Mapper<COMMAND_TYPE> mapper) {
        return new EmbeddedMapper<>(field, mapper);
    }
}
