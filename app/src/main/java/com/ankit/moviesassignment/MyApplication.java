package com.ankit.moviesassignment;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

public class MyApplication extends Application {
    String language;
    Locale locale;
    public static String language_code;

    @Override
    public void onCreate() {
        super.onCreate();
        language = Locale.getDefault().getDisplayLanguage();
        if(language.equals("Deutsch")) {
            locale = new Locale("de");
            language_code = "de";
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }else {
            language_code = "en";
        }
    }
}
