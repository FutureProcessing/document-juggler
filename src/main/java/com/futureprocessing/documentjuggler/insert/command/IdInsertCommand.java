package com.futureprocessing.documentjuggler.insert.command;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class IdInsertCommand extends AbstractInsertCommand {
    public IdInsertCommand(String field) {
        super(field);
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        document.append(field, new ObjectId((String) args[0]));
    }

    public static class Provider implements CommandProvider<InsertCommand> {

        @Override
        public InsertCommand getCommand(Method method, Mapper<InsertCommand> mapper) {
            if (!method.getParameterTypes()[0].equals(String.class)) {
                return new ForbiddenInsertCommand(method);
            }

            return new IdInsertCommand(getFieldName(method));
        }
    }
}
