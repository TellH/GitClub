package tellh.com.gitclub.presentation.contract;


import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

/**
 * Created by tlh on 2016/9/16 :)
 */
public interface ShowError {
    void showOnError(String msg, @UpdateType int updateType);
}
