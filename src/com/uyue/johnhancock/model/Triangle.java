package com.uyue.johnhancock.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.uyue.johnhancock.R;
import com.uyue.johnhancock.TextureUtils;
import com.uyue.johnhancock.R.drawable;

import android.opengl.GLES20;

//����������
public class Triangle {
	int mProgram;// �Զ�����Ⱦ���߳���id
	int muMVPMatrixHandle;// �ܱ任��������id
	int maPositionHandle; // ����λ����������id
	int maTexCoorHandle; // ��������������������id
	int muMMatrixHandle;// λ�á���ת�任��������

	// ƬԪ��Ⱦ��Tex1��TexWord������������
	int uTexHandle; // ��������������������id1
	int uTexWordHandle; // ��������������������id2

	String mVertexShader;// ������ɫ��
	String mFragmentShader;// ƬԪ��ɫ��

	FloatBuffer mVertexBuffer;// �����������ݻ���
	FloatBuffer mTexCoorBuffer;// ���������������ݻ���
	FloatBuffer mTexCoorWordBuffer;// ���������������ݻ���
	int vCount = 0;
	float xAngle = 0;// ��x����ת�ĽǶ�
	float yAngle = 0;// ��y����ת�ĽǶ�
	float zAngle = 0;// ��z����ת�ĽǶ�

	float Z = 0.01f;

	public Triangle(MySurfaceView mv) {
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ����ɫ��
		initShader(mv);

		initTexture();
	}
	
	public Triangle(MySurfaceView mv, String path) {
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ����ɫ��
		initShader(mv);
		
		bitmapPath = path;
		initTexture2();
	}

	int textureWallId;
	int textureWordId;
	String bitmapPath;

	public void setBitmapPath(String path){
		bitmapPath = path;
		textureWordId = TextureUtils.initTexture(bitmapPath);
	}
	
	
	private void initTexture() {
		// ��ʼ������
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		textureWordId = TextureUtils.initTexture(R.drawable.btv2);
	}
	
	private void initTexture2() {
		// ��ʼ������
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		textureWordId = TextureUtils.initTexture(bitmapPath);
	}

	// ��ʼ��������������ɫ���ݵķ���
	public void initVertexData() {
		// �����������ݵĳ�ʼ��================begin============================
		vCount = 3;
		final float UNIT_SIZE = 0.15f;
		float vertices[] = new float[] { 0 * UNIT_SIZE, 11 * UNIT_SIZE, Z, -11 * UNIT_SIZE, -11 * UNIT_SIZE, Z,
				11 * UNIT_SIZE, -11 * UNIT_SIZE, Z, };

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
		{ 0.5f, 0, 0, 1, 1, 1 };
		// �������������������ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTexCoorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);// �򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ���������������ݵĳ�ʼ��================end============================

		// �������������������ݻ���2
		float texCoorWord[] = new float[]// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		{ 0.2f, 0, 0, 0.7f, 0.7f, 0.7f };
		ByteBuffer cwd = ByteBuffer.allocateDirect(texCoorWord.length * 4);
		cwd.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTexCoorWordBuffer = cwd.asFloatBuffer();// ת��ΪFloat�ͻ���
		mTexCoorWordBuffer.put(texCoorWord);// �򻺳����з��붥����ɫ����
		mTexCoorWordBuffer.position(0);// ���û�������ʼλ��
	}

	// ��ʼ����ɫ��
	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_t.sh", mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_t.sh", mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		muMMatrixHandle = GLES20.glGetAttribLocation(mProgram, "uMMatrix");
		// ��ȡƬԪ��Ⱦ��Tex1��TexWord������������
		uTexHandle = GLES20.glGetUniformLocation(mProgram, "sTexture");
		uTexWordHandle = GLES20.glGetUniformLocation(mProgram, "sTextureWord");

		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf() {
		// �ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(mProgram);

		// MatrixState.setInitStack();

		MatrixState.rotate(90, 0, 0, 1);
//		MatrixState.translate(0, 0, 1.4f);
		
		// �����ձ任������shader����
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		// ��λ�á���ת�任������shader����
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);

		// TODO û������
		// ��λ�á���ת�任��������ɫ������
		// GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
		// MatrixState.getMMatrix(), 0);
		// �������λ�ô�����ɫ������
		// GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);

		// Ϊ����ָ������λ������
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
		// Ϊ����ָ������������������
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);

		// Ϊ����ָ������������������
		// GLES20.glVertexAttribPointer(maTexWordHandle, 4, GLES20.GL_FLOAT,
		// false, 2 * 4, mVertexBuffer);
		// ������λ����������
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// GLES20.glEnableVertexAttribArray(maTexWordHandle);

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
