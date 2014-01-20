package com.drcom.ui.View.controls.Ablum.cache;

import com.drcom.ui.View.controls.Ablum.util.FileManager;


public class FileCache extends AbstractFileCache {

	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {

		return FileManager.getSaveFilePath();
	}

}
