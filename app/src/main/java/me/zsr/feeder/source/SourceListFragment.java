package me.zsr.feeder.source;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.zsr.feeder.App;
import me.zsr.feeder.R;
import me.zsr.feeder.dao.FeedSource;
import me.zsr.feeder.data.FeedDB;
import me.zsr.feeder.data.FeedNetwork;
import me.zsr.feeder.base.BaseFragment;
import me.zsr.feeder.util.CommonEvent;
import me.zsr.feeder.util.LogUtil;
import me.zsr.feeder.util.NetworkUtil;
import me.zsr.feeder.util.VolleySingleton;

/**
 * @description:
 * @author: Zhangshaoru
 * @date: 10/28/15
 */
public class SourceListFragment extends BaseFragment {
    private static SourceListFragment sInstance;
    private List<FeedSource> mSourceList = new ArrayList<>();
    private OnSourceSelectedListener mListener;

    private View mRootView;
    private ListView mListView;
    private FeedAdapter mAdapter;
    private SwipeRefreshLayout mPullRefreshLayout;
    private View mAllHeaderView;

    public static SourceListFragment getInstance() {
        if (sInstance == null) {
            sInstance = new SourceListFragment();
        }
        return sInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_source_list, container, false);
        initData();
        initView();
        setListener();

        // Auto refresh while wifi is enabled
        if (NetworkUtil.isWifiEnabled(getActivity())) {
            FeedNetwork.getInstance().refreshAll();
        }
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSourceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSourceSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mSourceList = FeedDB.getInstance().loadAll();
    }

    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.feed_lv);

        mAllHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.source_list_item, null);
        ((NetworkImageView) mAllHeaderView.findViewById(R.id.source_favicon_img)).setDefaultImageResId(R.drawable.ic_all);
        ((TextView) mAllHeaderView.findViewById(R.id.source_title_txt)).setText(R.string.all);
        ((TextView) mAllHeaderView.findViewById(R.id.source_item_num_txt)).setText("" + FeedDB.getInstance().countItemByRead(App.SOURCE_ID_ALL, false));
        mListView.addHeaderView(mAllHeaderView);

        mAdapter = new FeedAdapter();
        mListView.setAdapter(mAdapter);
        mPullRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.feed_pull_to_refresh_layout);
    }

    private void setListener() {
        mPullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FeedNetwork.getInstance().refreshAll();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    showItemList(App.SOURCE_ID_ALL);
                } else {
                    showItemList(((FeedSource) parent.getAdapter().getItem(position)).getId());
                }
                mListener.onSourceSelected(position);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                } else {
                    final FeedSource feedSource = (FeedSource) parent.getAdapter().getItem(position);
                    List<CharSequence> menuList = new ArrayList<>();
                    if (FeedDB.getInstance().countItemByRead(feedSource.getId(), false) != 0) {
                        menuList.add(getString(R.string.mark_as_read));
                    }
                    menuList.add(getString(R.string.remove_subscription));
                    new MaterialDialog.Builder(getActivity())
                            .title(feedSource.getTitle())
                            .items(menuList.toArray(new CharSequence[menuList.size()]))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i,
                                                        CharSequence charSequence) {
                                    switch (i) {
                                        case 0:
                                            FeedDB.getInstance().markAllAsRead(feedSource.getId());
                                            break;
                                        case 1:
                                            FeedDB.getInstance().deleteSource(feedSource.getId());
                                            break;
                                    }

                                    notifyDataSetChanged();
                                }
                            }).show();
                    return true;
                }
            }
        });
    }

    private class FeedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSourceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSourceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeedSource feedSource = mSourceList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.source_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.source_favicon_img);
                viewHolder.imageView.setErrorImageResId(R.drawable.ic_rss);
                viewHolder.imageView.setDefaultImageResId(R.drawable.ic_rss);
                viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.source_title_txt);
                viewHolder.numTextView = (TextView) convertView.findViewById(R.id.source_item_num_txt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.imageView.setImageUrl(feedSource.getFavicon(), VolleySingleton.getInstance().getImageLoader());
            viewHolder.titleTextView.setText(feedSource.getTitle());
            switch (App.getInstance().mCurrentMode) {
                case STAR:
                    viewHolder.numTextView.setText("" + FeedDB.getInstance().countItemByStar(
                            feedSource.getId(), true));
                    break;
                case UNREAD:
                    viewHolder.numTextView.setText("" + FeedDB.getInstance().countItemByRead(
                            feedSource.getId(), false));
                    break;
                case ALL:
                    viewHolder.numTextView.setText("" + feedSource.getFeedItems().size());
                    break;
                default:
            }
            return convertView;
        }

        private class ViewHolder {
            NetworkImageView imageView;
            TextView titleTextView;
            TextView numTextView;
        }
    }

    public void onEventMainThread(CommonEvent commonEvent) {
        switch (commonEvent) {
            case FEED_DB_UPDATED:
                LogUtil.i("feed db updated");
                notifyDataSetChanged();
                break;
            default:
        }
    }

    private void notifyDataSetChanged() {
        mSourceList = FeedDB.getInstance().loadAll();
        ((TextView) mAllHeaderView.findViewById(R.id.source_item_num_txt)).setText("" + FeedDB.getInstance().countItemByRead(App.SOURCE_ID_ALL, false));
        mAdapter.notifyDataSetChanged();
        mPullRefreshLayout.setRefreshing(false);
    }

    private void showItemList(long sourceId) {
        ItemListFragment fragment = (ItemListFragment) getFragmentManager().findFragmentById(R.id.details_frame);
        if (fragment == null || fragment.getShownSourceId() != sourceId) {
            fragment = ItemListFragment.newInstance(sourceId);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.details_frame, fragment);
            ft.commit();
        }
    }
}