package com.uyue.johnhancock.model;

import static com.uyue.johnhancock.model.Constant.UNIT_SIZE;

//������
public class Cube {
	private static final String D_TAG = Cube.class.getSimpleName();
	// ���ڻ��Ƹ��������ɫ����
	ColorRect cr;
	ColorRect2 cr2;
	Triangle triangle;

	TextureRect textureRect;

	private String filePath;

	public Cube(MySurfaceView mv) {
		// �������ڻ��Ƹ��������ɫ����
		cr = new ColorRect(mv);
		cr2 = new ColorRect2(mv);

		triangle = new Triangle(mv);
		textureRect = new TextureRect(mv, 1, 1);
	}

	public Cube(MySurfaceView mv, String path) {
		// �������ڻ��Ƹ��������ɫ����
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
		// �ܻ���˼�룺ͨ����һ����ɫ������ת��λ��������ÿ�����λ��
		// �����������ÿ����

		// �����ֳ�
		MatrixState.pushMatrix();

		// ����ǰ��
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, UNIT_SIZE);
		cr.drawSelf();
		MatrixState.popMatrix();

		// ���ƺ���
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, -UNIT_SIZE);
		MatrixState.rotate(180, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// ��������
		MatrixState.pushMatrix();
		MatrixState.translate(0, UNIT_SIZE, 0);
		MatrixState.rotate(-90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// ��������
		MatrixState.pushMatrix();
		MatrixState.translate(0, -UNIT_SIZE, 0);
		MatrixState.rotate(90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// ��������
		MatrixState.pushMatrix();
		MatrixState.translate(UNIT_SIZE, 0, 0);
		MatrixState.rotate(-90, 1, 0, 0);
		MatrixState.rotate(90, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();

		// ��������
		MatrixState.pushMatrix();
		MatrixState.translate(UNIT_SIZE + 0.01f, 0, 0);
		// MatrixState.rotate(-90, 1, 0, 0);
		MatrixState.rotate(90, 0, 1, 0);
		textureRect.drawSelf();
		MatrixState.popMatrix();

		// ��������
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

		// �ָ��ֳ�
		MatrixState.popMatrix();

	}

}
