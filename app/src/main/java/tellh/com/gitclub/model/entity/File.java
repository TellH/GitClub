package tellh.com.gitclub.model.entity;

import tellh.com.gitclub.R;
import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class File implements LayoutItemType {
    public String fileName;

    public String html_url;

    public File(String fileName, String html_url) {
        this.fileName = fileName;
        this.html_url = html_url;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                ", html_url='" + html_url + '\'' +
                '}';
    }
}
