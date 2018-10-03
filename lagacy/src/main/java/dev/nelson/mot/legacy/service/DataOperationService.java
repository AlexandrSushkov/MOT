package dev.nelson.mot.legacy.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import dev.nelson.mot.legacy.service.action.DataOperationAction;
import dev.nelson.mot.legacy.service.action.DataOperationFabric;

public class DataOperationService extends IntentService {
    public static final String SERVICE_NAME = DataOperationService.class.getName();
    private static final String TAG = SERVICE_NAME;

    public DataOperationService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null){
            return;
        }
        Log.d(TAG, "onHandleIntent: " + intent.getAction());
        DataOperationAction action = DataOperationFabric.getAction(getBaseContext(), intent.getAction());
        action.perform(intent.getExtras());
    }
}
