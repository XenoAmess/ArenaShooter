package arenashooter.engine.math;

/**
 * Mutable 4*4 matrix of floats
 */
public class Mat4f implements Mat4fi {

//	public float 	m00, m10, m20, m30,
//					m01, m11, m21, m31,
//					m02, m12, m22, m32,
//					m03, m13, m23, m33;
	
	/*
	 * float[i][j]:
	 * i0j0, i1j0, i2j0, i3j0
	 * i0j1, i1j1, i2j1, i3j1
	 * i0j2, i1j2, i2j2, j3j2
	 * i0j3, i1j3, i2j3, i3j3
	 */
	public float[][] val = new float[4][4];
	
	public Mat4f() {}
	
	public Mat4f( Mat4fi m ) {
		val = new float[][]{
			{m.m00(), m.m10(), m.m20(), m.m30()},
			{m.m01(), m.m11(), m.m21(), m.m31()},
			{m.m02(), m.m12(), m.m22(), m.m32()},
			{m.m03(), m.m13(), m.m23(), m.m33()}
		};
	}
	
	public float m00() { return val[0][0]; }
	public float m10() { return val[1][0]; }
	public float m20() { return val[2][0]; }
	public float m30() { return val[3][0]; }
	
	public float m01() { return val[0][1]; }
	public float m11() { return val[1][1]; }
	public float m21() { return val[2][1]; }
	public float m31() { return val[3][1]; }
	
	public float m02() { return val[0][2]; }
	public float m12() { return val[1][2]; }
	public float m22() { return val[2][2]; }
	public float m32() { return val[3][2]; }
	
	public float m03() { return val[0][3]; }
	public float m13() { return val[1][3]; }
	public float m23() { return val[2][3]; }
	public float m33() { return val[3][3]; }
	
	@Override
	public float[] toArray(float[] target) {
		for(int j=0; j<4; j++)
			for(int i=0; i<4; i++)
				target[ (i*4)+j ] = val[i][j];
		
		return target;
	}
	
	public static Mat4f identity() {
		Mat4f res = new Mat4f();
//		res.m00 = res.m11 = res.m22 = res.m33 = 1;
		res.val[0][0] = 1;
		res.val[1][1] = 1;
		res.val[2][2] = 1;
		res.val[3][3] = 1;
		return res;
	}
	
	/**
	 * Set <i>this</i> to identity and return it
	 * @return <i>this</i> (modified)
	 */
	public Mat4f setToIdentity() {
		val[0][0] = 1;
		val[0][1] = 0;
		val[0][2] = 0;
		val[0][3] = 0;

		val[1][0] = 0;
		val[1][1] = 1;
		val[1][2] = 0;
		val[1][3] = 0;

		val[2][0] = 0;
		val[2][1] = 0;
		val[2][2] = 1;
		val[2][3] = 0;

		val[3][0] = 0;
		val[3][1] = 0;
		val[3][2] = 0;
		val[3][3] = 1;
		
		return this;
	}

