#version 300 es
//�������������ɫ��
precision highp float;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ��Ϊ��ʹ��ͬ�ľ���ֵ��ʾ�����ҶȲ�ͬ����100��
   //ʹ����ֵ�Ķ�Ӧ��ɫֵ��0��1֮�䣬����ֵ����1�Ļ����������ǰ�ɫ��            
   float depthValue=texture(sTexture,vTextureCoord).r/100.0;
   fragColor=vec4(depthValue,depthValue,depthValue,1.0);
}              