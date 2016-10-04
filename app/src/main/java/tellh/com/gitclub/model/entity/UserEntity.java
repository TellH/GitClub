package tellh.com.gitclub.model.entity;

import com.tellh.nolistadapter.DataBean;
import com.tellh.nolistadapter.IListAdapter;

import tellh.com.gitclub.R;

public class UserEntity extends DataBean {
    private String login;
    private int id;
    private String avatar_url;
    private String html_url;
    private String type;

    public boolean hasCheck = false;
    private String bio;
    public boolean isFollowing = false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_user;
    }
}
