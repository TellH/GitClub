package tellh.com.gitclub.model.entity;

import com.google.gson.annotations.SerializedName;
import com.tellh.nolistadapter.DataBean;
import com.tellh.nolistadapter.IListAdapter;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class RepositoryInfo extends DataBean {

    public boolean hasCheckState = false;
    public boolean hasStarred = false;
    public boolean hasWatched = false;
    /**
     * id : 64680262
     * name : AutoGo
     * full_name : TellH/AutoGo
     * owner : {"login":"TellH","id":15800681,"avatar_url":"https://avatars.githubusercontent.com/u/15800681?v=3","gravatar_id":"","url":"https://api.github.com/users/TellH","html_url":"https://github.com/TellH","followers_url":"https://api.github.com/users/TellH/followers","following_url":"https://api.github.com/users/TellH/following{/other_user}","gists_url":"https://api.github.com/users/TellH/gists{/gist_id}","starred_url":"https://api.github.com/users/TellH/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/TellH/subscriptions","organizations_url":"https://api.github.com/users/TellH/orgs","repos_url":"https://api.github.com/users/TellH/repos","events_url":"https://api.github.com/users/TellH/events{/privacy}","received_events_url":"https://api.github.com/users/TellH/received_events","type":"User","site_admin":false}
     * private : false
     * html_url : https://github.com/TellH/AutoGo
     * description : Ease your code, easy go!
     * ic_fork : false
     * url : https://api.github.com/repos/TellH/AutoGo
     * forks_url : https://api.github.com/repos/TellH/AutoGo/forks
     * keys_url : https://api.github.com/repos/TellH/AutoGo/keys{/key_id}
     * collaborators_url : https://api.github.com/repos/TellH/AutoGo/collaborators{/collaborator}
     * teams_url : https://api.github.com/repos/TellH/AutoGo/teams
     * hooks_url : https://api.github.com/repos/TellH/AutoGo/hooks
     * issue_events_url : https://api.github.com/repos/TellH/AutoGo/issues/events{/number}
     * events_url : https://api.github.com/repos/TellH/AutoGo/events
     * assignees_url : https://api.github.com/repos/TellH/AutoGo/assignees{/user}
     * branches_url : https://api.github.com/repos/TellH/AutoGo/branches{/branch}
     * tags_url : https://api.github.com/repos/TellH/AutoGo/tags
     * blobs_url : https://api.github.com/repos/TellH/AutoGo/git/blobs{/sha}
     * git_tags_url : https://api.github.com/repos/TellH/AutoGo/git/tags{/sha}
     * git_refs_url : https://api.github.com/repos/TellH/AutoGo/git/refs{/sha}
     * trees_url : https://api.github.com/repos/TellH/AutoGo/git/trees{/sha}
     * statuses_url : https://api.github.com/repos/TellH/AutoGo/statuses/{sha}
     * languages_url : https://api.github.com/repos/TellH/AutoGo/languages
     * stargazers_url : https://api.github.com/repos/TellH/AutoGo/stargazers
     * contributors_url : https://api.github.com/repos/TellH/AutoGo/contributors
     * subscribers_url : https://api.github.com/repos/TellH/AutoGo/subscribers
     * subscription_url : https://api.github.com/repos/TellH/AutoGo/subscription
     * commits_url : https://api.github.com/repos/TellH/AutoGo/commits{/sha}
     * git_commits_url : https://api.github.com/repos/TellH/AutoGo/git/commits{/sha}
     * comments_url : https://api.github.com/repos/TellH/AutoGo/comments{/number}
     * issue_comment_url : https://api.github.com/repos/TellH/AutoGo/issues/comments{/number}
     * contents_url : https://api.github.com/repos/TellH/AutoGo/contents/{+path}
     * compare_url : https://api.github.com/repos/TellH/AutoGo/compare/{base}...{head}
     * merges_url : https://api.github.com/repos/TellH/AutoGo/merges
     * archive_url : https://api.github.com/repos/TellH/AutoGo/{archive_format}{/ref}
     * downloads_url : https://api.github.com/repos/TellH/AutoGo/downloads
     * issues_url : https://api.github.com/repos/TellH/AutoGo/issues{/number}
     * pulls_url : https://api.github.com/repos/TellH/AutoGo/pulls{/number}
     * milestones_url : https://api.github.com/repos/TellH/AutoGo/milestones{/number}
     * notifications_url : https://api.github.com/repos/TellH/AutoGo/notifications{?since,all,participating}
     * labels_url : https://api.github.com/repos/TellH/AutoGo/labels{/name}
     * releases_url : https://api.github.com/repos/TellH/AutoGo/releases{/id}
     * deployments_url : https://api.github.com/repos/TellH/AutoGo/deployments
     * created_at : 2016-08-01T15:48:13Z
     * updated_at : 2016-08-25T20:53:50Z
     * pushed_at : 2016-08-16T06:52:23Z
     * git_url : git://github.com/TellH/AutoGo.git
     * ssh_url : git@github.com:TellH/AutoGo.git
     * clone_url : https://github.com/TellH/AutoGo.git
     * svn_url : https://github.com/TellH/AutoGo
     * homepage : null
     * size : 171
     * stars : 94
     * watchers_count : 94
     * language : Java
     * has_issues : true
     * has_downloads : true
     * has_wiki : true
     * has_pages : false
     * forks_count : 14
     * mirror_url : null
     * open_issues_count : 1
     * forks : 14
     * open_issues : 1
     * watchers : 94
     * default_branch : master
     * network_count : 14
     * subscribers_count : 6
     */

    private int id;
    private String name;
    private String full_name;
    /**
     * login : TellH
     * id : 15800681
     * avatar_url : https://avatars.githubusercontent.com/u/15800681?v=3
     * gravatar_id :
     * url : https://api.github.com/users/TellH
     * html_url : https://github.com/TellH
     * followers_url : https://api.github.com/users/TellH/followers
     * following_url : https://api.github.com/users/TellH/following{/other_user}
     * gists_url : https://api.github.com/users/TellH/gists{/gist_id}
     * starred_url : https://api.github.com/users/TellH/starred{/owner}{/repo}
     * subscriptions_url : https://api.github.com/users/TellH/subscriptions
     * organizations_url : https://api.github.com/users/TellH/orgs
     * repos_url : https://api.github.com/users/TellH/repos
     * events_url : https://api.github.com/users/TellH/events{/privacy}
     * received_events_url : https://api.github.com/users/TellH/received_events
     * type : User
     * site_admin : false
     */

    private UserEntity owner;
    @SerializedName("private")
    private boolean privateX;
    private String html_url;
    private String description;
    private boolean fork;
    @SerializedName("stargazers_count")
    public int stars;
    private int watchers_count;
    private String language;
    private int forks_count;
    private int open_issues_count;
    private int forks;
    private int open_issues;
    private String created_at;
    private String updated_at;

    public int getSubscribers_count() {
        return subscribers_count;
    }

    public void setSubscribers_count(int subscribers_count) {
        this.subscribers_count = subscribers_count;
    }

    private int subscribers_count;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {

        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public boolean isPrivateX() {
        return privateX;
    }

    public void setPrivateX(boolean privateX) {
        this.privateX = privateX;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(int open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getOpen_issues() {
        return open_issues;
    }

    public void setOpen_issues(int open_issues) {
        this.open_issues = open_issues;
    }

    @Override
    public String toString() {
        return "RepositoryInfo{" +
                "hasCheckState=" + hasCheckState +
                ", hasStarred=" + hasStarred +
                ", hasWatched=" + hasWatched +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", owner=" + owner +
                ", privateX=" + privateX +
                ", html_url='" + html_url + '\'' +
                ", description='" + description + '\'' +
                ", fork=" + fork +
                ", stars=" + stars +
                ", watchers_count=" + watchers_count +
                ", language='" + language + '\'' +
                ", forks_count=" + forks_count +
                ", open_issues_count=" + open_issues_count +
                ", forks=" + forks +
                ", open_issues=" + open_issues +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", subscribers_count=" + subscribers_count +
                '}';
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_repo;
    }
}
