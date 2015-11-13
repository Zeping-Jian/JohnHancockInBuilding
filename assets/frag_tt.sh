precision mediump float;

varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���

uniform sampler2D sTexture;//������������
uniform sampler2D sTextureWord;//Word������������

varying  vec4 vColor; //���մӶ�����ɫ�������Ĳ���
varying vec3 vPosition;//���մӶ�����ɫ�������Ķ���λ��

void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   gl_FragColor = texture2D(sTexture, vTextureCoord); 
   
   //����ƬԪ�������в�������ɫֵ   
  vec4 finalColorDay;   
  vec4 finalColorNight;   
  
  finalColorDay= texture2D(sTexture, vTextureCoord);
  finalColorNight = texture2D(sTextureWord, vTextureCoord); 
  
  if( finalColorNight[3] == 0.0){ // finalColorDay[0] > 0.0 && 
	// ͸�� �� �㶨���Ǹ�����
   	gl_FragColor=finalColorDay; 
  }else if(finalColorNight[2] >= 0.5){
   	// ֻҪ ���ּ� ���־�ȡ �ּ�
  	gl_FragColor=finalColorNight; 
  } else {
  	gl_FragColor=finalColorDay;   	
  }
}              