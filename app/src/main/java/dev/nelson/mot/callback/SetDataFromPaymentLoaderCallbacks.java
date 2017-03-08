package dev.nelson.mot.callback;


public interface SetDataFromPaymentLoaderCallbacks {
    public void fillPaymentInitialStateWithData(String title, int categoryId, String categoryName, long cost, String summary);
}
