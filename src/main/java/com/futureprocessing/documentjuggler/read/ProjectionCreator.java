package com.futureprocessing.documentjuggler.read;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import java.util.Set;

public class ProjectionCreator {

    private final ReadMapper mapper;

    private ProjectionCreator(ReadMapper mapper) {
        this.mapper = mapper;
    }

    public static ProjectionCreator create(ReadMapper mapper) {
        return new ProjectionCreator(mapper);
    }

    public DBObject getProjection(Set<String> fields) {
        if (fields.isEmpty()) {
            fields = mapper.getSupportedFields();
        }
        BasicDBObjectBuilder start = BasicDBObjectBuilder.start();
        fields.forEach(field -> start.append(field, 1));
        return start.get();
    }
}
