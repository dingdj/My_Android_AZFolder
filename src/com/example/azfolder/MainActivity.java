package com.example.azfolder;

import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import com.example.azfolder.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.example.azfolder.widget.AZFolder;
import com.example.azfolder.widget.AZFolder.FolderDataModel;

public class MainActivity extends Activity {

	public static final String TAG = "AZFolderActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.az_folder);
		AZFolder folder = (AZFolder)findViewById(R.id.azcontent);
		folder.bindData(fillData());
	}
    
    /**
     * 填充数据
     * @author dingdj
     * Date:2014-3-5上午11:10:59
     */
    private FolderDataModel[] fillData(){
    	Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
        FolderDataModel[] folderDataModels = new FolderDataModel[mApps.size()];
    	PackageManager pm = getPackageManager();
    	int i = 0;
    	for (ResolveInfo resolveInfo : mApps) {
    		folderDataModels[i] = new FolderDataModel();
    		folderDataModels[i].setIcon(resolveInfo.loadIcon(pm));
    		folderDataModels[i].setClassName(resolveInfo.activityInfo.name); // 获得该应用程序的启动Activity的name
    		folderDataModels[i].setPkgName(resolveInfo.activityInfo.packageName);
    		
    		String lable = resolveInfo.loadLabel(pm).toString();
    		folderDataModels[i].setTitle(lable);
    		
    		char c = paiXu(lable);
    		if(c == '#'){
    			folderDataModels[i].setCharIndex(35);
    		}else{
    			folderDataModels[i].setCharIndex(paiXu(lable));
    		}
    		i++;
		}
    	return folderDataModels;
    }
	
	
	public static char paiXu(String lable){
		lable = lable.trim();
		String alphabet = lable.substring(0, 1);  
        /*判断首字符是否为中文，如果是中文便将首字符拼音的首字母和&符号加在字符串前面*/
        if (alphabet.matches("[\\u4e00-\\u9fa5]+")) { 
			 String rtn =  getAlphabet(alphabet);
			 return rtn.charAt(0);
        }else{
        	alphabet = alphabet.toUpperCase();
        	char c = alphabet.charAt(0);
        	if(c>='A'&&c<='Z'){
        		return c;
        	}else{
        		return '#';
        	}
        }
	}
	
	public static String getAlphabet(String str) {
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出拼音全部大写
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		// 不带声调
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String pinyin = null;
		try {
			pinyin = (String) PinyinHelper.toHanyuPinyinStringArray(
					str.charAt(0), defaultFormat)[0];
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinyin.substring(0, 1);
	}

}
