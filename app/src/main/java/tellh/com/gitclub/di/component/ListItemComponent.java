package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListRepoActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListUserActivity;

@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface ListItemComponent {
    void inject(ListUserActivity activity);

    void inject(ListRepoActivity activity);
}