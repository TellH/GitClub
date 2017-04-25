package tellh.com.gitclub.presentation.presenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.model.net.DataSource.ArsenalDataSource;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.GankDataSource;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.presentation.contract.ExploreContract;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tlh on 2017/4/25 :)
 */
public class ExplorePresenterTest {
    @Mock
    ExploreContract.View view;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    ExplorePresenter presenter;
    @Mock
    ExploreDataSource mExploreDataSource;
    @Mock
    GankDataSource mGankDataSource;
    @Mock
    ArsenalDataSource mArsenalDataSource;
    @Mock
    RepositoryDataSource mRepositoryDataSource;

    @Before
    public void setUp() throws Exception {
        presenter = new ExplorePresenter(mExploreDataSource, mRepositoryDataSource, mGankDataSource, mArsenalDataSource);
        presenter.setView(view);
    }

    @Test
    public void listTrending() throws Exception {
        List<Trending> trendings = new ArrayList<>();
        trendings.add(new Trending());
        trendings.add(new Trending());
        trendings.add(new Trending());
        ArgumentCaptor<Map<String, String>> captor = new ArgumentCaptor<>();
        // Success to return data
        when(mExploreDataSource.listTrending(captor.capture())).thenReturn(Observable.just(trendings));
        presenter.listTrending();
        assertFalse(captor.getValue().keySet().contains("language"));
        assertTrue(captor.getValue().keySet().contains("since"));
        verify(view).onGetTrending(trendings);
        verify(view).showOnSuccess();
    }
}