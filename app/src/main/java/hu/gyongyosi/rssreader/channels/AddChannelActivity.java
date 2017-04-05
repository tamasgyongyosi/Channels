package hu.gyongyosi.rssreader.channels;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.api.ApiHelper;
import hu.gyongyosi.rssreader.common.BaseActivity;
import hu.gyongyosi.rssreader.models.Feed;
import hu.gyongyosi.rssreader.realm.RealmHelper;
import io.realm.Realm;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChannelActivity extends BaseActivity {

    @BindView(R.id.add_rss_url_input)
    TextInputEditText rssUrlEt;
    @BindView(R.id.add_rss_btn)
    Button addBtn;
    Unbinder unbinder;
    Realm realm;
    Call<Feed> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        unbinder = ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
    }

    @OnClick(R.id.add_rss_btn)
    public void addBtnOnClick(){
        String url = rssUrlEt.getText().toString();
        if(TextUtils.isEmpty(url)){
            rssUrlEt.setError(getString(R.string.error_required_field));
            rssUrlEt.setSelected(true);
            return;
        }

        if(!url.endsWith("/")) {
            url = url + "/";
        }

        if(!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }

        final String normalizedUrl = url;
        try {
            HttpUrl.parse(normalizedUrl);
        } catch (Exception e){
            showSnack(R.string.error_general);
            return;
        }
        addBtn.setClickable(false);
        ApiHelper apiHelper = new ApiHelper();
        call = apiHelper.getRssFeed(normalizedUrl, new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                addToRealm(response.body(), normalizedUrl);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                showSnack(R.string.error_general);
                addBtn.setClickable(true);
            }
        });
    }

    private void addToRealm(final Feed feed, final String url){
        RealmHelper realmHelper = new RealmHelper(realm);
        realmHelper.addToRealm(feed, url, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                finish();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                showSnack(R.string.error_general);
                addBtn.setClickable(true);
            }
        });
    }

    @OnTextChanged(R.id.add_rss_url_input)
    public void urlInputTextChanged(){
        rssUrlEt.setSelected(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        realm.close();
        if(call != null){
            call.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
