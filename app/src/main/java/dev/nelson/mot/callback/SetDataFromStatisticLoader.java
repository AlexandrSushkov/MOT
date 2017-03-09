package dev.nelson.mot.callback;

import java.util.LinkedList;

public interface SetDataFromStatisticLoader {
    public void setDataFromStatisticLoader(LinkedList<String> categoriesNames, LinkedList<Long> sumPerCategory);
}
