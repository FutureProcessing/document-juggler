package com.futureprocessing.mongojuggler.exception;


import static java.lang.String.format;

public class InvalidNumberOfDocumentsAffected extends RuntimeException {

    private final int affected;
    private final int expected;

    public InvalidNumberOfDocumentsAffected(int affected, int expected) {
        super(format("Affected %d documents, expected %d", affected, expected));
        this.affected = affected;
        this.expected = expected;
    }

    public int getAffected() {
        return affected;
    }

    public int getExpected() {
        return expected;
    }
}
