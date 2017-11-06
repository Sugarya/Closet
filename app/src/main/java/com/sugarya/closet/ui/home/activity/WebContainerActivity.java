package com.sugarya.closet.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sugarya.closet.R;
import com.sugarya.closet.ui.common.basic.BasicActivity;
import com.sugarya.closet.ui.common.basic.BasicFragment;
import com.sugarya.closet.ui.common.basic.BasicWebFragmentWithToolBar;
import com.sugarya.closet.ui.common.constants.HomeConstants;

import static com.sugarya.closet.ui.common.constants.HomeConstants.ARGUMENT_KEY_BASIC_WEB_HEADER_TITLE;
import static com.sugarya.closet.ui.common.constants.HomeConstants.ARGUMENT_KEY_BASIC_WEB_URL;


public class WebContainerActivity extends BasicActivity {


    private String mUrl = "";
    private String mHeaderTitle = "";

    public static Intent getCallingIntent(Context context, String url, String headerTitle){
        Intent intent = new Intent(context,WebContainerActivity.class);
        intent.putExtra(HomeConstants.KEY_INTENT_ACTIVITY_WEB_URL, url);
        intent.putExtra(HomeConstants.KEY_INTENT_ACTIVITY_HEADER_TITLE, headerTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initShow();
    }

    private void handleIntent(){
        Intent intent = getIntent();
        if(intent != null){
            mUrl = intent.getStringExtra(HomeConstants.KEY_INTENT_ACTIVITY_WEB_URL);
            mHeaderTitle = intent.getStringExtra(HomeConstants.KEY_INTENT_ACTIVITY_HEADER_TITLE);
        }
    }

    private void initShow(){
        showOneFragment(BasicWebFragmentWithToolBar.class.getSimpleName(), true, false);
    }



    @Override
    protected BasicFragment fragmentProvider(String fragmentTab, Bundle arguments) {
        BasicFragment basicFragment;
        if(BasicWebFragmentWithToolBar.class.getSimpleName().equals(fragmentTab)){
            arguments.putString(ARGUMENT_KEY_BASIC_WEB_URL, mUrl);
            arguments.putString(ARGUMENT_KEY_BASIC_WEB_HEADER_TITLE, mHeaderTitle);
            basicFragment = BasicWebFragmentWithToolBar.newInstance(arguments);
        }else{
            basicFragment = BasicWebFragmentWithToolBar.newInstance(arguments);
        }
        return basicFragment;
    }


    @Override
    protected int getFragmentContainerResID() {
        return R.id.frame_activity_body;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_web_container;
    }


}
