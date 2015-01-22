package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbField;

public interface AbstractQuery<T> {

    @DbField("_id")
    T withId(String id);

}
