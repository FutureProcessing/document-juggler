package com.futureprocessing.documentjuggler.model;

import com.futureprocessing.documentjuggler.annotation.Id;

public interface DefaultModel<MODEL extends DefaultModel> {

    @Id
    MODEL withId(String id);

    @Id
    String getId();
}
