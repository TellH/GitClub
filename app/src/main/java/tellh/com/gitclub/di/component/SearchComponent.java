package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.presentation.contract.SearchContract;
import tellh.com.gitclub.presentation.view.fragment.search.SearchFragment;
import tellh.com.gitclub.di.Fragment;
import tellh.com.gitclub.di.module.PresenterModule;
@Fragment
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface SearchComponent {
    void inject(SearchFragment searchFragment);
    SearchContract.Presenter PRESENTER();
}
