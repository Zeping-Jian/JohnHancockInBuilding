package com.uyue.johnhancock.model;

import static com.uyue.johnhancock.model.Constant.ratio;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView {
	protected static final String D_TAG = MySurfaceView.class.getSimpleName();

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private SceneRenderer mRenderer;// ������Ⱦ��

	private float mPreviousX;// �ϴεĴ���λ��X����

	float yAngle = 0;// �ܳ�����y����ת�ĽǶ�
	public String rednerBitmapPath;

	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2); // ����ʹ��OPENGL ES2.0
		mRenderer = new SceneRenderer(""); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	public MySurfaceView(Context context, String path) {
		super(context);
		this.setEGLContextClientVersion(2); // ����ʹ��OPENGL ES2.0
		mRenderer = new SceneRenderer(path); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	// �����¼��ص�����
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
//		queueEvent(new Runnable() {
//			public void run() {
//				// mRenderer.setColor(e.getX() / getWidth(), e.getY() /
//				// getHeight(), 1.0f);
//				Log.i(D_TAG, "queueEvent is function ");
//			}
//		});

		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;// ���㴥�ر�Xλ��
			yAngle += dx * TOUCH_SCALE_FACTOR;// ���������ζ���y����ת�Ƕ�
		}
		mPreviousX = x;
		return true;
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		super.onKeyDown(keyCode, event);
		Log.i(D_TAG, "on KeyDown" + keyCode  + "  event.getKeyCode() is:" + event.getKeyCode());
		return false;
	};

	Cube cube;// ������

	private class SceneRenderer implements GLSurfaceView.Renderer {
		String BitmapPath;

		public SceneRenderer(String path) {
			BitmapPath = path;
		}

		public void onDrawFrame(GL10 gl) {
			// �����Ȼ�������ɫ����
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			// �����ֳ�
			MatrixState.pushMatrix();
			// ��Y����ת
			MatrixState.rotate(yAngle, 0, 1, 0);

			// �������������
			MatrixState.pushMatrix();
			// MatrixState.translate(-3, 0, 0);
			MatrixState.rotate(60, 0, 1, 0);
			cube.drawSelf();
			MatrixState.popMatrix();

			// �����Ҳ�������
			// MatrixState.pushMatrix();
			// MatrixState.translate(3, 0, 0);
			// MatrixState.rotate(-60, 0, 1, 0);
			// cube.drawSelf();
			// MatrixState.popMatrix();

			// �ָ��ֳ�
			MatrixState.popMatrix();
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// �����Ӵ���С��λ��
			GLES20.glViewport(0, 0, width, height);
			// ����GLSurfaceView�Ŀ�߱�
			ratio = (float) width / height;

			// ��Ҫ�������ӽǣ� ���� �ӽǲ����ʵ��±���
			// ���ô˷����������͸��ͶӰ����
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
			// ���ô˷������������9����λ�þ���
			MatrixState.setCamera(0, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

			// ��ʼ���任����
			MatrixState.setInitStack();
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// ������Ļ����ɫRGBA
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			// �������������
			if (TextUtils.isEmpty(BitmapPath)) {
				cube = new Cube(MySurfaceView.this);
			} else {
				cube = new Cube(MySurfaceView.this, BitmapPath);
			}
			// ����ȼ��
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			// �򿪱������
			// GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}

	public void setBitmapPath(String filePath) {
		rednerBitmapPath = filePath;
		cube.setFilePath(rednerBitmapPath);
	}
}
