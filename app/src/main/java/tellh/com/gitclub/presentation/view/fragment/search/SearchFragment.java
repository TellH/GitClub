package tellh.com.gitclub.presentation.view.fragment.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.LazyFragment;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.di.component.DaggerSearchComponent;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.contract.SearchContract;
import tellh.com.gitclub.presentation.contract.bus.RxBus;
import tellh.com.gitclub.presentation.contract.bus.event.OnBackPressEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnClickOutsideToHideEvent;
import tellh.com.gitclub.presentation.view.adapter.CommonViewPagerAdapter;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;
import tellh.com.gitclub.presentation.widget.OnPageChangeListenerAdapter;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.REFRESH;
import static tellh.com.gitclub.presentation.contract.SearchContract.ListType;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.Presenter;
import static tellh.com.gitclub.presentation.contract.SearchContract.REPO;
import static tellh.com.gitclub.presentation.contract.SearchContract.USER;

public class SearchFragment extends LazyFragment
        implements SearchContract.View, OnListFragmentInteractListener {
    @Inject
    Presenter presenter;

    private SearchUserFragment userListListener;
    private SearchRepoFragment reposListListener;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabSort;
    private FloatingActionButton fabLang;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void initView() {
        initDagger();
        //find view
        ViewPager pager = (ViewPager) mRootView.findViewById(R.id.pager_search);
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) mRootView.findViewById(R.id.tab_layout);
        fabLang = (FloatingActionButton) mRootView.findViewById(R.id.fab_language);
        fabSort = (FloatingActionButton) mRootView.findViewById(R.id.fab_sort);
        fabMenu = (FloatingActionsMenu) mRootView.findViewById(R.id.fab);

        //init viewpager
        CommonViewPagerAdapter viewPagerAdapter = new CommonViewPagerAdapter(getFragmentManager());
        reposListListener = SearchRepoFragment.newInstance();
        userListListener = SearchUserFragment.newInstance();
        reposListListener.setListFragmentInteractListener(this);
        userListListener.setListFragmentInteractListener(this);
        viewPagerAdapter.addFragment(Utils.getString(R.string.title_search_repo), reposListListener);
        viewPagerAdapter.addFragment(Utils.getString(R.string.title_search_user), userListListener);
        pager.setAdapter(viewPagerAdapter);
        pager.setOffscreenPageLimit(2);
        pager.addOnPageChangeListener(new OnPageChangeListenerAdapter() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    presenter.setCurrentSearchEntity(presenter.getRepoSearchEntity());
                else
                    presenter.setCurrentSearchEntity(presenter.getUserSearchEntity());
            }
        });

        //init toolbar
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //init tab layout
        tabLayout.setupWithViewPager(pager);

        //init fab
        initFab();
    }

    private void initFab() {
        fabLang.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.getDialogLang().show();
                fabMenu.collapse();
            }
        });
        fabSort.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                SearchContract.SearchEntity searchEntity = presenter.getCurrentSearchEntity();
                if (searchEntity.type == REPO)
                    presenter.getDialogSortRepo().show();
                else presenter.getDialogSortUser().show();
                fabMenu.collapse();
            }
        });
        addSubscription(RxBus.getDefault().toObservable(OnBackPressEvent.class).subscribe(new Action1<OnBackPressEvent>() {
            @Override
            public void call(OnBackPressEvent event) {
                if (fabMenu == null || !fabMenu.isExpanded())
                    return;
                fabMenu.collapse();
                event.hasConsume = true;
            }
        }));
        addSubscription(RxBus.getDefault().toObservable(OnClickOutsideToHideEvent.class).subscribe(new Action1<OnClickOutsideToHideEvent>() {
            @Override
            public void call(OnClickOutsideToHideEvent event) {
                if (fabMenu == null || !fabMenu.isExpanded())
                    return;
                fabMenu.collapse();
                event.consume = true;
            }
        }));
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        presenter.setCurrentSearchEntity(presenter.getRepoSearchEntity());
        //to get the most popular repository and user.
        presenter.initialSearch();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_search;
    }

    private void initDagger() {
        DaggerSearchComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
        userListListener = null;
        reposListListener = null;
    }

    @Override
    public void onGetRepos(int total_count, List<RepositoryInfo> items, @UpdateType int updateType) {
        reposListListener.onGetRepos(total_count, items, updateType);
        if (updateType == REFRESH)
            reposListListener.hideLoading();
    }

    @Override
    public void onGetUsers(int total_count, List<UserEntity> items, @UpdateType int updateType) {
        userListListener.onGetUser(total_count, items, updateType);
        if (updateType == REFRESH)
            userListListener.hideLoading();
    }

    @Override
    public void showListRefreshLoading(@ListType int listType) {
        if (listType == REPO) {
            reposListListener.showLoading();
        } else
            userListListener.showLoading();
    }

    @Override
    public void showOnError(String msg, @ListType int type, @UpdateType int updateType) {
        showOnError(msg);
        //hide loading
        switch (type) {
            case USER:
                userListListener.hideLoading();
                showErrorView(msg, userListListener, updateType);
                break;
            case REPO:
                reposListListener.hideLoading();
                showErrorView(msg, reposListListener, updateType);
                break;
        }
    }

    private void showErrorView(String msg, final ListFragment listFragment, @UpdateType int updateType) {
        if (updateType == REFRESH && !msg.equals(Utils.getString(R.string.reqest_flying))) {
            listFragment.showErrorView();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        initSearchView(menu);
    }

    private void initSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(Utils.getString(R.string.title_search));
        searchView.setSubmitButtonEnabled(true);
        //Sorry, I have to use a little reflect :)
        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView iv = (ImageView) field.get(searchView);
            iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_send_white));
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.getCurrentSearchEntity().keyWord = query;
                presenter.searchCurrent(true, 1);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onFetchPage(@ListType int type, int page) {
        switch (type) {
            case USER:
                presenter.searchUser(page);
                break;
            case REPO:
                presenter.searchRepo(page);
                break;
        }
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    @Override //disable the default loading dialog from super class.
    public void showOnLoading() {
    }
}