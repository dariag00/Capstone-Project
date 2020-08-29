package com.kloso.capstoneproject.root;

import com.kloso.capstoneproject.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity target);
}
