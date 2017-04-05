package hu.gyongyosi.rssreader.channels;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.channels.ChannelAdapter.ViewHolder;
import hu.gyongyosi.rssreader.models.Channel;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class ChannelAdapter extends RealmRecyclerViewAdapter<Channel, ViewHolder> {

    OnItemClickListener mItemClickListener;
    private int lastPosition = ListView.INVALID_POSITION;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public ChannelAdapter(OrderedRealmCollection<Channel> channels) {
        super(channels, false);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_title) TextView title;
        @BindView(R.id.item_description) TextView description;
        @BindView(R.id.row_holder)LinearLayout holder;
        private String url;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            holder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

        public String getUrl() {
            return url;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rss_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Channel channel = getItem(position);
        holder.title.setText(channel.getTitle());
        holder.description.setText(channel.getDescription());
        holder.url = channel.getUrl();
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void refresh(RealmResults<Channel> channels){
        lastPosition = channels.size();
        this.updateData(channels);
    }
}
