package com.uyue.johnhancock.model;

import static com.uyue.johnhancock.model.Constant.UNIT_SIZE;

//立方体
public class Cube {
	private static final String D_TAG = Cube.class.getSimpleName();
	// 用于绘制各个面的颜色矩形
	ColorRect cr;
	ColorRect2 cr2;
	Triangle triangle;

	TextureRect textureRect;

	private String filePath;

	public Cube(MySurfaceView mv) {
		// 创建用于绘制各个面的颜色矩形
		cr = new ColorRect(mv);
		cr2 = new ColorRect2(mv);

		triangle = new Triangle(mv);
		textureRect = new TextureRect(mv, 1, 1);
	}

	public Cube(MySurfaceView mv, String path) {
		// 创建用于绘制各个面的颜色矩形
		cr = new ColorRect(mv);
		cr2 = new ColorRect2(mv);

		triangle = new Triangle(mv, path);
		textureRect = new TextureRect(mv, path, 1, 1);
	}

	public void setFilePath(String fileP) {
		filePath = fileP;
		cr2.setFilePath(filePath);
		triangle.setBitmapPath(fileP);
	}

	public void drawSelf() {
		// 总绘制思想：通过把一个颜色矩形旋转移位到立方体每个面的位置
		// 绘制立方体的每个面

		// 保护现场
		MatrixState.pushMatrix();

		// 绘制前面
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, UNIT_SIZE);
		cr.drawSelf();
		MatrixState.popMatrix();

		// 绘制后面
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, -UNIT_SIZE);
		MatrixState.rotate(180, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// 绘制上面
		MatrixState.pushMatrix();
		MatrixState.translate(0, UNIT_SIZE, 0);
		MatrixState.rotate(-90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// 绘制下面
		MatrixState.pushMatrix();
		MatrixState.translate(0, -UNIT_SIZE, 0);
		MatrixState.rotate(90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// 绘制左面
		MatrixState.pushMatrix();
		MatrixState.translate(UNIT_SIZE, 0, 0);
		MatrixState.rotate(-90, 1, 0, 0);
		MatrixState.rotate(90, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// 绘制左面
		MatrixState.pushMatrix();
		MatrixState.translate(UNIT_SIZE + 0.01f, 0, 0);
		// MatrixState.rotate(-90, 1, 0, 0);
		MatrixState.rotate(90, 0, 1, 0);
		textureRect.drawSelf();
		MatrixState.popMatrix();

		// 绘制右面
		MatrixState.pushMatrix();
		MatrixState.translate(-UNIT_SIZE, 0, 0);
		MatrixState.rotate(90, 1, 0, 0);
		MatrixState.rotate(-90, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-UNIT_SIZE - 0.01f, 0, 0);
		MatrixState.rotate(90, 1, 0, 0);
		MatrixState.rotate(-90, 0, 1, 0);
		// cr2.drawSelf();
		triangle.drawSelf();
		MatrixState.popMatrix();

		// 恢复现场
		MatrixState.popMatrix();

	}

}
