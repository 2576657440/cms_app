package org.com.gannan.farminginfoplatform.baseadapter.base;


/**
 * Created by Administrator on 16/11/16.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    int forViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
