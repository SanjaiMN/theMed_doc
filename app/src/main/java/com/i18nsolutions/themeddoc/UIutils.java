package com.i18nsolutions.themeddoc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
public class UIutils {

    private Activity mActivity;

    public UIutils(Activity activity){
        mActivity = activity;
    }
    public void showPhoto(Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        mActivity.startActivity(intent);
    }
}
