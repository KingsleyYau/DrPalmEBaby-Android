package com.drcom.ui.View.controls.Ablum.cache;

import java.io.File;

import com.drcom.ui.View.controls.Ablum.util.FileHelper;

import android.util.Log;


public abstract class AbstractFileCache {

	private String dirString;

	public AbstractFileCache() {

		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
		Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}

	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}

	public abstract String getSavePath(String url);

	public abstract String getCacheDir();

	public void clear() {
		dirString = getCacheDir();
		Log.i("xpf", "yu dir delete =" + dirString);
		FileHelper.deleteDirectory(dirString);
	}

}
