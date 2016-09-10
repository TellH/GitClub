package tellh.com.gitclub.common.wrapper;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class ImageLoader {
    public static void load(Context context, int resId, ImageView view) {
        Picasso.with(context)
                .load(resId)
//                .centerCrop()
                .into(view);
    }

    public static void load(Context context, String url, ImageView view) {
        Picasso.with(context)
                .load(url)
//                .centerCrop()
                .into(view);
    }

    public static void load(String url, ImageView view) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }

    public static void loadAndCrop(String url, ImageView view) {
        Picasso.with(view.getContext())
                .load(url)
                .fit()
                .centerCrop()
                .into(view);
    }
}
