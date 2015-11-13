package com.uyue.johnhancock;

import java.io.IOException;
import java.io.InputStream;

import com.uyue.johnhancock.app.HTApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.text.TextUtils;
import android.util.Log;

public class TextureUtils {
	private static final String D_TAG = TextureUtils.class.getSimpleName();

	public static int initTexture(String filePath)// textureId
	{
		if(TextUtils.isEmpty(filePath)){
			Log.e(D_TAG, "filePath is null");
			return -1;
		}
		
		int textureId;
		// ��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures(1, // ����������id������
				textures, // ����id������
				0 // ƫ����
		);
		textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		// ͨ������������ͼƬ===============begin===================
		Bitmap bitmapTmp;
		try {
			bitmapTmp = BitmapFactory.decodeFile(filePath);
			Log.i(D_TAG, "bitmap w:" + bitmapTmp.getWidth() + "   height: " + bitmapTmp.getHeight());
		} finally {

		}
		// ͨ������������ͼƬ===============end=====================

		// ʵ�ʼ�������
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // �������ͣ���OpenGL
													// ES�б���ΪGL10.GL_TEXTURE_2D
				0, // ����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmapTmp, // ����ͼ��
				0 // ����߿�ߴ�
		);
		bitmapTmp.recycle(); // ����ɹ����ص��Կ��к��ͷ��ڴ�ͼƬ��

		return textureId;
	}
	
	public static int initTexture(int drawableId) {
		int textureId;
		// ��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures(1, // ����������id������
				textures, // ����id������
				0 // ƫ����
		);
		textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		// ͨ������������ͼƬ===============begin===================
		InputStream is = HTApplication.context.getResources().openRawResource(drawableId); // R.drawable.wall
		Bitmap bitmapTmp;
		try {
			bitmapTmp = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ͨ������������ͼƬ===============end=====================

		// ʵ�ʼ�������
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // �������ͣ���OpenGL
													// ES�б���ΪGL10.GL_TEXTURE_2D
				0, // ����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmapTmp, // ����ͼ��
				0 // ����߿�ߴ�
		);
		bitmapTmp.recycle(); // ����ɹ����ص��Կ��к��ͷ��ڴ�ͼƬ��

		return textureId;
	}
}
