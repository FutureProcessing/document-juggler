package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public interface UpdateCommand {

    void update(UpdateBuilder updateBuilder, Object[] args);
}
