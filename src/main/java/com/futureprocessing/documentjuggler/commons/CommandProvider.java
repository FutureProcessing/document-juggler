package com.futureprocessing.documentjuggler.commons;

import java.lang.reflect.Method;

public interface CommandProvider<COMMAND> {

    COMMAND getCommand(Method method, Mapper<COMMAND> mapper);
}
