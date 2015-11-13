package com.uyue.johnhancock.makeSignature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.uyue.johnhancock.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HandWritingActivity extends Activity {
	private static final String D_TAG = HandWritingActivity.class.getSimpleName();

	private Bitmap mSignBitmap;
	// private String signPath = "/storage/sdcard0/1446717811978.png";
	private String signPath ;
	private ImageView ivSign;
	private TextView tvSign;
	private Button btnOK;

	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_sign);
		setTitle("欢迎使用手写签名");

		intent = this.getIntent();

		ivSign = (ImageView) findViewById(R.id.iv_sign);
		tvSign = (TextView) findViewById(R.id.tv_sign);

		ivSign.setOnClickListener(signListener);
		tvSign.setOnClickListener(signListener);

		btnOK = (Button) findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(okListener);
	}

	private OnClickListener signListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			WritePadDialog writeTabletDialog = new WritePadDialog(HandWritingActivity.this, new DialogListener() {
				@Override
				public void refreshActivity(Object object) {
					mSignBitmap = (Bitmap) object;
					signPath = createFile();
					Log.i(D_TAG, signPath);
					/*
					 * BitmapFactory.Options options = new
					 * BitmapFactory.Options(); options.inSampleSize = 15;
					 * options.inTempStorage = new byte[5 * 1024]; Bitmap zoombm
					 * = BitmapFactory.decodeFile(signPath, options);
					 */
					ivSign.setImageBitmap(mSignBitmap);
					tvSign.setVisibility(View.GONE);

				}
			});
			writeTabletDialog.show();
		}
	};

	private OnClickListener okListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (TextUtils.isEmpty(signPath)) {
				Log.w(D_TAG, "have no bitmap path");
			} else {
				// intent.getExtras().putString(Const.FILEPATH, signPath); // 上面的写法 错误
				intent.putExtra(Const.FILEPATH, signPath);
				Log.i(D_TAG, "Set Bundle signPath is :" + signPath);
				HandWritingActivity.this.setResult(RESULT_OK, intent);
				HandWritingActivity.this.finish();
			}
		}
	};

	/**
	 * 创建手写签名文件
	 * 
	 * @return
	 */
	private String createFile() {
		ByteArrayOutputStream baos = null;
		String _path = null;
		try {
			String sign_dir = Environment.getExternalStorageDirectory() + File.separator;
			_path = sign_dir + System.currentTimeMillis() + ".png";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _path;
	}
}