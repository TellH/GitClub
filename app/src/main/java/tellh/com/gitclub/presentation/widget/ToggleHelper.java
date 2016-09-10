package tellh.com.gitclub.presentation.widget;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by tlh on 2016/9/2 :)
 */
interface ToggleHelper<T extends View> {
    boolean toggle(@NonNull T me);

    void setState(@NonNull T me, boolean state);

    void setState(T me);
}
