package com.drcom.ui.View.controls.Ablum.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * �������ImageView (XMLҪ����������Բ������Ų���:android:scaleType="matrix")
 * 
 * @author zhaojunjie
 * 
 */
public class MyImageView extends ImageView {

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();// ��׿�б�ʾ����࣬��X,Y����ֵ
	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private boolean isFirst = false;
	private float scale;
	private float tempWidth, tempHeight, imageWidth, imageHeight;
	protected AlphaAnimation animation;

	private float W, H, bw, bh, mscale = 1f;
	private float[] mValues = new float[9];
	private Matrix lastMatrix = new Matrix();
	private boolean toRight = false, toLeft = false, toTop = false, toBottom = false;
	private int maxMultiple = 4;
	private float[] mSmallValues = new float[9];
	private boolean isClickTwo = false;
	private float dx, dy;
	private float scale1, scale2, scale3, scale4;
	private boolean isSetBitmap = false;

	public MyImageView(Context context) {
		super(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i("xpf", "MM MyIamgeView onMeasure.w=" + getMeasuredWidth() + "  h=" + getMeasuredHeight());
		if ((!(this.isSetBitmap)) && (getMeasuredWidth() != 0) && (getMeasuredHeight() != 0)) {
			this.isSetBitmap = true;
			initData();
		}
	}

	public void setImageBitmap(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
		this.bw = bitmap.getWidth();
		this.bh = bitmap.getHeight();
	}

	private void initData() {
		int i;
		setScaleType(ImageView.ScaleType.MATRIX);
		Log.i("xpf", "MM MyIamgeView initData .w=" + getMeasuredWidth() + "  h=" + getMeasuredHeight());
		setImageMatrix(this.matrix);

		this.W = getMeasuredWidth();
		this.H = getMeasuredHeight();
		this.scale1 = (this.bh / this.bw);
		this.scale2 = (this.H / this.W);
		this.scale3 = (this.W / this.bw);
		this.scale4 = (this.H / this.bh);
		float trans1 = this.H / 2.0F - this.bh * this.scale3 / 2.0F;
		float trans2 = this.W / 2.0F - this.bw * this.scale4 / 2.0F;

		Log.i("xpf", "bw=" + this.bw);
		Log.i("xpf", "W=" + this.W);
		if (this.scale1 < this.scale2) {
			this.matrix.setScale(this.scale3, this.scale3);
			this.matrix.postTranslate(0.0F, trans1);
			setImageMatrix(this.matrix);

			this.matrix.getValues(this.mValues);
			this.mscale = this.scale3;
			for (i = 0; i < this.mSmallValues.length; ++i)
				this.mSmallValues[i] = this.mValues[i];

			this.mSmallValues[0] /= 2.0F;
			this.mSmallValues[4] /= 2.0F;
		} else {
			this.matrix.setScale(this.scale4, this.scale4);
			this.matrix.postTranslate(trans2, 0.0F);
			setImageMatrix(this.matrix);

			this.matrix.getValues(this.mValues);
			this.mscale = this.scale4;
			for (i = 0; i < this.mSmallValues.length; ++i)
				this.mSmallValues[i] = this.mValues[i];

			this.mSmallValues[0] /= 2.0F;
			this.mSmallValues[4] /= 2.0F;
		}
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * �Ƿ��ƶ������ұ���
	 * 
	 * @return
	 */
	public boolean isMoveToRight() {
		return toRight;
	}

	/**
	 * �Ƿ��ƶ����������
	 * 
	 * @return
	 */
	public boolean isMoveToLeft() {
		return toLeft;
	}

	/**
	 * �Ƿ��ƶ������ϱ���
	 * 
	 * @return
	 */
	public boolean isMoveToTop() {
		return toTop;
	}

	/**
	 * �Ƿ��ƶ������±���
	 * 
	 * @return toBottom
	 */
	public boolean isMoveToBottom() {
		return toBottom;
	}

	/**
	 * ͼƬ�Ƿ�Ŵ���
	 * 
	 * @return boolean isZoom
	 */
	public boolean isZoomed() {
		float[] values = new float[9];
		matrix.getValues(values);
		if (values[0] == mValues[0]) {// �ж�ͼƬ�Ƿ�Ŵ���
			return false;
		} else {
			return true;
		}
	}

	/**
	 * �������Ŵ���
	 * 
	 * @param maxMultiple
	 */
	public void setMaxMultiple(int maxMultiple) {
		this.maxMultiple = maxMultiple;
	}

	/**
	 * ����ͼƬ�ķŴ������ƶ�����
	 * 
	 * @param dx
	 * @param dy
	 * @param multiple
	 */
	public void setZoomBigTo(float dx, float dy) {
		isClickTwo = true;
		this.dx = dx;
		this.dy = dy;

		Log.i("xpf", "��" + dx + " dy " + dy);
	}

	/**
	 * ����ͼƬ�ķŴ������ƶ�����
	 * 
	 * @param dx
	 * @param dy
	 * @param multiple
	 */
	public void setZoomToSmall() {
		Log.i("xpf", "mMatrix" + mscale);
		matrix.setValues(mValues);
		lastMatrix.set(matrix);
		this.setImageMatrix(matrix);
	}

	public void onACTION_DOWN(MotionEvent event) {
		savedMatrix.set(matrix);// saveMartrix ����ԭ���ľ���
		start.set(event.getX(), event.getY());
		mode = DRAG;// 1����ָʱ
		this.setImageMatrix(matrix);
	}

	public void onACTION_POINTER_UP() {
		mode = NONE;// ��2����ָ�뿪ʱ
		if (isFirst) {
			tempWidth *= scale;
			tempHeight *= scale;

			if (tempWidth < imageWidth || tempHeight < imageHeight) {
				matrix.postScale(imageWidth / tempWidth, imageHeight / tempHeight, mid.x, mid.y);

				tempWidth = imageWidth;
				tempHeight = imageHeight;
			}

		}
		if (scale1 < scale2) {// ��ģʽ
			float[] values = new float[9];
			matrix.getValues(values);
			if (values[0] < scale3) {// С����Ļ������С
				matrix.setValues(mValues);
				MyImageView.this.clearAnimation();
				ScaleAnimation animation = new ScaleAnimation(values[0] / mValues[0], 1f, values[0] / mValues[0], 1f, W / 2, H / 2);
				animation.setDuration((long) ((1 - values[0] / scale3) * 500));
				MyImageView.this.setAnimation(animation);
			}
		} else {// ��ģʽ
			float[] values = new float[9];
			matrix.getValues(values);
			if (values[0] < scale4) {// С����Ļ������С
				matrix.setValues(mValues);
				MyImageView.this.clearAnimation();
				ScaleAnimation animation = new ScaleAnimation(values[0] / mValues[0], 1f, values[0] / mValues[0], 1f, W / 2, H / 2);
				animation.setDuration((long) ((1 - values[0] / scale4) * 500));
				MyImageView.this.setAnimation(animation);
			}
		}

		// ����˫���Ŵ���������
		if (isClickTwo) {
			isClickTwo = false;
			Log.i("xpf", "mMatrix" + mValues[0]);
			matrix.setValues(mValues);
			matrix.postScale(maxMultiple / 2, maxMultiple / 2, dx, dy);
			Log.i("xpf", "ZoomTobig���Ŵ�λ��Ϊ��" + maxMultiple * mValues[0]);
			float[] values = new float[9];
			matrix.getValues(values);

			// Log.i("xpf", "��ǰΪ��" + values[0]);
			// if (values[0] < maxMultiple * mValues[0])// ����Ƿ񳬳����Χ
			// lastMatrix.set(matrix);
			// if (values[0] > maxMultiple * mValues[0]) {
			// matrix.set(lastMatrix);
			// }
			matrix.getValues(values);
			if (values[0] * bw > W) {// ���������λ��
				if (values[2] < -(values[0] * bw - W)) {
					values[2] = -(values[0] * bw - W);
					matrix.setValues(values);
					Log.i("xpf", "��");
				}
				if (values[2] > 0) {
					values[2] = 0;
					matrix.setValues(values);
					Log.i("xpf", "��");
				}
			} else {
				values[2] = W / 2 - values[0] * bw / 2;
				matrix.setValues(values);
			}

			if (values[0] * bh > H) {// �����߷����λ��
				if (values[5] < -(bh * values[0] - H)) {
					values[5] = -(bh * values[0] - H);
					matrix.setValues(values);
					Log.i("xpf", "��");
				}
				if (values[5] > 0) {
					values[5] = 0;
					matrix.setValues(values);
					Log.i("xpf", "��");
				}
			} else {
				values[5] = H / 2 - values[0] * bh / 2;
				matrix.setValues(values);
			}
			lastMatrix.set(matrix);
			// MyImageView.this.clearAnimation();
			// ScaleAnimation animation = new ScaleAnimation(1f, 2f,
			// 1f, 2f, dx, dy);
			// animation.setDuration(5000);
			// MyImageView.this.setAnimation(animation);
		}
		isFirst = false;

		float[] values2 = new float[9];
		matrix.getValues(values2);
		if (values2[0] > maxMultiple * mValues[0]) {
			matrix.set(lastMatrix);
		}
		this.setImageMatrix(matrix);
	}

	public void onACTION_POINTER_DOWN(MotionEvent event) {
		oldDist = spacing(event);// ��2����ָ����ʱ��������ָ����ľ���
		if (oldDist > 10f) {
			savedMatrix.set(matrix);// saveMartrix ���浱ǰ�ľ���
			midPoint(mid, event);// ��ʼ���м���λ��Ϊ��ָ�����е�
			mode = ZOOM;
		}
		this.setImageMatrix(matrix);
	}

	public void onACTION_MOVE(MotionEvent event) {// �ƶ�
		if (mode == DRAG) {// һ����ָ
			matrix.set(savedMatrix);// ʹ��ԭ���ľ���

			matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);

			float[] values = new float[9];
			matrix.getValues(values);
			Log.i("xpf", "ͼƬ����Ƿ������Ļ���values[0] * bw - W= " + (values[0] * bw - W));
			if (values[0] * bw > W) {// �����
				if ((event.getX() - start.x) <= 0 && values[2] < -(values[0] * bw - W)) {
					values[2] = -(values[0] * bw - W);
					matrix.setValues(values);
					toRight = true;
					toLeft = false;
					Log.i("xpf", "toRight=" + toRight);
				} else {
					toRight = false;
				}
				if ((event.getX() - start.x) > 0 && values[2] > 0) {
					values[2] = 0;
					matrix.setValues(values);
					toLeft = true;
					toRight = false;
					Log.i("xpf", "toLeft=" + toLeft);
				} else {
					toLeft = false;
				}
			} else {
				values[2] = W / 2 - values[0] * bw / 2;
				matrix.setValues(values);
				if ((event.getX() - start.x) > 0) {
					toLeft = true;
				} else {
					toRight = true;
				}
				Log.i("xpf", "���� toLeft " + toLeft + "  toRight " + toRight);
			}

			if (values[0] * bh > H) {// �����
				if ((event.getY() - start.y < 0) && values[5] < -(bh * values[0] - H)) {
					values[5] = -(bh * values[0] - H);
					matrix.setValues(values);
					toBottom = true;
				} else {
					toBottom = false;
				}
				if ((event.getY() - start.y > 0) && values[5] > 0) {
					values[5] = 0;
					matrix.setValues(values);
					Log.i("xpf", "��");
					toTop = true;
				} else {
					toTop = false;
				}
			} else {
				values[5] = H / 2 - values[0] * bh / 2;
				matrix.setValues(values);
			}

			float[] lastMatrixValues = new float[9];
			lastMatrix.getValues(lastMatrixValues);// �����ƶ���ķŴ�
			lastMatrixValues[2] = values[2];
			lastMatrixValues[5] = values[5];
			lastMatrix.setValues(lastMatrixValues);

		} else if (mode == ZOOM) {// ������ָ���ŵ�ʱ�� ,�Ŵ�
			toLeft = false;
			toRight = false;
			toTop = false;
			toBottom = false;
			float newDist = spacing(event);
			if (newDist > 10f) {// �ƶ��������10ʱ����
				matrix.set(savedMatrix);// ʹ��ԭ���ľ���
				scale = newDist / oldDist;
				matrix.postScale(scale, scale, mid.x, mid.y);

				float[] values = new float[9];
				matrix.getValues(values);
				if (values[0] < maxMultiple * mValues[0])// �������Ŵ���
					lastMatrix.set(matrix);
				if (values[0] > maxMultiple * mValues[0]) {
					matrix.set(lastMatrix);
				}

				if (scale1 < scale2) {// ��ģʽ
					if (values[0] < scale3 / 2) {// С����Ļ1/2������С
						matrix.setValues(mSmallValues);
					}
				} else {// ��ģʽ
					if (values[0] < scale4 / 2) {// С����Ļ1/2������С
						matrix.setValues(mSmallValues);
					}
				}

				matrix.getValues(values);
				if (values[0] * bw > W) {// ���������λ��
					if (values[2] < -(values[0] * bw - W)) {
						values[2] = -(values[0] * bw - W);
						matrix.setValues(values);
						Log.i("xpf", "��");
					}
					if (values[2] > 0) {
						values[2] = 0;
						matrix.setValues(values);
						Log.i("xpf", "��");
					}
				} else {
					values[2] = W / 2 - values[0] * bw / 2;
					matrix.setValues(values);
				}

				if (values[0] * bh > H) {// �����߷����λ��
					if (values[5] < -(bh * values[0] - H)) {
						values[5] = -(bh * values[0] - H);
						matrix.setValues(values);
						Log.i("xpf", "��");
					}
					if (values[5] > 0) {
						values[5] = 0;
						matrix.setValues(values);
						Log.i("xpf", "��");
					}
				} else {
					values[5] = H / 2 - values[0] * bh / 2;
					matrix.setValues(values);
				}
				isFirst = true;

			}
		}
		this.setImageMatrix(matrix);
	}

	public float getScale() {
		float[] values = new float[9];
		matrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	public float getImageWidth() {
		float[] values = new float[9];
		matrix.getValues(values);
		return values[Matrix.MSCALE_X] * bw;
	}

	public float getImageHeight() {
		float[] values = new float[9];
		matrix.getValues(values);
		return values[Matrix.MSCALE_Y] * bh;
	}

	public float getScreenWidth() {
		return W;
	}

	public float getScreeHeight() {
		return H;
	}

}// �����
