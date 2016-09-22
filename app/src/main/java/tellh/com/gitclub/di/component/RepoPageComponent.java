package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.Fragment;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.activity.repo_page.RepoPageActivity;

@Fragment
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface RepoPageComponent {
    void inject(RepoPageActivity activity);
}