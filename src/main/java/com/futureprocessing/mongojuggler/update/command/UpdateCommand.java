package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public interface UpdateCommand {

    void update(UpdateBuilder updateBuilder, Object[] args);
}
