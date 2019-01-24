#version 300 es
precision mediump float;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���

uniform sampler2D sTexture;//������������
uniform sampler2D depthTexture;//������������
uniform mat4 uViewProjectionInverseMatrix;
uniform mat4 uPreviousProjectionMatrix;
uniform int g_numSamples;

out vec4 fragColor;
void main()
{
   vec2 textureCoord=vTextureCoord;
   //�õ����ش�����Ȼ���ֵ
   float zOverW = texture(depthTexture,textureCoord).r;
   //HΪ�������ڵı�׼�ռ�λ�ã���ΧΪ-1~1
   vec4 H = vec4(textureCoord.x*2.0-1.0,(1.0-textureCoord.y)*2.0-1.0,zOverW,1.0);
   //ͨ���۲�-ͶӰ�����������б任
   vec4 D = uViewProjectionInverseMatrix*H;
   vec4 worldPos= D/D.w;
   //��ǰ�ӿ�λ��
   vec4 currentPos=H;
   //ʹ������λ�ã���ͨ��ǰһ���۲�-ͶӰ������б任
   vec4 previousPos=uPreviousProjectionMatrix*worldPos;
   //ͨ������wת��������ε�(-1,1)
   previousPos=previousPos/previousPos.w;
   //ʹ�õ�ǰ֡��ǰһ֡�е�λ�������������ٶ�
   vec2 velocity=((previousPos-currentPos)/float(g_numSamples)).xy;
   //�õ�������λ�õĳ�ʼ��ɫ
   vec4 color=texture(sTexture, textureCoord);
   textureCoord+=velocity;
   for(int i=1;i<g_numSamples;i++,textureCoord+=velocity)
   {
         //���ٶ���������ɫ������в���
         vec4 currentColor=texture(sTexture, textureCoord);
         //����ǰ��ɫ�ۼӵ���ɫ����
         color+=currentColor;
   }
   //�Բ������ȡƽ�����õ����յ�ģ����ɫ
   fragColor=color/float(g_numSamples);
}