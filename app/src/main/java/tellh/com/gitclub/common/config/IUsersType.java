package tellh.com.gitclub.common.config;

import android.support.annotation.StringDef;

/**
 * Created by tlh on 2016/9/20 :)
 */

public interface IUsersType {
    String USER = "User";
    String ORGANIZATION = "Organization";

    @StringDef({USER, ORGANIZATION})
    @interface UsersType {
    }
}
