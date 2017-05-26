package org.com.gannan.farminginfoplatform.baseadapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 16/11/16.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    public List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemChildViewClickListener mOnItemChildViewClickListener;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager())
            return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }

    public void addItem(int position) {
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        setListener(parent, holder, viewType);
        return holder;
    }


    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });

//        viewHolder.getView(R.id.relativeLayout1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemChildViewClickListener != null) {
//                    int position = viewHolder.getAdapterPosition();
//                    mOnItemChildViewClickListener.onItemChildViewClick(v, viewHolder, position);
//                }
//            }
//        });
//        viewHolder.getView(R.id.relativeLayout1).setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (mOnItemChildViewClickListener != null) {
//                    int position = viewHolder.getAdapterPosition();
//                    mOnItemChildViewClickListener.onItemChildViewLongClick(v, viewHolder, position);
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemChildViewClickListener{
        void onItemChildViewClick(View view, RecyclerView.ViewHolder holder, int position);
        boolean onItemChildViewLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }
    public void setOnItemChildViewClickListener(OnItemChildViewClickListener mOnItemChildViewClickListener) {
        this.mOnItemChildViewClickListener = mOnItemChildViewClickListener;
    }
}
