package hu.gyongyosi.rssreader.articles;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.common.BaseActivity;

public class LinkActivity extends BaseActivity {

    public static final String EXTRA_LINK = "extraLink";
    public static final String EXTRA_TITLE = "extraTitle";
    @BindView(R.id.link_web)WebView webView;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        unbinder = ButterKnife.bind(this);
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getIntent().getStringExtra(EXTRA_LINK));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
