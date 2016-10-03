package tellh.com.gitclub.presentation.contract;

import java.util.List;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.recyclertreeview_lib.TreeNode;

public interface RepoSourceContract {

    interface View extends BaseView {
        void onGetBranchList(List<Branch> branches);

        void onGetSourceTree(List<TreeNode> treeNodes);

        void onGetReadMe(String html_url);
    }

    interface Presenter extends MvpPresenter<View> {

        void initSourceTree(String owner, String repo);

        void getSourceTree(String owner, String repo, Branch branch);

        void getReadMe(String owner, String repo);
    }
}