	/**
	 * Create a rotation matrix
	 * @param q unit quaternion
	 * @return
	 */
	public static Mat4f rotation(QuatI q) {
		Mat4f res = new Mat4f();
		
		double ww = q.w() * q.w();
        double xx = q.x() * q.x();
        double yy = q.y() * q.y();
        double zz = q.z() * q.z();
        double zw = q.z() * q.w();
        double xy = q.x() * q.y();
        double xz = q.x() * q.z();
        double yw = q.y() * q.w();
        double yz = q.y() * q.z();
        double xw = q.x() * q.w();
		
		//First column
        res.val[0][0] = (float) (ww + xx - zz - yy);
        res.val[0][1] = (float) (xy + zw + zw + xy);
        res.val[0][2] = (float) (xz - yw + xz - yw);
		
		//Second column
        res.val[1][0] = (float) (-zw + xy - zw + xy);
        res.val[1][1] = (float) (yy - zz + ww - xx);
        res.val[1][2] = (float) (yz + yz + xw + xw);

		//Third column
        res.val[2][0] = (float) (yw + xz + xz + yw);
        res.val[2][1] = (float) (yz + yz - xw - xw);
        res.val[2][2] = (float) (zz - yy - xx + ww);
		
        //Fourth column
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Create a rotation matrix
	 * @param angle
	 * @return
	 */
	public static Mat4f rotation(float angle) {
		Mat4f res = new Mat4f();
		
		float w = (float)Math.cos(-angle/2);
		float z = (float)Math.sin(-angle/2);
		
		double ww = w * w;
        double zz = z * z;
        double zw = z * w;
		
		//First column
        res.val[0][0] = (float) (ww - zz);
        res.val[0][1] = (float) (zw + zw);
		
		//Second column
        res.val[1][0] = (float) (-zw - zw);
        res.val[1][1] = (float) (-zz + ww);

		//Third column
        res.val[2][2] = (float) (zz + ww);
		
        //Fourth column
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Create a translation matrix
	 * @param v
	 * @return
	 */
	public static Mat4f translation(Vec3fi v) {
		Mat4f res = identity();
		
		res.val[3][0] = v.x();
		res.val[3][1] = v.y();
		res.val[3][2] = v.z();
		
		return res;
	}
	
	/**
	 * Create a translation matrix
	 * @param v
	 * @return
	 */
	public static Mat4f translation(Vec2fi v) {
		Mat4f res = identity();
		
		res.val[3][0] = v.x();
		res.val[3][1] = v.y();
		
		return res;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @return
	 */
	public static Mat4f scale(Vec3fi v) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = v.x();
		res.val[1][1] = v.y();
		res.val[2][2] = v.z();
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @return
	 */
	public static Mat4f scale(Vec2fi v) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = v.x();
		res.val[1][1] = v.y();
		res.val[2][2] = 1;
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Create a transform matrix for a 3D object
	 * @param loc
	 * @param rot
	 * @param scale
	 * @return
	 */
	public static Mat4f transform( Vec3fi loc, QuatI rot, Vec3fi scale ) {
		Mat4f res = new Mat4f();
		mul(translation(loc), rotation(rot), res);
		mul(res, scale(scale), res);
		return res;
	}
	
	/**
	 * Create a transform matrix for a 2D object and stores the result in <i>target</i>
	 * <br/> Avoids object creation
	 * @param loc
	 * @param rot
	 * @param scale
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f transform( Vec2fi loc, double rot, Vec2fi scale, Mat4f target ) {
		double w = Math.cos(rot/2);
		double z = Math.sin(rot/2);
		
		double ww = w * w;
		double zz = z * z;
		double zw2 = 2*(z * w);
        
        //First column
        target.val[0][0] = (float)((ww - zz)*scale.x());
        target.val[0][1] = (float)(zw2*scale.x());
        target.val[0][2] = 0;
        target.val[0][3] = 0;
		//Second column
        target.val[1][0] = (float)(-zw2*scale.y());
        target.val[1][1] = (float)((-zz + ww)*scale.y());
        target.val[1][2] = 0;
        target.val[1][3] = 0;
		//Third column
        target.val[2][0] = 0;
        target.val[2][1] = 0;
        target.val[2][2] = (float)(zz + ww);
        target.val[2][3] = 0;
        //Fourth column
        target.val[3][0] = loc.x();
        target.val[3][1] = loc.y();
        target.val[3][2] = 0;
        target.val[3][3] = 1;
        
		return target;
	}
	
	/**
	 * Create a view matrix
	 * @param loc
	 * @param rot
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f viewMatrix(Vec3fi loc, QuatI rot, Mat4f target) {
		target.setToIdentity();
		target.val[3][0] = -loc.x();
		target.val[3][1] = -loc.y();
		target.val[3][2] = -loc.z();
		return mul(rotation(Quat.conjugate(rot)), target, target); //TODO: Remove new Quat() caused by conjugate
	}
	
	/**
	 * Transpose a matrix
	 * @param m
	 * @return m transposed
	 */
	public static Mat4f transpose( Mat4fi m ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = m.m00();
		res.val[1][0] = m.m01();
		res.val[2][0] = m.m02();
		res.val[3][0] = m.m03();
		
		res.val[0][1] = m.m10();
		res.val[1][1] = m.m11();
		res.val[2][1] = m.m12();
		res.val[3][1] = m.m13();
		
		res.val[0][2] = m.m20();
		res.val[1][2] = m.m21();
		res.val[2][2] = m.m22();
		res.val[3][2] = m.m23();
		
		res.val[0][3] = m.m30();
		res.val[1][3] = m.m31();
		res.val[2][3] = m.m32();
		res.val[3][3] = m.m33();
		
		return res;
	}
	
	/**
	 * Creates a symmetric perspective projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param yFOV vertical field of view (degrees)
	 * @param ratio aspect ratio (width/height)
	 * @return the projection matrix
	 */
	public static Mat4f perspective( float near, float far, float yFOV, float ratio ) {
		Mat4f res = new Mat4f();
		
		float top = (float) (Math.tan(Math.toRadians(yFOV)/2)*near);
		float right = top*ratio;
		res.val[0][0] = near/right;
		res.val[1][1] = near/-top;
		res.val[2][2] = -(far+near)/(far-near);
		res.val[3][2] = -(2*far*near)/(far-near);
		res.val[2][3] = -1;
		
		return res;
	}
	
	/**
	 * Creates an orthographic projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param left 
	 * @param bottom 
	 * @param right 
	 * @param top 
	 * @return the projection matrix
	 */
	public static Mat4f ortho( float near, float far, float left, float bottom, float right, float top ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = 2f/(right-left);
		res.val[1][1] = 2f/(top-bottom);
		res.val[2][2] = -2f/(far-near);
		res.val[3][3] = 1f;
		
		res.val[3][0] = -(right+left)/(right-left);
		res.val[3][1] = -(top+bottom)/(top-bottom);
		res.val[3][2] = -(far+near)/(far-near);
		
		return res;
	}
	
	/**
	 * Multiplies 2 matrices and stores the result in <i>target</i>
	 * @param m1
	 * @param m2
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2, Mat4f target ) {
//		float[][] res = new float[4][4];
//		
//		for( int j=0; j<4; j++ )
//			for( int i=0; i<4; i++ )
//				res[i][j] = (m1.val[0][j]*m2.val[i][0])+
//							(m1.val[1][j]*m2.val[i][1])+
//							(m1.val[2][j]*m2.val[i][2])+
//							(m1.val[3][j]*m2.val[i][3]);
//		
//		target.val = res;
//		return target;
		
		target.val[0][0] = m1.m00()*m2.m00() + m1.m10()*m2.m01() + m1.m20()*m2.m02() +  + m1.m30()*m2.m03();
		target.val[1][0] = m1.m00()*m2.m10() + m1.m10()*m2.m11() + m1.m20()*m2.m12() +  + m1.m30()*m2.m13();
		target.val[2][0] = m1.m00()*m2.m20() + m1.m10()*m2.m21() + m1.m20()*m2.m22() +  + m1.m30()*m2.m23();
		target.val[3][0] = m1.m00()*m2.m30() + m1.m10()*m2.m31() + m1.m20()*m2.m32() +  + m1.m30()*m2.m33();
		
		target.val[0][1] = m1.m01()*m2.m00() + m1.m11()*m2.m01() + m1.m21()*m2.m02() +  + m1.m31()*m2.m03();
		target.val[1][1] = m1.m01()*m2.m10() + m1.m11()*m2.m11() + m1.m21()*m2.m12() +  + m1.m31()*m2.m13();
		target.val[2][1] = m1.m01()*m2.m20() + m1.m11()*m2.m21() + m1.m21()*m2.m22() +  + m1.m31()*m2.m23();
		target.val[3][1] = m1.m01()*m2.m30() + m1.m11()*m2.m31() + m1.m21()*m2.m32() +  + m1.m31()*m2.m33();
		
		target.val[0][2] = m1.m02()*m2.m00() + m1.m12()*m2.m01() + m1.m22()*m2.m02() +  + m1.m32()*m2.m03();
		target.val[1][2] = m1.m02()*m2.m10() + m1.m12()*m2.m11() + m1.m22()*m2.m12() +  + m1.m32()*m2.m13();
		target.val[2][2] = m1.m02()*m2.m20() + m1.m12()*m2.m21() + m1.m22()*m2.m22() +  + m1.m32()*m2.m23();
		target.val[3][2] = m1.m02()*m2.m30() + m1.m12()*m2.m31() + m1.m22()*m2.m32() +  + m1.m32()*m2.m33();
		
		target.val[0][3] = m1.m03()*m2.m00() + m1.m13()*m2.m01() + m1.m23()*m2.m02() +  + m1.m33()*m2.m03();
		target.val[1][3] = m1.m03()*m2.m10() + m1.m13()*m2.m11() + m1.m23()*m2.m12() +  + m1.m33()*m2.m13();
		target.val[2][3] = m1.m03()*m2.m20() + m1.m13()*m2.m21() + m1.m23()*m2.m22() +  + m1.m33()*m2.m23();
		target.val[3][3] = m1.m03()*m2.m30() + m1.m13()*m2.m31() + m1.m23()*m2.m32() +  + m1.m33()*m2.m33();
		
		return target;
	}
	
	/**
	 * Multiplies 2 matrices
	 * @param m1
	 * @param m2
	 * @return m1*m2
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2 ) {
		return mul(m1, m2, new Mat4f());
	}
	
	public String toString() {
		String res = "Mat4f:\n";
		
		for( int j=0; j<4; j++ ) {
			for( int i=0; i<4; i++ ) {
				res+=val[i][j];
				if(i<3) res+=", ";
			}
			if(j<3) res+="\n";
		}
		
		return res;
	}
	
	@Override
	public Mat4f clone() {
		return new Mat4f(this);
	}
}
