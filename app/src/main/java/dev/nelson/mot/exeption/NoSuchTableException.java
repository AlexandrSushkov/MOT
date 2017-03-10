package dev.nelson.mot.exeption;

import android.database.SQLException;

public class NoSuchTableException extends SQLException {
    public NoSuchTableException() {
    }

    public NoSuchTableException(String tableName) {
        super("No such table " + tableName);
    }

    public NoSuchTableException(String tableName, Throwable cause) {
        super("No such table " + tableName, cause);
    }
}
