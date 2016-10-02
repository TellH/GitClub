package tellh.com.gitclub.model.entity;

import tellh.com.gitclub.R;
import tellh.com.recyclertreeview_lib.LayoutItemType;


/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {
    public String dirName;

    public String path;

    public Dir(String dirName, String path) {
        this.dirName = dirName;
        this.path = path;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }

    @Override
    public String toString() {
        return "Dir{" +
                "dirName='" + dirName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
