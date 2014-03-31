/**
 * @author dingdj
 * Date:2014-3-4下午2:48:15
 *
 */
package com.example.azfolder.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.azfolder.R;

import com.example.azfolder.widget.AZFolder.FolderDataModel;

/**
 * @author dingdj Date:2014-3-4下午2:48:15
 * 
 */
public class AZFolderAdapter extends FolderAdapter {

	private Context mContext;
	private int mCount;
	private FolderDataModel[] mDataArray;
	//private int mGridViewType;
	private LayoutInflater mInflater;
	//private int mItemResId;
	//private Map<Long, Boolean> mSelectMap = new HashMap();

	public AZFolderAdapter(Context paramContext) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.mContext = paramContext;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public Object getItem(int position) {
		if (mDataArray == null)
			return null;
		return mDataArray[position];
	}

	@Override
	public long getItemId(int position) {
		return 0L;
	}

	/**
	 * 设置数据集
	 * 
	 * @author dingdj Date:2014-3-4下午2:54:13
	 * @param paramArrayList
	 */
	public void setDataList(FolderDataModel[] datas) {
		mDataArray = datas;

		if ((mDataArray == null) || (mDataArray.length == 0))
			return;
		mCount = mDataArray.length;

		/*if (mSelectMap != null)
			mSelectMap.clear();
*/
		notifyDataSetChanged();
		return;
	}

	@Override
	protected void bindView(View convertView, int position, int itemViewType) {
		convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.clear_color));
	    AZFolderAdapter.ViewHolder localViewHolder = (AZFolderAdapter.ViewHolder)convertView.getTag();
	    localViewHolder.clickView.setVisibility(View.INVISIBLE);
	    FolderDataModel dataModel = (FolderDataModel)getItem(position);
		if (dataModel != null) {
			localViewHolder.textView.setText(dataModel.getTitle());
			localViewHolder.textView.setTextColor(this.mContext.getResources()
					.getColor(R.color.az_folder_name_color));
			localViewHolder.imageView.setImageDrawable(dataModel.getIcon());
		}
	}

	@Override
	protected View newView(int position, ViewGroup parent, int itemViewType) {
		return newViewOfNormalType(position, parent, itemViewType);
	}

	private View newViewOfNormalType(int position, ViewGroup parent,
			int itemViewType) {
		View localView = this.mInflater.inflate(R.layout.az_folder_item,
				parent, false);
		AZFolderAdapter.ViewHolder localViewHolder = new AZFolderAdapter.ViewHolder();
		localViewHolder.textView = ((TextView) localView
				.findViewById(R.id.az_item_text));
		localViewHolder.imageView = ((ImageView) localView
				.findViewById(R.id.az_item_img));
		localViewHolder.clickView = ((ImageView) localView
				.findViewById(R.id.az_clickring));
		localViewHolder.clickView.setVisibility(View.INVISIBLE);
		localView.setTag(localViewHolder);
		return localView;
	}

	class ViewHolder {
		public ImageView clickView;
		public ImageView imageView;
		public TextView textView;
	}

}
