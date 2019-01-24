package com.bn.Sample7_4;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
public class LoadRigidBody extends BNThing
{
	LoadedObjectVertexNormal lovo;//���������������
	RigidBody body;//��Ӧ�ĸ����������
	int mProgram;//��ɫ����������
	public LoadRigidBody(int mProgram,float mass,LoadedObjectVertexNormal lovo,float cx,float cy,float cz,DiscreteDynamicsWorld dynamicsWorld)
	{
		this.lovo=lovo;//������������������
		this.mProgram=mProgram;//������ɫ����������
		CollisionShape colShape=lovo.loadShape;//������ײ��״����
		boolean isDynamic = (mass != 0f);//ȷ�������Ƿ���˶�
		Vector3f localInertia = new Vector3f(0f, 0f, 0f);//������Ź��Ե�����
		if (isDynamic) {
			colShape.calculateLocalInertia(mass, localInertia);//�������
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//��ʼ���任����
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ø���ĳ�ʼλ��
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);		
		//��������������Ϣ����
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo
									(mass, myMotionState, colShape, localInertia);
		body = new RigidBody(cInfo);//�����������		
		body.setRestitution(0.4f);//���÷���ϵ��
		body.setFriction(0.8f);//����Ħ��ϵ��
		dynamicsWorld.addRigidBody(body);//��������ӽ���������
	}
	//���Ƹ���
	public void drawSelf()
	{
		lovo.initShader(mProgram);//��ʼ����ɫ��
		MatrixState.pushMatrix();//�����ֳ�
		//��ȡ�˸����Ӧ�ı任��Ϣ����
		Transform trans = body.getMotionState().getWorldTransform(new Transform());
		//����ƽ�Ʊ任
		MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
		Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ��ת�任����Ϣ������Ԫ��
		if(ro.x!=0||ro.y!=0||ro.z!=0)//����Ԫ��3����ķ�������Ϊ0
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ��ʽ
			MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);//������ת�任
		}
		lovo.drawSelf();//���Ƽ��ص����� 
		MatrixState.popMatrix();//�ָ��ֳ�
	}
}
