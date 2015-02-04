package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

import java.util.Collection;
import java.util.HashSet;

public class SetReadCommand implements ReadCommand {

    private final String field;

    public SetReadCommand(String field) {
        this.field = field;
    }

    @Override
    public Object read(BasicDBObject document) {
        return new HashSet((Collection) document.get(field));
    }
}
