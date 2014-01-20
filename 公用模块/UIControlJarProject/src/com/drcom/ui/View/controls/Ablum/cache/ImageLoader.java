package com.drcom.ui.View.controls.Ablum.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.drcom.ui.View.controls.Ablum.util.FileManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;


public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private AbstractFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// �̳߳�
	private ExecutorService executorService;
	private ExecutorService bigImageExecutorService;
	private FileCallback fileCallback;
	private Set<String> urls = new HashSet<String>();
	private String albumsavepath = "downloadFile";

	public ImageLoader(String filePath) {
		FileManager fm = new FileManager();
		fm.setFilePath(filePath);
		fileCache = new FileCache();
		executorService = Executors.newFixedThreadPool(5);
		bigImageExecutorService = Executors.newFixedThreadPool(5);
	}

	// ����СͼƬ
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
		if (isPic(url)) {// ����url�ж��ǲ���ͼƬ
			imageViews.put(imageView, url);
			// �ȴ��ڴ滺���в���
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else if (!isLoadOnlyFromCache) {
				// ��û�еĻ��������̼߳���ͼƬ
				queuePhoto(url, imageView);
			}
		}
	}

	// �����ļ������ص��ӿ�
	public void loadFile(String url, boolean isLoadOnlyFromCache, String fileName, FileCallback fileCallback) {
		if (url != null) {
			this.fileCallback = fileCallback;
			String fileName1 = url;
			if (url.contains("/") && url.lastIndexOf("/") + 1 < url.length()) {
				fileName1 = url.substring(url.lastIndexOf("/") + 1, url.length());
			} else if (url.lastIndexOf("/") + 1 == url.length()) {
				fileName1 = url;
			}
			// �ȴ��ڴ滺���в���
			if (!isLoadOnlyFromCache) {
				// ��û�еĻ��������̼߳���ͼƬ
				executorService.submit(new QueueFile(url, fileName1));
			}
		}
	}

	// ���ش�ͼƬ �����ص��ӿ�
	public void loadBigPic(String url, BigImageCallback bigImageCallback) {
		if (url != null) {
			// �ȴ��ڴ滺���в���
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				bigImageCallback.imageLoaded(url);
			} else {
				// ���ļ������ж�ȡ
				// ��û�еĻ��������̼߳���ͼƬ
				bigImageExecutorService.submit(new LoadPic(url, bigImageCallback));
			}
		}
	}

	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = memoryCache.get(url);
		if (null == bitmap) {
			bitmap = readCacheFromDisk(url);
		}
		return bitmap;
	}

	class QueueFile implements Runnable {
		String url;
		String fileName;

		QueueFile(String url, String fileName) {
			this.url = url;
			this.fileName = fileName;
		}

		QueueFile(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			File f = fileCache.getFile(url);

			// �ȴ��ļ������в����Ƿ���
			boolean b = false;
			if (f.exists()) {
				b = copyFile(f, fileName);
			}

			if (b && fileCallback != null) {
				fileCallback.fileLoaded(b);
				return;
			}
			if (urls.contains(url)) {
				// �����ض����У�������
				return;
			} else {
				// û�����أ���ӵ����ض���
				urls.add(url);
			}
			// ����ָ����url������ͼƬ
			try {
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				if (f != null && f.exists()) {
					b = copyFile(f, fileName);
				}

				if (b && fileCallback != null) {
					fileCallback.fileLoaded(b);
					urls.remove(url);
					return;
				}
				return;
			} catch (Exception ex) {
				urls.remove(url);
			}

		}

	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// ���µĲ�������UI�߳���
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	// ����ͼƬ�����浽cache��
	class LoadPic implements Runnable {
		String url;
		BigImageCallback bigImageCallback;

		LoadPic(String url, BigImageCallback bigImageCallback) {
			this.url = url;
			this.bigImageCallback = bigImageCallback;
		}

		@Override
		public void run() {
			File f = fileCache.getFile(url);
			// �ȴ��ļ������в����Ƿ���
			Bitmap b = null;
			if (f != null && f.exists()) {
				b = decodeBigFile(f);
			}
			if (b != null) {
				memoryCache.put(url, b);
				bigImageCallback.imageLoaded(url);
				return;
			}
			if (urls.contains(url)) {
				// �����ض����У�������
				return;
			} else {
				// û�����أ���ӵ����ض���
				urls.add(url);
			}
			// ����ָ����url������ͼƬ
			try {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				Log.i("xpf", "loadPic url=" + url);
				bitmap = decodeBigFile(f);
				if (bitmap != null) {
					memoryCache.put(url, bitmap);
					bigImageCallback.imageLoaded(url);
					urls.remove(url);
					return;
				}
			} catch (Exception ex) {
				// memoryCache.put(url, GlobalVariables.wrong_pic);
				// bigImageCallback.imageLoaded(url);
				urls.remove(url);
			}
		}
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// �ȴ��ļ������в����Ƿ���
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = decodeFile(f);
		}
		if (b != null) {
			return b;
		}
		// ����ָ����url������ͼƬ
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			// return GlobalVariables.wrong_pic;
			return null;
		}
	}

	// decode���ͼƬ���Ұ����������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����СҲ�������Ƶ�
	private Bitmap decodeFile(File f) {
		// decode image size
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(f.getAbsolutePath(), o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			Log.i("xpf", "scale" + scale);
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// decode���ͼƬ���Ұ����������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����СҲ�������Ƶ�
	private Bitmap decodeBigFile(File f) {
		// decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(f.getAbsolutePath(), o);

		// Find the correct scale value. It should be the power of 2.

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		Log.i("xpf", "scale Big width_tmp " + width_tmp + "  scale outHeight " + height_tmp);
		long limit = Runtime.getRuntime().maxMemory() / 16;
		int scale = 1;
		while (true) {
			if (width_tmp * height_tmp * 4 < limit)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		o.inJustDecodeBounds = false;
		o.inSampleSize = scale;
		o.inPurgeable = true;
		Log.i("xpf", "scale=" + scale);
		return BitmapFactory.decodeFile(f.getAbsolutePath(), o);
		// }
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	/**
	 * ��ֹͼƬ��λ
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// ������UI�߳��и��½���
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);

			}

		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
		imageViews.clear();
		urls.clear();
	}

	public void clearMemoryCache() {
		memoryCache.clear();
		imageViews.clear();
		urls.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}

	// ����翪�ŵĻص��ӿ�
	public interface BigImageCallback {
		// ע�� �˷�������������Ŀ������ͼ����Դ
		public void imageLoaded(String bmp);
	}

	// ����翪�ŵĻص��ӿ�,����bytes;
	public interface FileCallback {
		// ע�� �˷�������������Ŀ������ͼ����Դ
		public void fileLoaded(boolean isDone);
	}

	// �����ļ�
	public boolean copyFile(File sourceFile, String fileName) {
		Log.i("xpf", "scale ����ͼ");
		// File dirFile = new File(GlobalVariables.ALBUM_SAVEDIRECT_PATH);
		File dirFile = new File(albumsavepath);
		if (!dirFile.exists())
			dirFile.mkdirs();
		File imgFile = new File(dirFile, fileName);
		if (imgFile.exists() && imgFile.canRead() && imgFile.length() > 0) {
			// �Ѿ����˾Ͳ���������
			return true;
		}
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ����������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ���������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(imgFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// �ر���
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public void remove(String url) {
		if (url != null) {
			memoryCache.remove(url);
		}
	}

	public boolean isPic(String fileName) {
		boolean flag = false;
		if (fileName == null)
			return false;
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("jpg".equalsIgnoreCase(fileExt) || "png".equalsIgnoreCase(fileExt) || "gif".equalsIgnoreCase(fileExt) || "jpeg".equalsIgnoreCase(fileExt)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	// ��SD����ȡͼƬ
	protected Bitmap readCacheFromDisk(String key) {
		File file = fileCache.getFile(key);
		if (!file.exists() || !file.canRead()) {
			Log.i("xpf", "has file not exsit!");
			// û�ҵ������Ӧ���ļ�
			return null;
		} else {
			Log.i("xpf", "has file  exsit^^");
			return decodeBigFile(file);
		}
	}
}