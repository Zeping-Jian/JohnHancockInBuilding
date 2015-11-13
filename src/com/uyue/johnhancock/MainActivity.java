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
	Intent intent; // 需要是全局变量

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置为全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置为横屏模式
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);
		// 初始化mGLSurfaceView
		mGLSurfaceView = new MySurfaceView(this);
		mGLSurfaceView.requestFocus();// 获取焦点
		mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
		// 切换到主界面
		intent = new Intent();
		
		// 将自定义的GLSurfaceView添加到外层LinearLayout中
		ll = (LinearLayout) findViewById(R.id.main_liner);
		ll.addView(mGLSurfaceView);
		// 控制是否打开背面剪裁的ToggleButton
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
		case RESULT_OK: /* 取得数据，并显示于画面上 */
			Bundle tBundle = data.getExtras();
			String filePath = tBundle.getString(Const.FILEPATH);
			// String filePath =  data.getStringExtra(Const.FILEPATH);
			Log.i(D_TAG, "filePath:" + filePath);
			if (filePath != null) {
				// ll.removeAllViews(); 
				ll.removeView(mGLSurfaceView);
				mGLSurfaceView = new MySurfaceView(MainActivity.this, filePath);
				mGLSurfaceView.requestFocus();// 获取焦点
				mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
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