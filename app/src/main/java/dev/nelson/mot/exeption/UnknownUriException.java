package dev.nelson.mot.exeption;

import android.database.SQLException;

/**
 * Created by Nelson on 2/8/17.
 */

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
