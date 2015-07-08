package com.beak.bweibo.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.fragment.UserFragment;
import com.sina.weibo.sdk.openapi.models.User;

public class UserActivity extends ToolbarActivity {

    private UserFragment mUserFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mUserFragment = (UserFragment)getSupportFragmentManager().findFragmentById(R.id.user_fragment);
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        User user = (User)getIntent().getSerializableExtra(Finals.KEY_USER);
        if (user != null) {
            mUserFragment.loadUser(user);
            return;
        }
        long uid = getIntent().getLongExtra(Finals.KEY_UID, 0);
        if (uid != 0) {
            mUserFragment.loadUser(uid);
            return;
        }
        String screenName = getIntent().getStringExtra(Finals.KEY_AT);
        if (screenName != null) {
            //Toast.makeText(this, screenName, Toast.LENGTH_SHORT).show();
            mUserFragment.loadUser(screenName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
