package com.promovid.android.Net;

import com.promovid.android.App;
import com.promovid.android.Activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bvs on 3/10/16.
 */
@Singleton
@Component(modules = {NetModule.class})
public interface NetComponent {

    void inject(MainActivity a);

    void inject(App app);
}
