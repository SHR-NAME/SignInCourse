package com.shr.filehelper.mvp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.shr.filehelper.R;

public class FileMainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_main);

        FileMainFragment mainFragment = new FileMainFragment();

        getSupportFragmentManager().beginTransaction().
                add(R.id.file_container, mainFragment)
                .commit();

        new MainPresenter(this, mainFragment);

    }
}
