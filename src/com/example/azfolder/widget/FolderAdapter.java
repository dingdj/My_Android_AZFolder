/**
 * @author dingdj
 * Date:2014-3-4下午2:42:41
 *
 */
package com.example.azfolder.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author dingdj
 * Date:2014-3-4下午2:42:41
 *
 */
public abstract class FolderAdapter extends BaseAdapter{

	
	protected abstract void bindView(View convertView, int position, int itemViewType);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int i = getItemViewType(position);
	    if (convertView == null)
	    	convertView = newView(position, parent, i);
	    bindView(convertView, position, i);
	    return convertView;
	}
	
	protected abstract View newView(int position, ViewGroup parent, int itemViewType);

}
