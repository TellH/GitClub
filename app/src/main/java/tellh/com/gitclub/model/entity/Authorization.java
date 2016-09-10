package tellh.com.gitclub.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Authorization implements Parcelable {
    private int id;
    private String url;

    private AppBean app;
    private String token;
    private String hashed_token;
    private String token_last_eight;
    private String note;
    private String note_url;
    private String created_at;
    private String updated_at;
    private String fingerprint;
    private String[] scopes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppBean getApp() {
        return app;
    }

    public void setApp(AppBean app) {
        this.app = app;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHashed_token() {
        return hashed_token;
    }

    public void setHashed_token(String hashed_token) {
        this.hashed_token = hashed_token;
    }

    public String getToken_last_eight() {
        return token_last_eight;
    }

    public void setToken_last_eight(String token_last_eight) {
        this.token_last_eight = token_last_eight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_url() {
        return note_url;
    }

    public void setNote_url(String note_url) {
        this.note_url = note_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public static class AppBean implements Parcelable {
        private String name;
        private String url;
        private String client_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.url);
            dest.writeString(this.client_id);
        }

        public AppBean() {
        }

        protected AppBean(Parcel in) {
            this.name = in.readString();
            this.url = in.readString();
            this.client_id = in.readString();
        }

        public static final Parcelable.Creator<AppBean> CREATOR = new Parcelable.Creator<AppBean>() {
            @Override
            public AppBean createFromParcel(Parcel source) {
                return new AppBean(source);
            }

            @Override
            public AppBean[] newArray(int size) {
                return new AppBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
        dest.writeParcelable(this.app, flags);
        dest.writeString(this.token);
        dest.writeString(this.hashed_token);
        dest.writeString(this.token_last_eight);
        dest.writeString(this.note);
        dest.writeString(this.note_url);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.fingerprint);
        dest.writeStringArray(this.scopes);
    }

    public Authorization() {
    }

    protected Authorization(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.app = in.readParcelable(AppBean.class.getClassLoader());
        this.token = in.readString();
        this.hashed_token = in.readString();
        this.token_last_eight = in.readString();
        this.note = in.readString();
        this.note_url = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.fingerprint = in.readString();
        this.scopes = in.createStringArray();
    }

    public static final Parcelable.Creator<Authorization> CREATOR = new Parcelable.Creator<Authorization>() {
        @Override
        public Authorization createFromParcel(Parcel source) {
            return new Authorization(source);
        }

        @Override
        public Authorization[] newArray(int size) {
            return new Authorization[size];
        }
    };
}