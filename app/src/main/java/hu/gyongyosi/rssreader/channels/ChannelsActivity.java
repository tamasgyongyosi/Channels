package hu.gyongyosi.rssreader.channels;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.articles.ArticleActivity;
import hu.gyongyosi.rssreader.channels.ChannelAdapter.OnItemClickListener;
import hu.gyongyosi.rssreader.channels.ChannelAdapter.ViewHolder;
import hu.gyongyosi.rssreader.common.BaseActivity;
import hu.gyongyosi.rssreader.realm.RealmHelper;
import io.realm.Realm;
import io.realm.Realm.Transaction.OnError;
import io.realm.Realm.Transaction.OnSuccess;
import io.realm.Sort;

public class ChannelsActivity extends BaseActivity {

    @BindView(R.id.rss_list) RecyclerView mRecyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;
    Unbinder unbinder;
    Realm realm;
    String sort = "title";
    Sort order = Sort.ASCENDING;
    RealmHelper realmHelper;
    ChannelAdapter channelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        unbinder = ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        channelAdapter = new ChannelAdapter(null);
        channelAdapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(channelAdapter);
        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChannelsActivity.this, AddChannelActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        channelAdapter.refresh(realmHelper.getAllChannel(sort, order));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        channelAdapter.setOnItemClickListener(null);
        mRecyclerView.setAdapter(null);
        unbinder.unbind();
        realm.close();
    }

    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            delete(((ViewHolder) viewHolder).getUrl());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    }

    private void delete(String url){
        realmHelper.deleteFromRealm(url, new OnSuccess() {
            @Override
            public void onSuccess() {
                refresh();
                showSnack(R.string.channel_delete);
            }
        }, new OnError() {
            @Override
            public void onError(Throwable error) {
                showSnack(R.string.error_general);
            }
        });
    }

    ChannelAdapter.OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(ChannelsActivity.this, ArticleActivity.class);
            intent.putExtra(ArticleActivity.EXTRA_URL, channelAdapter.getItem(position).getUrl());
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channels, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:
                if(order == Sort.ASCENDING){
                    order = Sort.DESCENDING;
                } else {
                    order = Sort.ASCENDING;
                }
                refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
