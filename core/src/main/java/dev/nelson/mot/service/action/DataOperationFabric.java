package dev.nelson.mot.service.action;

public class DataOperationFabric {

    public static final String INSERT_PAYMENT = "insert_payment";
    public static final String INSERT_CATEGORY = "insert_category";
    public static final String UPDATE_PAYMENT = "update_payment";
    public static final String UPDATE_CATEGORY = "update_category";
    public static final String DELETE_PAYMENT = "delete_payment";
    public static final String DELETE_CATEGORY = "delete_category";

    public static DataOperationAction getAction (String action){
        switch (action){
            case INSERT_PAYMENT:
                return new InsertPaymentAction();
            case INSERT_CATEGORY:
                return new InsertCategoryAction();
            case UPDATE_PAYMENT:
                return new UpdatePaymentAction();
            case UPDATE_CATEGORY:
                return new UpdateCategoryAction();
            case DELETE_PAYMENT:
                return new DeletePaymentAction();
            case DELETE_CATEGORY:
                return new DeleteCategoryAction();
            default:
                throw new RuntimeException(DataOperationFabric.class.getName() + " No such action: " + action);
        }
    }

}
