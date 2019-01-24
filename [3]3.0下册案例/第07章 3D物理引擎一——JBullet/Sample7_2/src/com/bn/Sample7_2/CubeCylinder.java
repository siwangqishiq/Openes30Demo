package com.bn.Sample7_2;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
public class CubeCylinder 
{
	Cylinder cl;//���ڻ��Ƶ�Բ��
	TexCube tc;//���ڻ��Ƶ�����������
	float halfSize;//��߳�
	RigidBody body;//��Ӧ�ĸ������
	MySurfaceView mv;//MySurfaceView������
	
	public CubeCylinder(MySurfaceView mv,float halfSize,CollisionShape[] csa,
			DiscreteDynamicsWorld dynamicsWorld,float mass,float cx,float cy,float cz,int[] mProgram)
	{	
		CompoundShape comShape=new CompoundShape(); //���������״����
		Transform localTransform = new Transform();//�����任����
		localTransform.setIdentity();//��ʼ���任 
		localTransform.origin.set(new Vector3f(0, 0, 0));//���ñ任��ƽ�Ʋ���
		comShape.addChildShape(localTransform, csa[0]);//�������״�����ӣ�
		comShape.addChildShape(localTransform, csa[1]);//�������״	��Χ��x���Բ����
		localTransform = new Transform();//�����任����
		localTransform.basis.rotX((float)Math.toRadians(90));//��x��ת90
		comShape.addChildShape(localTransform, csa[2]);//�������״��Χ��y���Բ����
		boolean isDynamic = (mass != 0f);//�жϸ����Ƿ���˶�
		Vector3f localInertia = new Vector3f(0, 0, 0);//������Ź��Ե�����
		if (isDynamic) //�����������˶�
		{
			comShape.calculateLocalInertia(mass, localInertia);//�������Ĺ���
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//��ʼ���任����
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ø���ĳ�ʼλ��
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);//����������˶�״̬����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo//��������������Ϣ����
		(
				mass, myMotionState, comShape, localInertia
		);
		body = new RigidBody(rbInfo);//�����������
		body.setRestitution(0.6f);//���÷���ϵ��
		body.setFriction(0.8f); //����Ħ��ϵ��
		dynamicsWorld.addRigidBody(body);//��������ӽ���������
		tc=new TexCube(halfSize,mProgram[0]);//��������������������
		cl=new Cylinder(halfSize/2,halfSize*3.6f,16,mProgram[0],mv);//����������Բ��		
		this.halfSize=halfSize;//�����߳�
		this.mv=mv;//����MySurfaceView������
	}
	public void drawSelf(int[] texIda,int[] texIdb)//���Ʒ���
	{
		int texId1=texIda[0];//�����������˶�ʱ������
		int texId2=texIdb[1];//Բ���˶�ʱ������
		if(!body.isActive())//�����徲ֹ
		{
			texId1=texIda[1];//�����徲ֹʱ������
			texId2=texIdb[0];//Բ����ֹʱ������
		}
		MatrixState.pushMatrix();//�����ֳ�
		Transform trans=body.getMotionState().getWorldTransform(new Transform());//��ȡ��������Ӧ����ĵı任��Ϣ����
		MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);//����ƽ�Ʊ任
		Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ��ת�任����Ϣ������Ԫ��
		if(ro.x!=0||ro.y!=0||ro.z!=0)//����Ԫ��3����ķ�������Ϊ0
		{
			if(!Float.isNaN(ro.x)&&!Float.isNaN(ro.y)&&!Float.isNaN(ro.z)
					&&!Float.isInfinite(ro.x)&&!Float.isInfinite(ro.y)&&!Float.isInfinite(ro.z))
			{
				float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
				MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);
			}
		}
		tc.drawSelf( texId1);//��������������
		MatrixState.pushMatrix();//�����ֳ�
		MatrixState.rotate(90,0, 0, 1);//��z����ת90��
		MatrixState.translate(0, -halfSize*1.8f, 0);//��y�����ƽ��
		cl.drawSelf(texId2,  texId2,  texId2);//������X��Բ��	
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
		MatrixState.translate(0, -halfSize*1.8f, 0);//��y�����ƽ��
		cl.drawSelf(texId2,  texId2,  texId2);//������Y���Բ��
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.popMatrix();//�ָ��ֳ�
	}
}
