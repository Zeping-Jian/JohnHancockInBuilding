package com.uyue.johnhancock.model;

import static com.uyue.johnhancock.model.Constant.UNIT_SIZE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.uyue.johnhancock.TextureUtils;

import android.opengl.GLES20;
import android.util.Log;

//颜色矩形
public class ColorRect2 {
	private static final String D_TAG = ColorRect2.class.getSimpleName();
	int mProgram;// 自定义渲染管线着色器程序id
	int muMVPMatrixHandle;// 总变换矩阵引用
	int muMMatrixHandle;// 位置、旋转变换矩阵引用
	int maPositionHandle; // 顶点位置属性引用
	int maColorHandle; // 顶点颜色属性引用

	int maTexCoorHandle; // 纹理引用
	int uBitmapTexHandle; // 纹理引用

	String mVertexShader;// 顶点着色器代码脚本
	String mFragmentShader;// 片元着色器代码脚本

	FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
	FloatBuffer mColorBuffer;// 顶点着色数据缓冲
	int vCount = 0;

	public ColorRect2(MySurfaceView mv) {
		// 初始化顶点坐标与着色数据
		initVertexData();
		// 初始化shader
		initShader(mv);

		setFilePath("");
	}

	// 初始化顶点坐标与着色数据的方法
	public void initVertexData() {
		// 顶点坐标数据的初始化================begin============================
		vCount = 6;
		float vertices[] = new float[] { 0, 0, 0, UNIT_SIZE, UNIT_SIZE, 0, -UNIT_SIZE, UNIT_SIZE, 0, -UNIT_SIZE,
				-UNIT_SIZE, 0, UNIT_SIZE, -UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE, 0 };

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

		// 顶点着色数据的初始化================begin============================
		 float colors[] = new float[]// 顶点颜色值数组，每个顶点4个色彩值RGBA
		 { 0, 1, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0,
		 1, 0, 0, 0, };
		// float colors[] = new float[] { 0, 0, 1, 0, 0, 1, 1, 1 };
		// 创建顶点着色数据缓冲
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mColorBuffer = cbb.asFloatBuffer();// 转换为Float型缓冲
		mColorBuffer.put(colors);// 向缓冲区中放入顶点着色数据
		mColorBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
		// 顶点着色数据的初始化================end============================

	}

	// 初始化shader
	public void initShader(MySurfaceView mv) {
		// 这里加载 是 vertex.sh or vertex2.sh 最用一样 ， 当然内容是一样的。
		// 加载顶点着色器的脚本内容
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex2.sh", mv.getResources());
		// 加载片元着色器的脚本内容
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag2.sh", mv.getResources());
		// 基于顶点着色器与片元着色器创建程序
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// 获取程序中顶点位置属性引用id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// 获取程序中顶点颜色属性引用id
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		// 获取程序中总变换矩阵引用id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// 获取位置、旋转变换矩阵引用id
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

		// 获取程序中顶点纹理坐标属性引用id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");

		// 获取图片的纹理引用
		uBitmapTexHandle = GLES20.glGetUniformLocation(mProgram, "sTextureBitmap");
	}

	int TexHandle; // 系统分配的纹理 id

	// 在一个面上-进行签名操作，怎么样
	public void drawSelf() {
		// 制定使用某套shader程序
		GLES20.glUseProgram(mProgram);
		// 将最终变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		// 将位置、旋转变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		// 为画笔指定顶点位置数据
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
		// 为画笔指定顶点着色数据
		// GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT,
		// false, 4 * 4, mColorBuffer);

		GLES20.glVertexAttribPointer(maTexCoorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);

		// 允许顶点位置数据数组
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		if (TexHandle > 0) {
			// 绑定纹理
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TexHandle);
			GLES20.glUniform1i(uBitmapTexHandle, 0);
		}

		// 绘制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}


	String filePath;

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		//
		// String johnPath = "/storage/sdcard0/1446730124850.png";
		// 3 横
		// String johnPath = "/storage/sdcard0/1446731177799.png";
		
		// 3 竖 
		String johnPath = "/storage/sdcard0/1446731320291.png";
		
		TexHandle = TextureUtils.initTexture(johnPath);
		Log.i(D_TAG, "TexHandle is:" + TexHandle);
	}
}
