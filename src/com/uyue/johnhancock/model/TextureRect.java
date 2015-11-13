package com.uyue.johnhancock.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.uyue.johnhancock.R;
import com.uyue.johnhancock.TextureUtils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

// 纹理矩形
public class TextureRect {
	private static final String D_TAG = TextureRect.class.getSimpleName();
	int mProgram;// 自定义渲染管线程序id
	int muMVPMatrixHandle;// 总变换矩阵引用id
	int muMMatrixHandle;// 总变换矩阵引用id

	// 片元渲染器Tex1、TexWord两个纹理引用
	int uTexHandle; // 顶点纹理坐标属性引用id1
	int uTexWordHandle; // 顶点纹理坐标属性引用id2

	int maPositionHandle; // 顶点位置属性引用id
	int maTexCoorHandle; // 顶点纹理坐标属性引用id
	String mVertexShader;// 顶点着色器
	String mFragmentShader;// 片元着色器

	FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
	FloatBuffer mTexCoorBuffer;// 顶点纹理坐标数据缓冲
	int vCount = 0;
	float xAngle = 0;// 绕x轴旋转的角度
	float yAngle = 0;// 绕y轴旋转的角度
	float zAngle = 0;// 绕z轴旋转的角度

	float sRange;// s纹理坐标范围
	float tRange;// t纹理坐标范围

	public TextureRect(MySurfaceView mv, float sRange, float tRange) {
		// 设置s纹理坐标范围
		this.sRange = sRange;
		// 设置t纹理坐标范围
		this.tRange = tRange;

		// 初始化顶点坐标与着色数据
		initVertexData();
		// 初始化shader
		initShader(mv);
		initTexture1();
	}

	public TextureRect(MySurfaceView mv, String path, float sRange, float tRange) {
		// 设置s纹理坐标范围
		this.sRange = sRange;
		// 设置t纹理坐标范围
		this.tRange = tRange;

		// 初始化顶点坐标与着色数据
		initVertexData();
		// 初始化shader
		initShader(mv);

		bitmapPath = path;
		initTexture2();
	}

	int textureWallId;
	int textureWordId;
	String bitmapPath;

	private void initTexture1() {
		// 初始化纹理
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		Log.i(D_TAG, "textureWallId is :" + textureWallId);
		textureWordId = TextureUtils.initTexture(R.drawable.btv2);
	}

	private void initTexture2() {
		// 初始化纹理
		textureWallId = TextureUtils.initTexture(R.drawable.wall);
		Log.i(D_TAG, "textureWallId is :" + textureWallId);
		textureWordId = TextureUtils.initTexture(bitmapPath);
	}

	// 初始化顶点坐标与着色数据的方法
	public void initVertexData() {
		// 顶点坐标数据的初始化================begin============================
		vCount = 6;
		final float UNIT_SIZE = 0.3f;
		float vertices[] = new float[] { -4 * UNIT_SIZE, 4 * UNIT_SIZE, 0, -4 * UNIT_SIZE, -4 * UNIT_SIZE, 0,
				4 * UNIT_SIZE, -4 * UNIT_SIZE, 0,

				4 * UNIT_SIZE, -4 * UNIT_SIZE, 0, 4 * UNIT_SIZE, 4 * UNIT_SIZE, 0, -4 * UNIT_SIZE, 4 * UNIT_SIZE, 0 };

		// 创建顶点坐标数据缓冲
		// vertices.length*4是因为一个整数四个字节
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mVertexBuffer = vbb.asFloatBuffer();// 转换为Float型缓冲
		mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
		mVertexBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
		// 顶点坐标数据的初始化================end============================

		// 顶点纹理坐标数据的初始化================begin============================
		float texCoor[] = new float[]// 顶点颜色值数组，每个顶点4个色彩值RGBA
		{ 0, 0, 0, tRange, sRange, tRange, sRange, tRange, sRange, 0, 0, 0 };
		// 创建顶点纹理坐标数据缓冲
		ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
		cbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mTexCoorBuffer = cbb.asFloatBuffer();// 转换为Float型缓冲
		mTexCoorBuffer.put(texCoor);// 向缓冲区中放入顶点着色数据
		mTexCoorBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
		// 顶点纹理坐标数据的初始化================end============================

	}

	// 初始化shader
	public void initShader(MySurfaceView mv) {
		// 加载顶点着色器的脚本内容
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_tt.sh", mv.getResources());
		// 加载片元着色器的脚本内容
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_tt.sh", mv.getResources());
		// 基于顶点着色器与片元着色器创建程序
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// 获取程序中顶点位置属性引用id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// 获取程序中顶点纹理坐标属性引用id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");

		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		// 获取程序中总变换矩阵引用id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

		// 获取片元渲染器Tex1、TexWord两个纹理引用
		uTexHandle = GLES20.glGetUniformLocation(mProgram, "sTexture");
		uTexWordHandle = GLES20.glGetUniformLocation(mProgram, "sTextureWord");

	}

	public void drawSelf() {
		// 制定使用某套shader程序
		GLES20.glUseProgram(mProgram);
		// 初始化变换矩阵
		// Matrix.setRotateM(MatrixState.getMMatrix(), 0, 0, 0, 1, 0);
		// // 设置沿Z轴正向位移1
		// Matrix.translateM(MatrixState.getMMatrix(), 0, 0, 0, 3);
		// // 设置绕y轴旋转
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, yAngle, 0, 1, 0);
		// // 设置绕z轴旋转
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, zAngle, 0, 0, 1);
		// // 设置绕x轴旋转
		// Matrix.rotateM(MatrixState.getMMatrix(), 0, xAngle, 1, 0, 0);
		// 将最终变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		// 将位置、旋转变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);

		// 为画笔指定顶点位置数据
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
		// 为画笔指定顶点纹理坐标数据
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
		// 允许顶点位置数据数组
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		// 绑定纹理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureWallId);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureWordId);

		GLES20.glUniform1i(uTexHandle, 0);
		GLES20.glUniform1i(uTexWordHandle, 1);

		// 绘制纹理矩形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}
