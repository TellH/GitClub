package tellh.com.gitclub.model.net.DataSource;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.Dir;
import tellh.com.gitclub.model.entity.File;
import tellh.com.gitclub.model.entity.GitTree;
import tellh.com.gitclub.model.entity.ReadMe;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.service.RepositoryService;
import tellh.com.recyclertreeview_lib.LayoutItemType;
import tellh.com.recyclertreeview_lib.TreeNode;

import static tellh.com.gitclub.common.config.Constant.PER_PAGE;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class RepositoryDataSource {
    private RepositoryService repositoryApi;

    public RepositoryDataSource(RepositoryService repositoryApi) {
        this.repositoryApi = repositoryApi;
    }

    public Observable<RepositoryInfo> getRepoInfo(String owner, String repo) {
        return repositoryApi.getRepoInfo(owner, repo)
                .compose(RxJavaUtils.<RepositoryInfo>applySchedulers());
    }

    public Observable<List<RepositoryInfo>> listForks(String owner, String repo, int page) {
        return repositoryApi.listForks(owner, repo, page)
                .compose(RxJavaUtils.<List<RepositoryInfo>>applySchedulers());
    }

    public Observable<List<UserEntity>> listWatchers(String owner, String repo, int page) {
        return repositoryApi.listWatchers(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<UserEntity>> listStargazers(String owner, String repo, int page) {
        return repositoryApi.listStargazers(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<UserEntity>> listContributors(String owner, String repo, int page) {
        return repositoryApi.listContributors(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<RepositoryInfo> toFork(String owner, String repo) {
        return repositoryApi.toFork(owner, repo)
                .compose(RxJavaUtils.<RepositoryInfo>applySchedulers());
    }

    public Observable<Boolean> toStar(String owner, String repo) {
        return repositoryApi.toStar(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> toWatch(String owner, String repo) {
        return repositoryApi.toWatch(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> unStar(String owner, String repo) {
        return repositoryApi.unStar(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> unWatch(String owner, String repo) {
        return repositoryApi.unWatch(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> checkStarred(String owner, String repo) {
        return repositoryApi.checkStarred(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> checkWatching(String owner, String repo) {
        return repositoryApi.checkWatching(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> delete(String owner, String repo) {
        return repositoryApi.delete(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<List<Branch>> listBranches(String owner, String repo) {
        return repositoryApi.listBranches(owner, repo)
                .compose(RxJavaUtils.<List<Branch>>applySchedulers());
    }

    public Observable<List<TreeNode>> getContent(final String owner, final String repo, final Branch branch) {
        return repositoryApi.getContent(owner, repo, branch.getCommit().getSha())
                .map(new Func1<GitTree, List<TreeNode>>() {
                    @Override
                    public List<TreeNode> call(GitTree gitTree) {
                        List<GitTree.TreeEntity> treeEntities = gitTree.getTree();
                        List<TreeNode> treeViewNodes = new ArrayList<>(treeEntities.size());
                        for (int index = 0; index < treeEntities.size(); ) {
                            GitTree.TreeEntity treeEntity = treeEntities.get(index++);
                            switch (treeEntity.getNodeType()) {
                                case GitTree.DIR:
                                    TreeNode<Dir> dirTreeNode = new TreeNode<>(new Dir(treeEntity.getPath(), treeEntity.getPath()));
                                    index = findChildNode(treeEntities, index, dirTreeNode, treeEntity.getPath(), owner, repo, branch.getName());
                                    treeViewNodes.add(dirTreeNode);
                                    break;
                                case GitTree.FILE:
                                    treeViewNodes.add(new TreeNode<>(new File(treeEntity.getPath(),
                                            treeEntity.getHtml_url(owner, repo, branch.getName()))));
                                    break;
                            }
                        }
                        sortNodeList(treeViewNodes);
                        return treeViewNodes;
                    }
                }).compose(RxJavaUtils.<List<TreeNode>>applySchedulers());
    }

    private int findChildNode(List<GitTree.TreeEntity> treeEntities, int index, TreeNode<Dir> dirNode,
                              String dirPath, final String owner, final String repo, String branchName) {
        while (index < treeEntities.size()
                && treeEntities.get(index).getPath().startsWith(dirPath + "/")) {
            GitTree.TreeEntity childEntity = treeEntities.get(index++);
            String childPath = childEntity.getPath();
            String nodeName = childPath.substring(childPath.lastIndexOf("/") + 1, childPath.length());
            switch (childEntity.getNodeType()) {
                case GitTree.DIR:
                    TreeNode<Dir> dirChildNode = new TreeNode<>(new Dir(nodeName, childPath));
                    index = findChildNode(treeEntities, index, dirChildNode, childEntity.getPath(), owner, repo, branchName);
                    dirNode.addChild(dirChildNode);
                    break;
                case GitTree.FILE:
                    dirNode.addChild(new TreeNode<>(new File(nodeName, childEntity.getHtml_url(owner, repo, branchName))));
                    break;
            }
        }
        // Compress the tree high and sort the child node list.
        List<TreeNode> childList = dirNode.getChildList();
        TreeNode firstChild = childList.get(0);
        LayoutItemType firstChildContent = firstChild.getContent();
        if (childList.size() == 1 && firstChildContent instanceof Dir) {
            Dir childDirNode = (Dir) firstChildContent;
            dirNode.getContent().dirName = StringUtils.append(dirNode.getContent().dirName, "/", childDirNode.dirName);
            childList.clear();
            List<TreeNode> firstChildChildList = firstChild.getChildList();
            for (TreeNode treeNode : firstChildChildList) {
                treeNode.setParent(dirNode);
            }
            childList.addAll(firstChildChildList);
        } else {
            //push Dir node to the front and pull File node to the back.
            sortNodeList(childList);
        }
        return index;
    }

    private void sortNodeList(List<TreeNode> nodeList) {
        int front = 0;
        int back = nodeList.size() - 1;
        while (true) {
            while (front < back && nodeList.get(front).getContent() instanceof Dir)
                front++;
            while (front < back && nodeList.get(back).getContent() instanceof File)
                back--;
            if (front < back)
                Collections.swap(nodeList, front, back);
            else break;
        }
    }

    public Observable<SearchResult<RepositoryInfo>> search(String keyWord, String language, int page) {
        return search(keyWord, language, null, page)
                .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
    }

    public Observable<SearchResult<RepositoryInfo>> search(String keyWord, String language, SortType_Repo sort, int page) {
        if (keyWord == null)
            keyWord = "";
//        if (location != null && !location.isEmpty()) {
//            q += "+location:" + location;
//        }
        if (!TextUtils.isEmpty(language))
            keyWord += language;
        if (TextUtils.isEmpty(sort.val()))
            return repositoryApi.search(keyWord, page, PER_PAGE)
                    .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
        return repositoryApi.search(keyWord, sort.val(), page, PER_PAGE)
                .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
    }

    public Observable<ReadMe> getReadMe(String owner, String repo) {
        return repositoryApi.getReadMe(owner, repo)
                .compose(RxJavaUtils.<ReadMe>applySchedulers());
    }
}
