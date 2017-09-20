package com.lyj.bannerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by yu on 2017/9/19.
 */

public class ActNext extends Activity {

    private TextView mShowTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mShowTv = (TextView) findViewById(R.id.tv_show);
        String title = getIntent().getStringExtra("TITLE");
        mShowTv.setText(title);
    }

    public static void actionStart(Context context,String title){
        Intent intent = new Intent(context,ActNext.class);
        intent.putExtra("TITLE",title);
        context.startActivity(intent);

    }
}
