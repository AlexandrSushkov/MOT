package dev.nelson.mot.callback;

public interface DatabaseChangesCallback {
    public void lastInsertedRow(int lastInsertedRow);

    public void updateDataFromDB();
}
