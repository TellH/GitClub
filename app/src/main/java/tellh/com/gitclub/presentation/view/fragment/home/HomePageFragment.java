package tellh.com.gitclub.presentation.view.fragment.home;

import android.os.Bundle;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.LazyFragment;
import tellh.com.gitclub.di.component.DaggerHomePageComponent;
import tellh.com.gitclub.presentation.contract.HomePageContract;

public class HomePageFragment extends LazyFragment implements HomePageContract.View {
    @Inject
    HomePageContract.Presenter presenter;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    private void initDagger() {
        DaggerHomePageComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_personal_page;
    }

    @Override
    public void initView() {
        initDagger();
    }
}
