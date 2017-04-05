package hu.gyongyosi.rssreader.articles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.gyongyosi.rssreader.R;
import hu.gyongyosi.rssreader.models.Article;
import hu.gyongyosi.rssreader.models.Feed;

/**
 * Created by gyongyosit on 2017.04.03..
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    Feed feed;
    Context context;
    OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ArticleAdapter(Context context) {
        this.context = context;
    }

    public void refresh(Feed feed){
        this.feed = feed;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.article_title) TextView title;
        @BindView(R.id.article_description) TextView description;
        @BindView(R.id.article_date) TextView date;
        @BindView(R.id.article_image) ImageView image;
        @BindView(R.id.row_holder)LinearLayout holder;
        @BindView(R.id.article_share)ImageView shareIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            shareIv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return feed != null ? feed.getArticleList().size() : 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (feed == null) {
            return;
        }

        final Article article = feed.getArticleList().get(position);
        holder.title.setText(article.getTitle() != null ? Html.fromHtml(article.getTitle()) : "");
        holder.description.setText(article.getArticleDescription() != null ? Html.fromHtml(article.getArticleDescription()) : "");
        holder.date.setText(article.getPubDate() != null ? article.getPubDate() : "");

        if (article.getEnclosure() != null) {
            Glide.with(context)
                    .load(article.getEnclosure().getUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_news)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
        }

        if(article.getLink() != null){
            holder.holder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LinkActivity.class);
                    intent.putExtra(LinkActivity.EXTRA_LINK, article.getLink());
                    intent.putExtra(LinkActivity.EXTRA_TITLE, article.getTitle());
                    context.startActivity(intent);
                }
            });
        }
    }

}
