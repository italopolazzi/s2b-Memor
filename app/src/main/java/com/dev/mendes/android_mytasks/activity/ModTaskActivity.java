package com.dev.mendes.android_mytasks.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.dataBase.Task;
import com.dev.mendes.android_mytasks.fragment.ModTaskFragment;

public class ModTaskActivity extends AppCompatActivity {

    public static String EXTRA_TASK = "TASK";
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().hide();

        try {
            task = getIntent().getExtras().getParcelable(EXTRA_TASK);
        } catch (NullPointerException n){
            task = null;
        }

        if(task != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, ModTaskFragment.newInstance(task))
                    .commit();

        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new ModTaskFragment(false))
                    .commit();

        }
    }

}
