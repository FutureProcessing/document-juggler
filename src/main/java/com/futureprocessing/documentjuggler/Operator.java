package com.futureprocessing.documentjuggler;

public class Operator<OPERATOR_TYPE, MAPPER_TYPE> {

    private final Class<OPERATOR_TYPE> rootClass;
    private final MAPPER_TYPE mapper;

    public Operator(Class<OPERATOR_TYPE> rootClass, MAPPER_TYPE mapper) {
        this.rootClass = rootClass;
        this.mapper = mapper;
    }

    public Class<OPERATOR_TYPE> getRootClass() {
        return rootClass;
    }

    public MAPPER_TYPE getMapper() {
        return mapper;
    }
}
