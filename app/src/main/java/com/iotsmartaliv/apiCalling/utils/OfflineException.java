package com.iotsmartaliv.apiCalling.utils;

import java.io.IOException;

public class OfflineException extends IOException {

    public OfflineException(String no_internet) {

    }

    @Override
    public String getMessage() {
        return "No Internet";
    }
}
