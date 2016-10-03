package com.promovid.android;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.promovid.android.Net.DaggerNetComponent;
import com.promovid.android.Net.NetComponent;
import com.promovid.android.Net.NetModule;

/**
 * Created by bvs on 2/10/16.
 */
public class App extends Application {

    private static App _instance;
    private NetComponent mNetComponent;

    public static App getInstance() {
        return _instance;
    }
    public static NetComponent getNetComponent() {
        return getInstance().mNetComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        Fresco.initialize(this);
        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .build();
        mNetComponent.inject(this);
    }
}
