package tellh.com.gitclub.di.component;

import android.content.Context;

/**
 * Created by tlh on 2017/4/24 :)
 */
public class ComponentHolder {
    private static Context context;
    private static AppComponent appComponent;
    private static ExploreComponent exploreComponent;
    private static HomePageComponent homePageComponent;
    private static ListItemComponent listItemComponent;
    private static LoginComponent loginComponent;
    private static NewsComponent newsComponent;
    private static RepoPageComponent repoPageComponent;
    private static SearchComponent searchComponent;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ComponentHolder.context = context;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static void setAppComponent(AppComponent appComponent) {
        ComponentHolder.appComponent = appComponent;
    }

    public static ExploreComponent getExploreComponent() {
        return exploreComponent;
    }

    public static void setExploreComponent(ExploreComponent exploreComponent) {
        ComponentHolder.exploreComponent = exploreComponent;
    }

    public static HomePageComponent getHomePageComponent() {
        return homePageComponent;
    }

    public static void setHomePageComponent(HomePageComponent homePageComponent) {
        ComponentHolder.homePageComponent = homePageComponent;
    }

    public static ListItemComponent getListItemComponent() {
        return listItemComponent;
    }

    public static void setListItemComponent(ListItemComponent listItemComponent) {
        ComponentHolder.listItemComponent = listItemComponent;
    }

    public static LoginComponent getLoginComponent() {
        return loginComponent;
    }

    public static void setLoginComponent(LoginComponent loginComponent) {
        ComponentHolder.loginComponent = loginComponent;
    }

    public static NewsComponent getNewsComponent() {
        return newsComponent;
    }

    public static void setNewsComponent(NewsComponent newsComponent) {
        ComponentHolder.newsComponent = newsComponent;
    }

    public static RepoPageComponent getRepoPageComponent() {
        return repoPageComponent;
    }

    public static void setRepoPageComponent(RepoPageComponent repoPageComponent) {
        ComponentHolder.repoPageComponent = repoPageComponent;
    }

    public static SearchComponent getSearchComponent() {
        return searchComponent;
    }

    public static void setSearchComponent(SearchComponent searchComponent) {
        ComponentHolder.searchComponent = searchComponent;
    }
}
