package com.sugarya.closet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sugarya.closet.R;

/**
 * 图片加载器工具类
 * Created by Ethan on 2017/7/11.
 */

public class ImageLoader {

    public static void display(Context context, String url, ImageView imageView){
        if(context == null || imageView == null){
            return;
        }
        if (TextUtils.isEmpty(url)) {
            url = "http://";
        }

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.icon_image_holder)
                .error(R.drawable.icon_image_holder)
                .into(imageView);
    }


}
