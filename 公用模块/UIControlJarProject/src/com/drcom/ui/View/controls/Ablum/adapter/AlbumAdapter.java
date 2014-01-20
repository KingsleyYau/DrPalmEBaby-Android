package com.drcom.ui.View.controls.Ablum.adapter;

import java.util.List;

import com.drcom.ui.View.controls.Ablum.cache.ImageLoader;
import com.drcom.ui.View.controls.Ablum.cache.ImageLoader.BigImageCallback;
import com.drcom.ui.View.controls.Ablum.control.MyImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class AlbumAdapter extends BaseAdapter {
	public static final int FREE = 4;
	public static final int REFRESH = 5;
	private List<String> data;
	private Context context;
	private int no_pic;
	private ImageLoader mImageLoader;
	private boolean hasDown = false;
	protected int num;
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FREE:
				mImageLoader.loadBigPic(data.get(num), new BigImageCallback() {
					@Override
					public void imageLoaded(String bmp) {
						if (bmp.equals(data.get(num))) {
							Message msg = new Message();
							msg.what = REFRESH;// 当前图片下载完成，刷新UI
							mHandler.sendMessage(msg);
							Log.i("xpf", "下载完成");
						}
					}
				});
				break;
			case REFRESH:
				if (null != AlbumAdapter.this) {
					AlbumAdapter.this.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}

		};

	};

	public AlbumAdapter(Context context, List<String> data, String cachePath, int resid) {
		this.data = data;
		this.context = context;
		this.mImageLoader = new ImageLoader(cachePath);
		this.no_pic = resid;
	}

	public int getCount() {
		return this.data.size();
	}

	public Object getItem(int position) {
		return this.data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (position - 3 >= 0)
			this.mImageLoader.remove((String) this.data.get(position - 3));

		if (position + 2 < this.data.size()) {
			this.mImageLoader.remove((String) this.data.get(position + 2));
		}

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			RelativeLayout relativeLayout = new RelativeLayout(this.context);
			relativeLayout.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			MyImageView img = new MyImageView(this.context);
			Gallery.LayoutParams lpimg = new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			relativeLayout.addView(img, lpimg);
			vh.img = img;

			ProgressBar prb = new ProgressBar(this.context);
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			relativeLayout.addView(prb, lp1);
			vh.prb = prb;

			convertView = relativeLayout;
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Bitmap bitmap = this.mImageLoader.getBitmapFromCache((String) this.data.get(position));
		if (bitmap == null) {
			this.hasDown = false;
			vh.prb.setVisibility(0);
			vh.img.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), this.no_pic));
		} else {
			this.hasDown = true;
			vh.prb.setVisibility(8);
			vh.img.setImageBitmap(bitmap);
		}
		this.num = position;

		if (!(this.hasDown))
			GalleryWhetherStop();

		return convertView;
	}

	private void GalleryWhetherStop() {
		Log.i("xpf", "检测滑动是否停止，停止了就开始下载图片");
		Runnable runnable = new Runnable() {
			public void run() {
				int index;
				try {
					index = 0;
					index = num;
					Thread.sleep(1000L);
					if (index != num)
						return;
					Log.i("xpf", " 开始下载");
					Message msg = new Message();
					msg.what = FREE;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		new Thread(runnable).start();
	}

	public class ViewHolder {
		public MyImageView img;
		public ProgressBar prb;
	}
}