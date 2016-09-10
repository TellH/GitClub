package tellh.com.gitclub.model.entity;

/**
 * Created by tlh on 2016/8/28 :)
 */
public class ReadMe {

    /**
     * name : README.md
     * html_url : https://github.com/TellH/AutoGo/blob/master/README.md
     * type : file
     */

    private String name;
    private String html_url;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
