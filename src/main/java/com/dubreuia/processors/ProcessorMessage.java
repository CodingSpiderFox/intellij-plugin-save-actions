package com.dubreuia.processors;

import static java.text.MessageFormat.format;

public class ProcessorMessage {

    private static final String FORMAT = "{0} ({1})";

    private ProcessorMessage() {
        // static class
    }

    public static String toStringBuilder(String name, boolean enabled) {
        return format(FORMAT, name, enabled);
    }

}
