package tellh.com.gitclub.di.component;

import dagger.Component;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.di.module.PresenterModule;
import tellh.com.gitclub.presentation.view.fragment.news.NewsFragment;
@DiView
@Component(modules = {PresenterModule.class}, dependencies = {AppComponent.class})
public interface NewsComponent {
    void inject(NewsFragment activity);
}
