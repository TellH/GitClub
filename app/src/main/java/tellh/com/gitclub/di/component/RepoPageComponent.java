package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.activity.repo_page.RepoPageActivity;
import tellh.com.gitclub.presentation.view.activity.repo_page.RepoSourceActivity;

@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface RepoPageComponent {
    void inject(RepoPageActivity activity);

    void inject(RepoSourceActivity activity);
}