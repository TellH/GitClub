package tellh.com.gitclub.model.entity;

import java.util.List;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class SearchResult<T> {

    /**
     * total_count : 22
     * incomplete_results : false
     * items : [{"id":7222719,"name":"autogo","full_name":"polaris1119/autogo","owner":{"login":"polaris1119","id":899673,"avatar_url":"https://avatars.githubusercontent.com/u/899673?v=3","gravatar_id":"","url":"https://api.github.com/users/polaris1119","html_url":"https://github.com/polaris1119","followers_url":"https://api.github.com/users/polaris1119/followers","following_url":"https://api.github.com/users/polaris1119/following{/other_user}","gists_url":"https://api.github.com/users/polaris1119/gists{/gist_id}","starred_url":"https://api.github.com/users/polaris1119/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/polaris1119/subscriptions","organizations_url":"https://api.github.com/users/polaris1119/orgs","repos_url":"https://api.github.com/users/polaris1119/repos","events_url":"https://api.github.com/users/polaris1119/events{/privacy}","received_events_url":"https://api.github.com/users/polaris1119/received_events","type":"User","site_admin":false},"private":false,"html_url":"https://github.com/polaris1119/autogo","description":"Go语言是静态语言，修改源代码总是需要编译、运行，如果用Go做Web开发，修改一点就要编译、运行，然后才能看结果，很痛苦。autogo就是为了让Go开发更方便。","ic_fork":false,"url":"https://api.github.com/repos/polaris1119/autogo","forks_url":"https://api.github.com/repos/polaris1119/autogo/forks","keys_url":"https://api.github.com/repos/polaris1119/autogo/keys{/key_id}","collaborators_url":"https://api.github.com/repos/polaris1119/autogo/collaborators{/collaborator}","teams_url":"https://api.github.com/repos/polaris1119/autogo/teams","hooks_url":"https://api.github.com/repos/polaris1119/autogo/hooks","issue_events_url":"https://api.github.com/repos/polaris1119/autogo/issues/events{/number}","events_url":"https://api.github.com/repos/polaris1119/autogo/events","assignees_url":"https://api.github.com/repos/polaris1119/autogo/assignees{/user}","branches_url":"https://api.github.com/repos/polaris1119/autogo/branches{/branch}","tags_url":"https://api.github.com/repos/polaris1119/autogo/tags","blobs_url":"https://api.github.com/repos/polaris1119/autogo/git/blobs{/sha}","git_tags_url":"https://api.github.com/repos/polaris1119/autogo/git/tags{/sha}","git_refs_url":"https://api.github.com/repos/polaris1119/autogo/git/refs{/sha}","trees_url":"https://api.github.com/repos/polaris1119/autogo/git/trees{/sha}","statuses_url":"https://api.github.com/repos/polaris1119/autogo/statuses/{sha}","languages_url":"https://api.github.com/repos/polaris1119/autogo/languages","stargazers_url":"https://api.github.com/repos/polaris1119/autogo/stargazers","contributors_url":"https://api.github.com/repos/polaris1119/autogo/contributors","subscribers_url":"https://api.github.com/repos/polaris1119/autogo/subscribers","subscription_url":"https://api.github.com/repos/polaris1119/autogo/subscription","commits_url":"https://api.github.com/repos/polaris1119/autogo/commits{/sha}","git_commits_url":"https://api.github.com/repos/polaris1119/autogo/git/commits{/sha}","comments_url":"https://api.github.com/repos/polaris1119/autogo/comments{/number}","issue_comment_url":"https://api.github.com/repos/polaris1119/autogo/issues/comments{/number}","contents_url":"https://api.github.com/repos/polaris1119/autogo/contents/{+path}","compare_url":"https://api.github.com/repos/polaris1119/autogo/compare/{base}...{head}","merges_url":"https://api.github.com/repos/polaris1119/autogo/merges","archive_url":"https://api.github.com/repos/polaris1119/autogo/{archive_format}{/ref}","downloads_url":"https://api.github.com/repos/polaris1119/autogo/downloads","issues_url":"https://api.github.com/repos/polaris1119/autogo/issues{/number}","pulls_url":"https://api.github.com/repos/polaris1119/autogo/pulls{/number}","milestones_url":"https://api.github.com/repos/polaris1119/autogo/milestones{/number}","notifications_url":"https://api.github.com/repos/polaris1119/autogo/notifications{?since,all,participating}","labels_url":"https://api.github.com/repos/polaris1119/autogo/labels{/name}","releases_url":"https://api.github.com/repos/polaris1119/autogo/releases{/id}","deployments_url":"https://api.github.com/repos/polaris1119/autogo/deployments","created_at":"2012-12-18T12:09:25Z","updated_at":"2016-08-22T06:44:32Z","pushed_at":"2013-01-04T02:38:42Z","git_url":"git://github.com/polaris1119/autogo.git","ssh_url":"git@github.com:polaris1119/autogo.git","clone_url":"https://github.com/polaris1119/autogo.git","svn_url":"https://github.com/polaris1119/autogo","homepage":"http://studygolang.com","size":158,"stargazers_count":69,"watchers_count":69,"language":"Go","has_issues":true,"has_downloads":true,"has_wiki":true,"has_pages":false,"forks_count":12,"mirror_url":null,"open_issues_count":1,"forks":12,"open_issues":1,"watchers":69,"default_branch":"master","score":70.9584}]
     */

    private int total_count;

    private List<T> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
