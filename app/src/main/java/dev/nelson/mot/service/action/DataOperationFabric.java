package dev.nelson.mot.service.action;

public class DataOperationFabric {

    public static final String INSERT_WASTE = "insert_waste";
    public static final String INSERT_CATEGORY = "insert_category";
    public static final String UPDATE_WASTE = "update_waste";
    public static final String UPDATE_CATEGORY = "update_category";
    public static final String DELETE_WASTE = "delete_waste";
    public static final String DELETE_CATEGORY = "delete_category";

    public static DataOperationAction getAction (String action){
        switch (action){
            case INSERT_WASTE:
                return new InsertWasteAction();
            case INSERT_CATEGORY:
                return new InsertCategoryAction();
            case UPDATE_WASTE:
                return new UpdateWasteAction();
            case UPDATE_CATEGORY:
                return new UpdateCategoryAction();
            case DELETE_WASTE:
                return new DeleteWasteAction();
            case DELETE_CATEGORY:
                return new DeleteCategoryAction();
            default:
                throw new RuntimeException("No action " + action);
        }
    }

}
