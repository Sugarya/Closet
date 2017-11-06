package com.sugarya.closet.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Ethan on 17/2/21.
 * 弹出提示的工具类
 */

public class HintUtils {

    private static HintUtils sHintUtils;
    private Toast mToast;

    private HintUtils() {
    }

    public static HintUtils getInstance(){
        if(sHintUtils == null){
            synchronized (HintUtils.class){
                if(sHintUtils == null){
                    sHintUtils = new HintUtils();
                }
            }
        }
        return sHintUtils;
    }

    public static void showLongSnackBar(View view, String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show();
    }

    public static void showShortSnackBar(View view, String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }

    public void displayShortToast(Context context,String msg){
        if(context == null){
            return;
        }
        if(mToast == null){
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static void showShortToast(Context context,String msg){
        if(context == null) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
