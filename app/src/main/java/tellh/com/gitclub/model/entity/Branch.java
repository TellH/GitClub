package tellh.com.gitclub.model.entity;

/**
 * Created by tlh on 2016/10/2 :)
 */

public class Branch {

    /**
     * name : master
     * commit : {"sha":"dbc27ba20c6aba34ff148598a370f6c27e06c468","url":"https://api.github.com/repos/TellH/AutoGo/commits/dbc27ba20c6aba34ff148598a370f6c27e06c468"}
     */

    private String name;
    /**
     * sha : dbc27ba20c6aba34ff148598a370f6c27e06c468
     * url : https://api.github.com/repos/TellH/AutoGo/commits/dbc27ba20c6aba34ff148598a370f6c27e06c468
     */

    private CommitEntity commit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommitEntity getCommit() {
        return commit;
    }

    public void setCommit(CommitEntity commit) {
        this.commit = commit;
    }

    public static class CommitEntity {
        private String sha;
        private String url;

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
