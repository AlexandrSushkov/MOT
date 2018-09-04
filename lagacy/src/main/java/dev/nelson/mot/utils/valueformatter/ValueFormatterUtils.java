package dev.nelson.mot.utils.valueformatter;

class ValueFormatterUtils {

    private static final int Y_AXIS = 1;
    private static final int SIDE_Y_AXIS = 2;

    static String formatYAxisValue(float value){
        return formatValue(value, Y_AXIS);
    }

    static String formatSideYAxisValue(float value){
        return formatValue(value, SIDE_Y_AXIS);
    }

    private static String formatValue(float value, int flag){
        String v = String.valueOf((int) value);
        String result = "";
        if(value == 0f ){
            result = "0";
        }else if(value < 100000f){
            switch (flag){
                case SIDE_Y_AXIS:
                    result = v.substring(0, v.length() - 1);
                    break;
                case Y_AXIS:
                    result =  new StringBuffer(v).insert(v.length()-1, ".").toString();
            }
        }else if(value >= 100000f && value < 100000000f){
            v = v.substring(0, v.length() - 4);
            if (v.substring(v.length() - 1, v.length()).equals("0")){
                result = v.substring(0, v.length() - 1) + "K";
            } else {
                result = new StringBuffer(v).insert(v.length()-1, ".").toString() + " K";
            }
        }else {
            v = v.substring(0, v.length() - 7);
            if (v.substring(v.length() - 1, v.length()).equals("0")){
                result = v.substring(0, v.length() - 1) + "M";
            } else {
                result = new StringBuffer(v).insert(v.length()-1, ".").toString() + " M";
            }
        }
        return  result;
    }
}
