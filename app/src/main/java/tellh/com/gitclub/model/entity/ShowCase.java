package tellh.com.gitclub.model.entity;

import com.tellh.nolistadapter.DataBean;
import com.tellh.nolistadapter.IListAdapter;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class ShowCase extends DataBean {
    /**
     * name : Made in Africa
     * slug : made-in-africa
     * description : African tech is booming – so here are just a few of the great open source projects driving the continent.
     * image_url : http://trending.codehub-app.com/made-in-africa.png
     */

    private String name;
    private String slug;
    private String description;
    private String image_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_showcase;
    }
}
