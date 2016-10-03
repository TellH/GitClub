package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.activity.user_personal_page.PersonalHomePageActivity;
import tellh.com.gitclub.presentation.view.fragment.home.HomePageFragment;

@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface HomePageComponent {
    void inject(HomePageFragment fragment);

    void inject(PersonalHomePageActivity activity);
}
