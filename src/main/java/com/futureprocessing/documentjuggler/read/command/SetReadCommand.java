package com.futureprocessing.documentjuggler.read.command;


import com.mongodb.BasicDBObject;

import java.util.Collection;
import java.util.HashSet;

public class SetReadCommand extends AbstractReadCommand {

    public SetReadCommand(String field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object readValue(BasicDBObject document) {
        return new HashSet((Collection) document.get(field));
    }


}
