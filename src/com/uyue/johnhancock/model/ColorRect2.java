package com.uyue.johnhancock.model;

import static com.uyue.johnhancock.model.Constant.UNIT_SIZE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.uyue.johnhancock.TextureUtils;

import android.opengl.GLES20;
import android.util.Log;

//��ɫ����
public class ColorRect2 {
	private static final String D_TAG = ColorRect2.class.getSimpleName();
	int mProgram;// �Զ�����Ⱦ������ɫ������id
	int muMVPMatrixHandle;// �ܱ任��������
	int muMMatrixHandle;// λ�á���ת�任��������
	int maPositionHandle; // ����λ����������
	int maColorHandle; // ������ɫ��������

	int maTexCoorHandle; // ��������
	int uBitmapTexHandle; // ��������

	String mVertexShader;// ������ɫ������ű�
	String mFragmentShader;// ƬԪ��ɫ������ű�

	FloatBuffer mVertexBuffer;// �����������ݻ���
	FloatBuffer mColorBuffer;// ������ɫ���ݻ���
	int vCount = 0;

	public ColorRect2(MySurfaceView mv) {
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(mv);

		setFilePath("");
	}

	// ��ʼ��������������ɫ���ݵķ���
	public void initVertexData() {
		// �����������ݵĳ�ʼ��================begin============================
		vCount = 6;
		float vertices[] = new float[] { 0, 0, 0, UNIT_SIZE, UNIT_SIZE, 0, -UNIT_SIZE, UNIT_SIZE, 0, -UNIT_SIZE,
				-UNIT_SIZE, 0, UNIT_SIZE, -UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE, 0 };

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

		// ������ɫ���ݵĳ�ʼ��================begin============================
		 float colors[] = new float[]// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		 { 0, 1, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0, };
		// float colors[] = new float[] { 0, 0, 1, 0, 0, 1, 1, 1 };
		// ����������ɫ���ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mColorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mColorBuffer.put(colors);// �򻺳����з��붥����ɫ����
		mColorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ������ɫ���ݵĳ�ʼ��================end============================

	}

	// ��ʼ��shader
	public void initShader(MySurfaceView mv) {
		// ������� �� vertex.sh or vertex2.sh ����һ�� �� ��Ȼ������һ���ġ�
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex2.sh", mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag2.sh", mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�����ɫ��������id
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// ��ȡλ�á���ת�任��������id
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");

		// ��ȡͼƬ����������
		uBitmapTexHandle = GLES20.glGetUniformLocation(mProgram, "sTextureBitmap");
	}

	int TexHandle; // ϵͳ��������� id

	// ��һ������-����ǩ����������ô��
	public void drawSelf() {
		// �ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(mProgram);
		// �����ձ任������shader����
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		// ��λ�á���ת�任������shader����
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		// Ϊ����ָ������λ������
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
		// Ϊ����ָ��������ɫ����
		// GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT,
		// false, 4 * 4, mColorBuffer);

		GLES20.glVertexAttribPointer(maTexCoorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);

		// ������λ����������
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		if (TexHandle > 0) {
			// ������
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TexHandle);
			GLES20.glUniform1i(uBitmapTexHandle, 0);
		}

		// ����������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}


	String filePath;

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		//
		// String johnPath = "/storage/sdcard0/1446730124850.png";
		// 3 ��
		// String johnPath = "/storage/sdcard0/1446731177799.png";
		
		// 3 �� 
		String johnPath = "/storage/sdcard0/1446731320291.png";
		
		TexHandle = TextureUtils.initTexture(johnPath);
		Log.i(D_TAG, "TexHandle is:" + TexHandle);
	}
}
