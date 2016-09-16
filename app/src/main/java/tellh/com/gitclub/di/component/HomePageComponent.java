package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.Fragment;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.fragment.home.HomePageFragment;

@Fragment
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface HomePageComponent {
    void inject(HomePageFragment fragment);
}
