package com.uyue.johnhancock.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.uyue.johnhancock.R;
import com.uyue.johnhancock.TextureUtils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

// �������
public class TextureRect {
	private static final String D_TAG = TextureRect.class.getSimpleName();
	int mProgram;// �Զ�����Ⱦ���߳���id
	int muMVPMatrixHandle;// �ܱ任��������id
	int muMMatrixHandle;// �ܱ任��������id

	// ƬԪ��Ⱦ��Tex1��TexWord������������
	int uTexHandle; // ��������������������id1
	int uTexWordHandle; // ��������������������id2

	int maPositionHandle; // ����λ����������id
	int maTexCoorHandle; // ��������������������id
	String mVertexShader;// ������ɫ��
	String mFragmentShader;// ƬԪ��ɫ��

	FloatBuffer mVertexBuffer;// �����������ݻ���
	FloatBuffer mTexCoorBuffer;// ���������������ݻ���
	int vCount = 0;
	float xAngle = 0;// ��x����ת�ĽǶ�
	float yAngle = 0;// ��y����ת�ĽǶ�
	float zAngle = 0;// ��z����ת�ĽǶ�

	float sRange;// s�������귶Χ
	float tRange;// t�������귶Χ

	public TextureRect(MySurfaceView mv, float sRange, float tRange) {
		// ����s�������귶Χ
		this.sRange = sRange;
		// ����t�������귶Χ
		this.tRange = tRange;

		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(mv);
		initTexture1();
	}

	public TextureRect(MySurfaceView mv, String path, float sRange, float tRange) {
		// ����s�������귶Χ
		this.sRange = sRange;
		// ����t�������귶Χ
		this.tRange = tRange;

		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(mv);

		bitmapPath = path;
		initTexture2();
	}

	int textureWallId;
	int textureWordId;
	String bitmapPath;

	private void initTexture1() {
		// ��ʼ������
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		Log.i(D_TAG, "textureWallId is :" + textureWallId);
		textureWordId = TextureUtils.initTexture(R.drawable.btv2);
	}

	private void initTexture2() {
		// ��ʼ������
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		Log.i(D_TAG, "textureWallId is :" + textureWallId);
		textureWordId = TextureUtils.initTexture(bitmapPath);
	}

	// ��ʼ��������������ɫ���ݵķ���
	public void initVertexData() {
		// �����������ݵĳ�ʼ��================begin============================
		vCount = 6;
		final float UNIT_SIZE = 0.3f;
		float vertices[] = new float[] { -4 * UNIT_SIZE, 4 * UNIT_SIZE, 0, -4 * UNIT_SIZE, -4 * UNIT_SIZE, 0,
				4 * UNIT_SIZE, -4 * UNIT_SIZE, 0,

				4 * UNIT_SIZE, -4 * UNIT_SIZE, 0, 4 * UNIT_SIZE, 4 * UNIT_SIZE, 0, -4 * UNIT_SIZE, 4 * UNIT_SIZE, 0 };

		// ���������������ݻ���
		// vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================

		// ���������������ݵĳ�ʼ��================begin============================
		float texCoor[] = new float[]// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		{ 0, 0, 0, tRange, sRange, tRange, sRange, tRange, sRange, 0, 0, 0 };
		// �������������������ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTexCoorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);// �򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ���������������ݵĳ�ʼ��================end============================

	}

	// ��ʼ��shader
	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_tt.sh", mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_tt.sh", mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");

		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

		// ��ȡƬԪ��Ⱦ��Tex1��TexWord������������
		uTexHandle = GLES20.glGetUniformLocation(mProgram, "sTexture");
		uTexWordHandle = GLES20.glGetUniformLocation(mProgram, "sTextureWord");

	}

	public void drawSelf() {
		// �ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(mProgram);
		// ��ʼ���任����
		// Matrix.setRotateM(MatrixState.getMMatrix(), 0, 0, 0, 1, 0);
		// // ������Z������λ��1
		// Matrix.translateM(MatrixState.getMMatrix(), 0, 0, 0, 3);
		// // ������y����ת
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, yAngle, 0, 1, 0);
		// // ������z����ת
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, zAngle, 0, 0, 1);
		// // ������x����ת
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, xAngle, 1, 0, 0);
		// �����ձ任������shader����
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		// ��λ�á���ת�任������shader����
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);

		// Ϊ����ָ������λ������
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
		// Ϊ����ָ������������������
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
		// ������λ����������
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		// ������
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureWallId);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureWordId);

		GLES20.glUniform1i(uTexHandle, 0);
		GLES20.glUniform1i(uTexWordHandle, 1);

		// �����������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}
