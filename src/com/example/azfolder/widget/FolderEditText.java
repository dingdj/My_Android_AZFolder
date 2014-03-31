/**
 * @author dingdj
 * Date:2014-3-4上午11:37:00
 *
 */
package com.example.azfolder.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author dingdj Date:2014-3-4上午11:37:00
 * 
 */
public class FolderEditText extends EditText implements
		View.OnFocusChangeListener, View.OnTouchListener {

	public FolderEditText(Context paramContext) {
		super(paramContext);
		init();
	}

	public FolderEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init();
	}

	public FolderEditText(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init();
	}

	private void init() {
		setClearIconVisible(false);
		setBackgroundDrawable(null);
		setHighlightColor(Color.WHITE);
		super.setOnTouchListener(this);
		super.setOnFocusChangeListener(this);
	}
	
	protected void setClearIconVisible(boolean paramBoolean)
	  {
	    if (!paramBoolean)
	    {
	      setTextColor(-1);
	      return;
	    }
	    setTextColor(-16043969);
	  }


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	}
}
