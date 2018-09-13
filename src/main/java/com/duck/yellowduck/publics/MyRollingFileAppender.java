package com.duck.yellowduck.publics;

import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;


public class MyRollingFileAppender extends RollingFileAppender {

    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        return this.getThreshold().equals(priority);
    }

}
