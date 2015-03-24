package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.mongodb.BasicDBObject;

import java.lang.reflect.Method;
import java.util.Set;

public class IdReadCommand extends AbstractReadCommand {

    public IdReadCommand(String field) {
        super(field);
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        return document.getObjectId(field).toHexString();
    }

    public static class Provider implements CommandProvider<ReadCommand>{
        @Override
        public ReadCommand getCommand(Method method, Mapper<ReadCommand> mapper) {
            return new IdReadCommand(FieldNameExtractor.getFieldName(method));
        }
    }
}
