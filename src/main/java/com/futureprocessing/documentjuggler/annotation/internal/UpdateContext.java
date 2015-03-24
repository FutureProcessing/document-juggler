package com.futureprocessing.documentjuggler.annotation.internal;


import com.futureprocessing.documentjuggler.update.command.UpdateCommand;

public @interface UpdateContext {

    Class<? extends UpdateCommand> command();
}
