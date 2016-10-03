package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.fragment.explore.ExploreFragment;

@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface ExploreComponent {
    void inject(ExploreFragment fragment);
    ExploreContract.Presenter PRESENTER();
}
