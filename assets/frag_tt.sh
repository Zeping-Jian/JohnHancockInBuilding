precision mediump float;

varying vec2 vTextureCoord; //接收从顶点着色器过来的参数

uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D sTextureWord;//Word纹理内容数据

varying  vec4 vColor; //接收从顶点着色器过来的参数
varying vec3 vPosition;//接收从顶点着色器过来的顶点位置

void main()                         
{           
   //给此片元从纹理中采样出颜色值            
   gl_FragColor = texture2D(sTexture, vTextureCoord); 
   
   //给此片元从纹理中采样出颜色值   
  vec4 finalColorDay;   
  vec4 finalColorNight;   
  
  finalColorDay= texture2D(sTexture, vTextureCoord);
  finalColorNight = texture2D(sTextureWord, vTextureCoord); 
  
  if( finalColorNight[3] == 0.0){ // finalColorDay[0] > 0.0 && 
	// 透明 则 搞定是那个问题
   	gl_FragColor=finalColorDay; 
  }else if(finalColorNight[2] >= 0.5){
   	// 只要 有字迹 出现就取 字迹
  	gl_FragColor=finalColorNight; 
  } else {
  	gl_FragColor=finalColorDay;   	
  }
}              