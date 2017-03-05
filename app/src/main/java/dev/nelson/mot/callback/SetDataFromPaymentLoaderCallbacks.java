package dev.nelson.mot.callback;


public interface SetDataFromPaymentLoaderCallbacks {
    public void fillPaymentInitialStateWithData(String title, int categoryId, String categoryName, double cost, String summary);
}
