package com.uyue.johnhancock;

import com.uyue.johnhancock.makeSignature.Const;
import com.uyue.johnhancock.makeSignature.HandWritingActivity;
import com.uyue.johnhancock.model.MySurfaceView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private static final String D_TAG = MainActivity.class.getSimpleName();
	protected static final int MyRequestCode = 0;
	private MySurfaceView mGLSurfaceView;
	private LinearLayout ll;
	// Bundle bundle;
	Intent intent; // ��Ҫ��ȫ�ֱ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����Ϊȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ����Ϊ����ģʽ
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);
		// ��ʼ��mGLSurfaceView
		mGLSurfaceView = new MySurfaceView(this);
		mGLSurfaceView.requestFocus();// ��ȡ����
		mGLSurfaceView.setFocusableInTouchMode(true);// ����Ϊ�ɴ���
		// �л���������
		intent = new Intent();
		
		// ���Զ����GLSurfaceView��ӵ����LinearLayout��
		ll = (LinearLayout) findViewById(R.id.main_liner);
		ll.addView(mGLSurfaceView);
		// �����Ƿ�򿪱�����õ�ToggleButton
		Button tb = (Button) this.findViewById(R.id.ToggleButton01);
		tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Bundle bundle = new Bundle();
				intent.setClass(MainActivity.this, HandWritingActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, MyRequestCode);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
		case RESULT_OK: /* ȡ�����ݣ�����ʾ�ڻ����� */
			Bundle tBundle = data.getExtras();
			String filePath = tBundle.getString(Const.FILEPATH);
			// String filePath =  data.getStringExtra(Const.FILEPATH);
			Log.i(D_TAG, "filePath:" + filePath);
			if (filePath != null) {
				// ll.removeAllViews(); 
				ll.removeView(mGLSurfaceView);
				mGLSurfaceView = new MySurfaceView(MainActivity.this, filePath);
				mGLSurfaceView.requestFocus();// ��ȡ����
				mGLSurfaceView.setFocusableInTouchMode(true);// ����Ϊ�ɴ���
				// mGLSurfaceView.setBitmapPath(filePath);
				ll.addView(mGLSurfaceView);
				//mGLSurfaceView.invalidate();
			} else {
				Log.i(D_TAG, "File path is Null");
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}
}