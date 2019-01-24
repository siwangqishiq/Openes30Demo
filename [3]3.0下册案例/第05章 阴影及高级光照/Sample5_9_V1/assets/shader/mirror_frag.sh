#version 300 es
precision mediump float;//ָ������Ĭ�Ͼ���
uniform highp mat4 uMVPMatrixMirror; //����������۲켰ͶӰ��Ͼ��� 
uniform sampler2D sTexture;//������������
in vec4 vPosition;//�������Զ�����ɫ����ƬԪλ������
out vec4 fragColor;//���ݵ���Ⱦ���ߵ�ƬԪ��ɫ
void main()                         
{    
 	//��ƬԪ��λ��ͶӰ������������Ľ�ƽ����
   vec4 gytyPosition=uMVPMatrixMirror * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//����͸�ӳ���
   float s=(gytyPosition.s+1.0)/2.0;//��ͶӰ������껻��Ϊ��������
   float t=(gytyPosition.t+1.0)/2.0;
  
   //�����������ɫ����ƬԪ
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0)
   {
   		//�Ծ�������ͼ���в���
      	vec4 finalColor=texture(sTexture, vec2(s,t));    
   		//�������յ�ƬԪ��ɫֵ
   		fragColor = finalColor;
   }else{
   		fragColor=vec4(0.0,0.0,0.0,1.0);//�������յ�ƬԪ��ɫֵ
   }
}   