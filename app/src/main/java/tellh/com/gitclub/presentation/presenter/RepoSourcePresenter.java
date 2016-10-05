package tellh.com.gitclub.presentation.presenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.ReadMe;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.presentation.contract.RepoSourceContract;
import tellh.com.recyclertreeview_lib.TreeNode;

/**
 * Created by tlh on 2016/10/3 :)
 */

public class RepoSourcePresenter extends BasePresenter<RepoSourceContract.View> implements RepoSourceContract.Presenter {
    private final RepositoryDataSource mRepositoryDataSource;

    public RepoSourcePresenter(RepositoryDataSource repositoryDataSource) {
        this.mRepositoryDataSource = repositoryDataSource;
    }

    @Override
    public void initSourceTree(final String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.listBranches(owner, repo)
                        .flatMap(new Func1<List<Branch>, Observable<List<TreeNode>>>() {
                            @Override
                            public Observable<List<TreeNode>> call(List<Branch> branches) {
                                if (branches == null || branches.size() == 0)
                                    return Observable.error(new Throwable("No Branches."));
                                getView().onGetBranchList(branches);
                                return mRepositoryDataSource.getContent(owner, repo, branches.get(0));
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<TreeNode>>() {
                            @Override
                            public void onNext(List<TreeNode> treeNodes) {
                                getView().showOnSuccess();
                                getView().onGetSourceTree(treeNodes);
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_get_source_tree) + errorStr);
                            }
                        })
        );
    }

    @Override
    public void getSourceTree(String owner, String repo, Branch branch) {
        addSubscription(
                mRepositoryDataSource.getContent(owner, repo, branch)
                        .subscribe(new DefaultSubscriber<List<TreeNode>>() {
                            @Override
                            public void onNext(List<TreeNode> treeNodes) {
                                getView().showOnSuccess();
                                getView().onGetSourceTree(treeNodes);
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_get_source_tree) + errorStr);
                            }
                        })
        );
    }

    @Override
    public void getReadMe(String owner, String repo) {
        addSubscription(
                mRepositoryDataSource.getReadMe(owner, repo)
                        .compose(RxJavaUtils.<ReadMe>setLoadingListener(getView()))
                        .subscribe(new DefaultSubscriber<ReadMe>() {
                            @Override
                            public void onNext(ReadMe readMe) {
                                getView().showOnSuccess();
                                getView().onGetReadMe(readMe.getHtml_url());
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError("Fail to get ReadMe File");
                            }
                        })
        );
    }


}
