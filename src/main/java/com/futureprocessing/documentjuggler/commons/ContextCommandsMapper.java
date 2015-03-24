package com.futureprocessing.documentjuggler.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;

public class ContextCommandsMapper {


    public static <COMMAND_TYPE> COMMAND_TYPE getCommand(Method method, Mapper<COMMAND_TYPE> mapper, Class contextClass) {

        Annotation readContext = from(method).read(contextClass);
        if (readContext != null) {

            try {
                Method commandProviderMethod = contextClass.getMethod("value");
                Class<? extends CommandProvider<COMMAND_TYPE>> commandClass = (Class<? extends CommandProvider<COMMAND_TYPE>>) commandProviderMethod.invoke(readContext);

                CommandProvider<COMMAND_TYPE> commandProvider = commandClass.newInstance();
                return commandProvider.getCommand(method, mapper);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
