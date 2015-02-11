package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

import java.util.Collection;

public class AddToSetCollectionUpdateCommand extends AbstractUpdateCommand{

    public AddToSetCollectionUpdateCommand(String field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Collection collection = (Collection) args[0];
        collection.forEach(el -> updateBuilder.addToSet(field, el));
    }
}
