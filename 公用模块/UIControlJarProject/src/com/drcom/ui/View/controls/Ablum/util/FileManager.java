package com.drcom.ui.View.controls.Ablum.util;


public class FileManager {
	private static String filePath = "files";

	public static String getSaveFilePath() {
		String dirPath = CommonUtil.getRootFilePath() + "EbabyAlbum/";
		if (CommonUtil.hasSDCard()) {
			dirPath = CommonUtil.getRootFilePath() + "EbabyAlbum/" + filePath + "/";
			FileHelper.createDirectory(dirPath);
			return dirPath;
		} else {
			dirPath = CommonUtil.getRootFilePath() + "EbabyAlbum/" + filePath;
			FileHelper.createDirectory(dirPath);
			return dirPath;
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		FileManager.filePath = filePath;
	}
}
