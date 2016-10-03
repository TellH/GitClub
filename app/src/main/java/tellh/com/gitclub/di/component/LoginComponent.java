package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;

@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface LoginComponent {
    void inject(LoginFragment loginFragment);
}