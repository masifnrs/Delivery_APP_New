package com.test.delivery_app_new.app;

public class StartApp extends AppController {
    public boolean parsing;



    @Override
    public void onCreate() {
        super.onCreate();
        parsing = false;

        // Initialize the singletons so their instances
        // are bound to the application process.
    }

}
