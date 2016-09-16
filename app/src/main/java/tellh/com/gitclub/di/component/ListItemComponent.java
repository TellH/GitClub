package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.Fragment;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.activity.ListRepoActivity;
import tellh.com.gitclub.presentation.view.activity.ListUserActivity;

@Fragment
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface ListItemComponent {
    void inject(ListUserActivity activity);

    void inject(ListRepoActivity activity);
}