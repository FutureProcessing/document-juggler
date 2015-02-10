package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.write.UpdateBuilder;

public interface UpdateCommand {

    void update(UpdateBuilder updateBuilder, Object[] args);
}
