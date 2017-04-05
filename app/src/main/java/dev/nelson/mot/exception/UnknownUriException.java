package dev.nelson.mot.exception;

import android.database.SQLException;

public class UnknownUriException extends SQLException {
    public UnknownUriException() {
    }

    public UnknownUriException(String uri) {
        super("Unknown uri " + uri);
    }

    public UnknownUriException(String uri, Throwable cause) {
        super("Unknown uri " + uri, cause);
    }
}
