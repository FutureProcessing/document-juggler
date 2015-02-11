package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

import java.util.Collection;

public abstract class AbstractCollectionUpdateCommand extends AbstractUpdateCommand {

    public AbstractCollectionUpdateCommand(String field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Collection collection = (Collection) args[0];
        collection.forEach(el -> update(updateBuilder, el));
    }

    protected abstract void update(UpdateBuilder updateBuilder, Object value);
}
