package dev.nelson.mot.legacy.service.action;

import android.content.Context;

public class DataOperationFabric {

    public static final String INSERT_PAYMENT = "insert_payment";
    public static final String INSERT_CATEGORY = "insert_category";
    public static final String UPDATE_PAYMENT = "update_payment";
    public static final String UPDATE_CATEGORY = "update_category";
    public static final String DELETE_PAYMENT = "delete_payment";
    public static final String DELETE_CATEGORY = "delete_category";

    public static DataOperationAction getAction (Context context, String action){
        switch (action){
            case INSERT_PAYMENT:
                return new InsertPaymentAction(context);
            case INSERT_CATEGORY:
                return new InsertCategoryAction(context);
            case UPDATE_PAYMENT:
                return new UpdatePaymentAction(context);
            case UPDATE_CATEGORY:
                return new UpdateCategoryAction(context);
            case DELETE_PAYMENT:
                return new DeletePaymentAction(context);
            case DELETE_CATEGORY:
                return new DeleteCategoryAction(context);
            default:
                throw new RuntimeException(DataOperationFabric.class.getName() + " No such action: " + action);
        }
    }

}
