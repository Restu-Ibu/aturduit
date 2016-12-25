package com.restuibu.aturduit;

import android.app.Application;

import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;

/**
 * Created by airgalon on 12/12/2016.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                new InspectorModulesProvider() {
                    @Override
                    public Iterable<ChromeDevtoolsDomain> get() {
                        return new Stetho.DefaultInspectorModulesBuilder(MyApplication.this).finish();
                    }
                }
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }


}
