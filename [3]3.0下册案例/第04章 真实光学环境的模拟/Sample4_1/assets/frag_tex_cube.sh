#version 300 es
precision mediump float;
uniform samplerCube sTexture;//������������
in vec3 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
out vec4 fragColor;//�������ƬԪ��ɫ
void main() {
    //ͨ������Ĳ�������������ͼ�������texture����ִ�в���
   fragColor=texture(sTexture, vTextureCoord);    
}   
