package hu.gyongyosi.rssreader.articles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.api.ApiHelper;
import hu.gyongyosi.rssreader.common.BaseActivity;
import hu.gyongyosi.rssreader.models.Article;
import hu.gyongyosi.rssreader.models.Feed;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends BaseActivity implements OnRefreshListener {

    public static final String EXTRA_URL = "extraUrl";
    @BindView(R.id.feed_refresh)SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.feed_list)RecyclerView mRecyclerView;
    Unbinder unbinder;
    ApiHelper apiHelper;
    ArticleAdapter articleAdapter;
    Feed feed;
    CallbackManager callbackManager;
    Call<Feed> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        unbinder = ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(this);
        articleAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setAdapter(articleAdapter);
        apiHelper = new ApiHelper();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh(){
        showPb();
        call = apiHelper.getRssFeed(getIntent().getStringExtra(EXTRA_URL), new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if(response.body() != null) {
                    setTitle(response.body().getChannelTitle());
                    feed = response.body();
                    articleAdapter.refresh(feed);
                } else {
                    showSnack(R.string.error_general);
                }
                hidePb();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                showSnack(R.string.error_general);
                hidePb();
            }
        });
    }

    public void hidePb(){
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void showPb(){
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(call != null){
            call.cancel();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    ArticleAdapter.OnItemClickListener onItemClickListener = new ArticleAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Article article = feed.getArticleList().get(position);
            ShareLinkContent.Builder builder = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(article.getLink()))
                    .setContentTitle(article.getTitle())
                    .setContentDescription(article.getArticleDescription());

            if(article.getEnclosure() != null && article.getEnclosure().getUrl() != null){
                builder.setImageUrl(Uri.parse(article.getEnclosure().getUrl()));
            }

            ShareDialog.show(ArticleActivity.this, builder.build());
        }
    };

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
