package com.example.azfolder.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.azfolder.MainActivity;
import com.example.azfolder.R;

/**
 * @author dingdj Date:2014-3-4上午10:27:18
 * 
 */
public class AZFolder extends LinearLayout implements
		View.OnFocusChangeListener, View.OnTouchListener,
		AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
		AdapterView.OnItemLongClickListener {

	public static char[] charArray = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
			77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 35 };
	public static int[] idList = { R.id.az_zimulist_A, R.id.az_zimulist_B,
			R.id.az_zimulist_C, R.id.az_zimulist_D, R.id.az_zimulist_E,
			R.id.az_zimulist_F, R.id.az_zimulist_G, R.id.az_zimulist_H,
			R.id.az_zimulist_I, R.id.az_zimulist_J, R.id.az_zimulist_K,
			R.id.az_zimulist_L, R.id.az_zimulist_M, R.id.az_zimulist_N,
			R.id.az_zimulist_O, R.id.az_zimulist_P, R.id.az_zimulist_Q,
			R.id.az_zimulist_R, R.id.az_zimulist_S, R.id.az_zimulist_T,
			R.id.az_zimulist_U, R.id.az_zimulist_V, R.id.az_zimulist_W,
			R.id.az_zimulist_X, R.id.az_zimulist_Y, R.id.az_zimulist_Z,
			R.id.az_zimulist_other };
	public static int[] idframeList = { R.id.az_zimulist_frame_A,
			R.id.az_zimulist_frame_B, R.id.az_zimulist_frame_C,
			R.id.az_zimulist_frame_D, R.id.az_zimulist_frame_E,
			R.id.az_zimulist_frame_F, R.id.az_zimulist_frame_G,
			R.id.az_zimulist_frame_H, R.id.az_zimulist_frame_I,
			R.id.az_zimulist_frame_J, R.id.az_zimulist_frame_K,
			R.id.az_zimulist_frame_L, R.id.az_zimulist_frame_M,
			R.id.az_zimulist_frame_N, R.id.az_zimulist_frame_O,
			R.id.az_zimulist_frame_P, R.id.az_zimulist_frame_Q,
			R.id.az_zimulist_frame_R, R.id.az_zimulist_frame_S,
			R.id.az_zimulist_frame_T, R.id.az_zimulist_frame_U,
			R.id.az_zimulist_frame_V, R.id.az_zimulist_frame_W,
			R.id.az_zimulist_frame_X, R.id.az_zimulist_frame_Y,
			R.id.az_zimulist_frame_Z, R.id.az_zimulist_frame_other };

	private FolderDataModel[] appList;

	private AZFolderAdapter mAzFolderAdapter = new AZFolderAdapter(getContext());
	private ArrayList<String> curZimuIndex = new ArrayList<String>();
	protected GridView mContentGridView;
	protected TextView mContentTextView;
	View zimulistView;
	private int mPreSelectZiMuIndex;
	private int mCurSelectStartPos;
	private int mCurSelectLength;
	
	private boolean mIngoreHeadChange;
	private DelayTouchEvent delayTouchEvent = new DelayTouchEvent();
	private Handler handler = new Handler();

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AZFolder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AZFolder(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public AZFolder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	/**
	 * 开始绑定数据并显示
	 * 
	 * @author dingdj Date:2014-3-4下午2:36:17
	 */
	public void bindData(FolderDataModel[] models) {
		mContentGridView = (GridView) findViewById(R.id.az_custom_grid);
		this.mContentGridView.setOnItemClickListener(this);
		this.mContentGridView.setOnItemLongClickListener(this);
		this.mContentGridView.setOnScrollListener(this);
		this.mContentGridView.setAdapter(this.mAzFolderAdapter);
		this.zimulistView = findViewById(R.id.zimulist);
		this.zimulistView.setOnTouchListener(this);
		//填充数据
		sortItemArray(models);
		refreshZimuList();
		getBackground().setAlpha(170);
		this.mContentGridView.setSelection(0);
	}
	
	/**
	 * 对数据进行排序 并初始化CurZimu
	 * @author dingdj
	 * Date:2014-3-4下午4:38:03
	 *  @param models
	 *  @return
	 */
	private boolean sortItemArray(FolderDataModel[] models) {
		if (models == null || models.length == 0) {
			return false;
		}

		int model_size = models.length;
		this.appList = models;

		Arrays.sort(appList, 0, model_size, new FolderDataModel());
		//long curTimeMillis = System.currentTimeMillis();
		countCurZimuIndex(this.appList, model_size);
		//Log.e(AZFolderActivity.TAG, (System.currentTimeMillis() - curTimeMillis)+"");
		this.mAzFolderAdapter.setDataList(appList);
		return true;
	}
	
	/**
	 * 计算当前的应用包含的所有的字母
	 * @author dingdj
	 * Date:2014-3-4下午4:36:56
	 *  @param folderModels
	 *  @param size
	 */
	private void countCurZimuIndex(FolderDataModel[] folderModels, int size) {
		if ((this.curZimuIndex == null) || (folderModels == null))
			return;
		this.curZimuIndex.clear();
		char c = '#';
		boolean[] exists = new boolean[charArray.length];
		for (int i = 0; i < size; i++) {
			// 默认返回的是小写字母对应的int
			int j = folderModels[i].charToInt;
			if ((j > 90) || (j < 65)) {
				c = '#';
				if(!exists[charArray.length-1]){
					exists[charArray.length-1] = true;
					this.curZimuIndex.add(String.valueOf(c));
				}
			} else {
				c = (char) j;
				if(!exists[j-65]){
					exists[j-65] = true;
					this.curZimuIndex.add(String.valueOf(c));
				}
				
			}
			/*if (!checkContainSpecialZimu(c))
				this.curZimuIndex.add(String.valueOf(c));*/
		}
		
		
		
	}

	/**
	 * 刷新字母列表 判断是否有当前可见的字母
	 * 
	 * @author dingdj Date:2014-3-4下午3:29:05
	 */
	private void refreshZimuList() {
		if ((this.curZimuIndex == null) || (this.curZimuIndex.size() <= 0))
			return;
		for (int i = 0; i < charArray.length; i++) {
			if (checkContainSpecialZimu(charArray[i])) {
				setZimuListVisible(i, true);
			} else {
				setZimuListVisible(i, false);
			}
		}
	}

	/**
	 * 判断当前字母列表是否有包含特定字母
	 * @author dingdj
	 * Date:2014-3-4下午3:31:23
	 *  @param zimu
	 *  @return
	 */
	private boolean checkContainSpecialZimu(char zimu) {
		if ((this.curZimuIndex == null) || (this.curZimuIndex.size() <= 0))
			return false;
		for (String curZimu : curZimuIndex) {
			if(curZimu.equals(String.valueOf(zimu))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置可见性
	 * @author dingdj
	 * Date:2014-3-4下午3:46:10
	 *  @param ziMuPosition
	 *  @param visiable
	 */
	private void setZimuListVisible(int ziMuPosition, boolean visiable) {
		if (ziMuPosition >= idframeList.length) {
			Log.e(MainActivity.TAG, "ziMuPosition >= idframeList.length");
			return;
		}
		int viewId = idframeList[ziMuPosition];
		View localView = findViewById(viewId);
		if (visiable) {
			localView.setVisibility(View.VISIBLE);
		} else {
			localView.setVisibility(View.GONE);
		}
	}

	// 数据模型
	public static class FolderDataModel implements Comparator<FolderDataModel>{
		private Drawable icon;
		private String title;
		private int charToInt;
		private String pkgName;
		private String className;

		public Drawable getIcon() {
			return icon;
		}

		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getCharIndex() {
			return charToInt;
		}

		public void setCharIndex(int charIndex) {
			this.charToInt = charIndex;
		}
		
		public String getPkgName() {
			return pkgName;
		}

		public void setPkgName(String pkgName) {
			this.pkgName = pkgName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		@Override
		public int compare(FolderDataModel lhs, FolderDataModel rhs) {
			if(lhs.charToInt == rhs.charToInt){
				return 0;
			}
			if(lhs.charToInt == 35){
				return 1;
			}
			
			if(rhs.charToInt == 35){
				return -1;
			}
			if(lhs.charToInt > rhs.charToInt){
				return 1;
			}else if(lhs.charToInt < rhs.charToInt){
				return -1;
			}
			return 0;
		}
		
		public Intent getIntent(){
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(pkgName,className));
			return intent;
		}
		
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if ((this.appList != null) && (this.appList.length > position)
				&& (position >= 0)) {
			FolderDataModel dataModel = this.appList[position];
			try{
				view.getContext().startActivity(dataModel.getIntent());
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(this.mIngoreHeadChange){
			return;
		}
		setZimuListSelect(findSelChar(getScrollToIndexByPos(firstVisibleItem)));
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.e(MainActivity.TAG, "onTouch");
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			removeGridViewHighLightState();
			if(v == this.zimulistView){
				checkTouchAZCharList(event.getY());
				handler.removeCallbacks(delayTouchEvent);
				handler.postDelayed(delayTouchEvent, 300L);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(v == this.zimulistView){
				checkTouchAZCharList(event.getY());
				handler.removeCallbacks(delayTouchEvent);
				handler.postDelayed(delayTouchEvent, 300L);
			}
			break;

		case MotionEvent.ACTION_UP:
			if(v == this.zimulistView){
				setGridViewHighLightColor();
			}
		default:
			break;
		}
		return true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	}
	
	/**
	 * 设置高亮状态
	 * @author dingdj
	 * Date:2014-3-5上午10:34:31
	 */
	private void setGridViewHighLightColor() {
		int childCounts = this.mContentGridView.getChildCount();
		int firstVisiablePosition = this.mContentGridView
				.getFirstVisiblePosition();
		
		int startPosition = mCurSelectStartPos - firstVisiablePosition;
		int endPosition = startPosition + mCurSelectLength + 1;
		Log.e(MainActivity.TAG, "startPosition:"+startPosition + " endPosition:"+ endPosition);
		if (endPosition >= childCounts) {
			endPosition = childCounts;
		}
		for (int i = startPosition; i < endPosition; i++) {
			View localView = this.mContentGridView.getChildAt(i);
			if (localView != null) {
				AZFolderAdapter.ViewHolder localViewHolder = (AZFolderAdapter.ViewHolder) localView
						.getTag();
				if ((localViewHolder != null)&& (localViewHolder.textView != null))
					localViewHolder.textView.setTextColor(getResources()
							.getColor(R.color.az_folder_item_tips_fgcolor));
			}
		}
	}
	
	/**
	 * 遍历清除所有的高亮状态
	 * @author dingdj
	 * Date:2014-3-4下午4:59:43
	 */
	private void removeGridViewHighLightState() {
		if (this.mContentGridView == null)
			return;
		int gridViewChildNum = this.mContentGridView.getChildCount();
		for (int i = 0; i < gridViewChildNum; i++) {
			View localView = this.mContentGridView.getChildAt(i);
			if (localView == null) {
				continue;
			}
			if ((localView.getTag() instanceof AZFolderAdapter.ViewHolder)) {
				AZFolderAdapter.ViewHolder localViewHolder = (AZFolderAdapter.ViewHolder) localView
						.getTag();
				if (localViewHolder != null)
					localViewHolder.textView.setTextColor(getResources()
							.getColor(R.color.az_folder_name_color));
			}
		}
	}
	
	/**
	 * 触摸字母时的响应
	 * @author dingdj
	 * Date:2014-3-4下午5:50:31
	 *  @param yPosition
	 *  @return
	 */
	private boolean checkTouchAZCharList(float yPosition) {
		this.mIngoreHeadChange = true;
		int curTouchedZiMuIndex = findSelectZIMUIndex(yPosition);
		if (curTouchedZiMuIndex < this.curZimuIndex.size()) {
			char c = ((String) this.curZimuIndex.get(curTouchedZiMuIndex))
					.charAt(0);
			if ('#' == c) {
				c = 35;
			}
			Log.e(MainActivity.TAG, String.valueOf(c));
			int[] startEndPosition = GetScrollPosAndLenghtByIds(c);
			if(startEndPosition[1] == -1){
				setCurSelectStartAndLength(startEndPosition[0], 0);
			}else{
				setCurSelectStartAndLength(startEndPosition[0], startEndPosition[1]-startEndPosition[0]);
			}
			
			this.mContentGridView.setSelection(startEndPosition[0]);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 设置当前被选中的当前位置 以及对应的长度
	 * @author dingdj
	 * Date:2014-3-5上午10:08:23
	 *  @param curSelectPos
	 *  @param curSelectLength
	 */
	private void setCurSelectStartAndLength(int startSelectPos,
			int mCurSelectLength) {
		this.mCurSelectStartPos = startSelectPos;
		this.mCurSelectLength = mCurSelectLength;
	}
	
	/**
	 * 通过字母来获取对应数据队列中的开始和结束位置
	 * @author dingdj
	 * Date:2014-3-5上午10:01:51
	 * @param char2Int
	 * @return
	 */
	private int[] GetScrollPosAndLenghtByIds(int char2Int) {
		int[] arrayOfInt = { -1, -1 };
		for (int i = 0; i < appList.length; i++) {
			FolderDataModel model = appList[i];
			if (char2Int == model.charToInt) {
				if (arrayOfInt[0] == -1) {
					arrayOfInt[0] = i;
				} else {
					arrayOfInt[1] = i;
				}
			}
			if(arrayOfInt[0] != -1 && char2Int != model.charToInt){
				break;
			}
		}
		return arrayOfInt;
	}
	
	/**
	 * 通过touch的yposition来定位字母
	 * @author dingdj
	 * Date:2014-3-4下午5:14:43
	 *  @param yPosition
	 *  @return
	 */
	private int findSelectZIMUIndex(float yPosition) {
		if ((this.curZimuIndex != null) && (this.curZimuIndex.size() > 0)) {
			int padBottom = (int) getResources().getDimension(
					R.dimen.az_gridview_content_pad_bottom);
			int height = this.zimulistView.getHeight() - padBottom;
			float curTouchedZiMuIndex;

			int curZimuNum = this.curZimuIndex.size();
			/**
			 * float oneZiMuHeight = height/curZimuNum; 每个字母的高度
			 * yPosition/oneZiMuHeight = yPosition * curZimuNum / height;
			 * //算出点击了第几个字母
			 */
			curTouchedZiMuIndex = yPosition * curZimuNum / height;
			if (curTouchedZiMuIndex > 0.0f) {
				if (curTouchedZiMuIndex >= 26) {
					curTouchedZiMuIndex = 26 - 1;
				}
				setZimuListSelect((int)curTouchedZiMuIndex);
				return (int) curTouchedZiMuIndex;
			}
		}
		return 0;
	}
	
	/**
	 * 点击字母时变色处理
	 * @author dingdj
	 * Date:2014-3-4下午5:46:33
	 * @param positionInCurZimuIndex
	 */
	private void setZimuListSelect(int positionInCurZimuIndex)
	  {
	    int id = findSelIndexId(positionInCurZimuIndex);
	    if (this.mPreSelectZiMuIndex >= 0)
	    {
	      TextView view = (TextView)findViewById(this.mPreSelectZiMuIndex);
	      if (view != null)
	    	  view.setBackgroundColor(getResources().getColor(R.color.clear_color));
	    }
	    if (positionInCurZimuIndex >= 0)
	    {
	      TextView view = (TextView)findViewById(id);
	      if (view != null)
	    	  view.setBackgroundDrawable(getResources().getDrawable(R.drawable.folder_az_bg));
	      this.mPreSelectZiMuIndex = id;
	    }
	  }
	
	
	/**
	 * 根据位置找到ID
	 * @author dingdj
	 * Date:2014-3-4下午5:40:42
	 *  @param index
	 *  @return
	 */
	private int findSelIndexId(int index) {
		if (index < 0) {
			return R.id.az_zimulist_A;
		}
		if ((this.curZimuIndex != null) && (this.curZimuIndex.size() > 0)) {
			if (index >= this.curZimuIndex.size())
				index = this.curZimuIndex.size() - 1;
			String str = (String) this.curZimuIndex.get(index);
			for (int i = 0; i < charArray.length; i++) {
				if (str.equals(String.valueOf(charArray[i]))) {
					int id = idList[i];
					return id;
				}
			}
		}
		return R.id.az_zimulist_A;
	}
	
	/**
	 * 获取这个字符在curZimuIndex中的索引
	 * @author dingdj
	 * Date:2014-3-6上午9:13:42
	 *  @param c
	 *  @return
	 */
	private int findSelChar(char c) {
		int i = this.curZimuIndex.size();
		for (int j = 0; j < i; j++)
			if (((String) this.curZimuIndex.get(j)).equals(String.valueOf(c)))
				return j;
		return 0;
	}
	
	/**
	 * 获得第一个可见元素的字母
	 * @author dingdj
	 * Date:2014-3-6上午9:12:01
	 *  @param firstVisibleItem
	 *  @return
	 */
	private char getScrollToIndexByPos(int firstVisibleItem) {
		char c = 65;
		if(this.appList != null){
			int i = this.appList.length;
			
			if ((firstVisibleItem > 0) && (firstVisibleItem < i))
				c = (char) this.appList[firstVisibleItem].charToInt;
		}
		return c;
	}

	public void setAppList(FolderDataModel[] appList) {
		this.appList = appList;
	}
	
	protected void delayTouch() {
		this.mIngoreHeadChange = false;
	}
	
	class DelayTouchEvent implements Runnable{

		@Override
		public void run() {
			delayTouch();
		}
		
	}
	


}
