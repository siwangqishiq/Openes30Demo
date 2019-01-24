package com.bn.Sample7_8;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bn.Sample7_8.util.LoadedObjectVertexNormalTexture;
import com.bn.Sample7_8.util.MatrixState;
import com.bn.Sample7_8.util.SYSUtil;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

//������
public class Box {
	//������������
	float rx;
	float ry;
	float rz;
	float v_x = 1f; //����
    float v_y = 1f;//����
    float v_z = 1f;//����
	DynamicsWorld dynamicsWorld;//��������  
	LoadedObjectVertexNormalTexture rbobj;//����ģ��
	int vehicleTextureId;//����id
	RigidBody rb;
	float []f = new float[4];
	
	public Box(float rx, float ry, float rz, float rbangle,DynamicsWorld dynamicsWorld,
			LoadedObjectVertexNormalTexture rbobj, int vehicleTextureId,CollisionShape rbShape) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		//this.rl = rl;
		//this.rw = rw;
		//this.rh = rh;
		this.dynamicsWorld = dynamicsWorld;
		this.rbobj = rbobj;
		this.vehicleTextureId = vehicleTextureId;
		
	//CollisionShape rbShape = new BoxShape(new Vector3f(v_x/2, v_y/2, v_z/2));
		Transform rbTransform = new Transform();	//��������ĳ�ʼ�任����
		rbTransform.setIdentity();					//�Գ�ʼ�任�����ʼ��
		rbTransform.origin.set(rx,ry,rz);			//�ƶ�����
		//��Ԫ����ת
		f[2] = 1;
		if(rbangle!=0){
		 Quat4f q = new Quat4f(0,0,0,1f);				 //�½���Ԫ��
		 AxisAngle4f aa = new AxisAngle4f(0, 1, 0, rbangle); //����·��Ҫת�ĽǶȸ���Ԫ����ֵ
		 q.set(aa);
		 f = SYSUtil.fromSYStoAXYZ(q);				 //����Ԫ����Ϊ�������ʾ	
		 rbTransform.setRotation(q);                  	 //�Գ�ʼ�任����ֵ
		}
		rb = localCreateRigidBody(10, rbTransform, rbShape);//��ø���
	} 
	
	public RigidBody localCreateRigidBody(float mass, 
			Transform startTransform, CollisionShape shape) {
			boolean isDynamic = (mass != 0f);					//�����Ƿ�����ƶ�
			Vector3f localInertia = new Vector3f(0f, 0f, 0f);
			if (isDynamic) {									//���������ƶ�
				shape.calculateLocalInertia(mass, localInertia);		//�������
			}
			//����������˶�״̬����
			DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
			//��������������Ϣ����
			RigidBodyConstructionInfo cInfo = 
			new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
			RigidBody body = new RigidBody(cInfo);				//�½��������
			//����Ħ��ϵ��
			body.setFriction(0.8f);
			body.setRestitution(0.6f);//���÷���ϵ��
			dynamicsWorld.addRigidBody(body);				//�Ѵ˸�����뵽����������
			return body;									//���ظ���
			}
			public void drawSelf(){		
				MatrixState.pushMatrix();							//�����ֳ�
				//���������ǰ���ƶ�����
				Transform trans = rb.getMotionState().getWorldTransform(new Transform());
				//������λ�任
				MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
				//System.out.println("////"+trans.origin.y);
				Quat4f ro=trans.getRotation(new Quat4f());			//��ȡ��ǰ�任����ת��Ϣ
				if(ro.x!=0||ro.y!=0||ro.z!=0){
					float[] fa=SYSUtil.fromSYStoAXYZ(ro);			//����Ԫ��ת����AXYZ����ʽ
					MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);			//ִ����ת
				}
				//���Ƴ������
				rbobj.drawSelf(vehicleTextureId);
				MatrixState.popMatrix();							//�ָ��ֳ�
									//�ָ��ֳ�
			}
			public void clientResetScene() {
				Transform tr = new Transform();
				tr.setIdentity();
				tr.origin.set(this.rx,this.ry,this.rz);
				//��Ԫ����ת

				Quat4f q = new Quat4f(0,0,0,1f);
				tr.setRotation(q);
				rb.setCenterOfMassTransform(tr); //������������
				rb.setLinearVelocity(new Vector3f(0, 0, 0));//�������ٶ�
				rb.setAngularVelocity(new Vector3f(0, 0, 0));//���ý��ٶ�
				dynamicsWorld.getBroadphase().getOverlappingPairCache().
				cleanProxyFromPairs(rb.getBroadphaseHandle(), dynamicsWorld.getDispatcher());
			}
}