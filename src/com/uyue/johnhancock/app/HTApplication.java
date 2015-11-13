package com.uyue.johnhancock.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class HTApplication extends Application {

	private static final String D_TAG = HTApplication.class.getSimpleName();
	public static Context context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(D_TAG, "onCreate");
		context = HTApplication.this;
	}
}
