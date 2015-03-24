package com.futureprocessing.documentjuggler.annotation.internal;


import com.futureprocessing.documentjuggler.insert.command.InsertCommand;

public @interface InsertContext {

    Class<? extends InsertCommand> command();
}
