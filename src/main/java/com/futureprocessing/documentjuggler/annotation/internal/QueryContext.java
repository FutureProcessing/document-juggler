package com.futureprocessing.documentjuggler.annotation.internal;


import com.futureprocessing.documentjuggler.query.command.QueryCommand;

public @interface QueryContext {

    Class<? extends QueryCommand> command();
}
