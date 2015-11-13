uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵

attribute vec3 aPosition;  //顶点位置
attribute vec4 aColor;    //顶点颜色

attribute vec2 aTexCoor;    //顶点纹理坐标
attribute vec3 aNormal;    //对应点 法向量

varying vec2 vTextureCoord; //用于传递给片元着色器的变量

varying vec4 vColor;  //用于传递给片元着色器的变量
varying vec3 vPosition;//用于传递给片元着色器的顶点位置


void main()  {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vColor = aColor;//将接收的颜色传递给片元着色器
   vPosition=(uMMatrix * vec4(aPosition,1)).xyz;
   
   vTextureCoord = aTexCoor;
}                      