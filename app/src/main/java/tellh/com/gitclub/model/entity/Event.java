package tellh.com.gitclub.model.entity;

import com.google.gson.annotations.SerializedName;
import com.tellh.nolistadapter.DataBean;
import com.tellh.nolistadapter.IListAdapter;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.IEventType;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class Event extends DataBean {

    /**
     * id : 4474452714
     * type : WatchEvent
     * actor : {"id":5214214,"login":"drakeet","display_login":"drakeet","gravatar_id":""}
     * repo : {"id":44109745,"name":"google/android-classyshark"}
     * payload : {"action":"started"}
     * public : true
     * created_at : 2016-08-26T09:27:44Z
     */

    private String id;
    private String type;
    /**
     * id : 5214214
     * login : drakeet
     * display_login : drakeet
     * gravatar_id :
     */

    private ActorEntity actor;

    private ActorEntity org;
    /**
     * id : 44109745
     * name : google/android-classyshark
     */

    private RepoEntity repo;
    /**
     * action : started
     */

    private PayloadEntity payload;
    @SerializedName("public")
    private boolean publicX;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type != null ? type : IEventType.Unhandled;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActorEntity getActor() {
        return actor;
    }

    public void setActor(ActorEntity actor) {
        this.actor = actor;
    }

    public ActorEntity getOrg() {
        return org;
    }

    public void setOrg(ActorEntity org) {
        this.org = org;
    }

    public RepoEntity getRepo() {
        return repo;
    }

    public void setRepo(RepoEntity repo) {
        this.repo = repo;
    }

    public PayloadEntity getPayload() {
        return payload;
    }

    public void setPayload(PayloadEntity payload) {
        this.payload = payload;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_news;
    }

    public static class ActorEntity {
        private int id;
        private String login;
        private String display_login;
        private String avatar_url;

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getDisplay_login() {
            return display_login;
        }

        public void setDisplay_login(String display_login) {
            this.display_login = display_login;
        }
    }

    public static class RepoEntity {
        private int id;
        private String name;

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
    }

    public static class PayloadEntity {
        public String action;
        public String description;
        public RepositoryInfo repository;
        public UserInfo sender;
        public int number;
        public PullRequest pull_request;

        @SerializedName("public")
        public boolean is_public;
        public UserInfo org;
        public String created_at;
        public Issue issue;
        public CommitComment comment;
        public Release release;
        public Team team;
        public long push_id;
        public int size;
        public int distinct_size;
        public String ref;
        public String head;
        public String before;
        public RepositoryInfo forkee;
        public UserInfo member;

        public static class PullRequest {
            public int number;
            public String title;
        }

        public static class Issue {
            public int number;
            public String title;
        }

        public static class CommitComment {
            public String body;
        }

        public static class Release {
            public String body;
        }

        public static class Team {
            public String name;
        }
    }
}
