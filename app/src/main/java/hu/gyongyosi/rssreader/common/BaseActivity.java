package hu.gyongyosi.rssreader.common;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.info.InfoActivity;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class BaseActivity extends AppCompatActivity {

    protected Snackbar snackbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.show_info:
                startActivity(new Intent(BaseActivity.this, InfoActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showSnack(int resId){
        if(getCurrentFocus() == null){
            return;
        }
        if(snackbar != null){
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(getCurrentFocus(), resId, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
