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
	public final float[][] val;
	
	public Mat4f() {
		val = new float[4][4];
	}
	
	/**
	 * Clones a matrix
	 * @param m
	 */
	public Mat4f( Mat4fi m ) {
		val = new float[][]{
			{m.m00(), m.m10(), m.m20(), m.m30()},
			{m.m01(), m.m11(), m.m21(), m.m31()},
			{m.m02(), m.m12(), m.m22(), m.m32()},
			{m.m03(), m.m13(), m.m23(), m.m33()}
		};
	}
	
	@Override
	public float m00() { return val[0][0]; }
	@Override
	public float m10() { return val[1][0]; }
	@Override
	public float m20() { return val[2][0]; }
	@Override
	public float m30() { return val[3][0]; }

	@Override
	public float m01() { return val[0][1]; }
	@Override
	public float m11() { return val[1][1]; }
	@Override
	public float m21() { return val[2][1]; }
	@Override
	public float m31() { return val[3][1]; }

	@Override
	public float m02() { return val[0][2]; }
	@Override
	public float m12() { return val[1][2]; }
	@Override
	public float m22() { return val[2][2]; }
	@Override
	public float m32() { return val[3][2]; }

	@Override
	public float m03() { return val[0][3]; }
	@Override
	public float m13() { return val[1][3]; }
	@Override
	public float m23() { return val[2][3]; }
	@Override
	public float m33() { return val[3][3]; }
	
	@Override
	public float[] toArray(float[] target) {
		for(int j=0; j<4; j++)
			for(int i=0; i<4; i++)
				target[ (i*4)+j ] = val[i][j];
		
		return target;
	}
	
	/**
	 * @return new identity matrix
	 */
	public static Mat4f identity() {
		Mat4f res = new Mat4f();
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
	public Mat4f toIdentity() {
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
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f rotation(QuatI q, Mat4f target) {
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
        target.val[0][0] = (float) (ww + xx - zz - yy);
        target.val[0][1] = (float) (xy + zw + zw + xy);
        target.val[0][2] = (float) (xz - yw + xz - yw);
        target.val[0][3] = 0;
		
		//Second column
        target.val[1][0] = (float) (-zw + xy - zw + xy);
        target.val[1][1] = (float) (yy - zz + ww - xx);
        target.val[1][2] = (float) (yz + yz + xw + xw);
        target.val[1][3] = 0;

		//Third column
        target.val[2][0] = (float) (yw + xz + xz + yw);
        target.val[2][1] = (float) (yz + yz - xw - xw);
        target.val[2][2] = (float) (zz - yy - xx + ww);
        target.val[2][3] = 0;
		
        //Fourth column
        target.val[3][0] = 0;
        target.val[3][1] = 0;
        target.val[3][2] = 0;
        target.val[3][3] = 1;
		
		return target;
	}
	
	/**
	 * Create a rotation matrix
	 * @param angle
	 * @return new matrix object
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
	 * Rotate this matrix
	 * @param q rotation quaternion
	 * @return <i>this</i> rotated
	 */
	public Mat4f rotate(QuatI q) {
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
        double r00 = ww + xx - zz - yy;
        double r01 = xy + zw + zw + xy;
        double r02 = xz - yw + xz - yw;
        
        //Second column
        double r10 = -zw + xy - zw + xy;
        double r11 = yy - zz + ww - xx;
        double r12 = yz + yz + xw + xw;

        //Third column
        double r20 = yw + xz + xz + yw;
        double r21 = yz + yz - xw - xw;
        double r22 = zz - yy - xx + ww;
        
        float m00 = (float) (m00()*r00 + m10()*r01 + m20()*r02);
        float m10 = (float) (m00()*r10 + m10()*r11 + m20()*r12);
        float m20 = (float) (m00()*r20 + m10()*r21 + m20()*r22);
        
        float m01 = (float) (m01()*r00 + m11()*r01 + m21()*r02);
        float m11 = (float) (m01()*r10 + m11()*r11 + m21()*r12);
        float m21 = (float) (m01()*r20 + m11()*r21 + m21()*r22);
        
        float m02 = (float) (m02()*r00 + m12()*r01 + m22()*r02);
        float m12 = (float) (m02()*r10 + m12()*r11 + m22()*r12);
        float m22 = (float) (m02()*r20 + m12()*r21 + m22()*r22);
        
        float m03 = (float) (m03()*r00 + m13()*r01 + m23()*r02);
        float m13 = (float) (m03()*r10 + m13()*r11 + m23()*r12);
        float m23 = (float) (m03()*r20 + m13()*r21 + m23()*r22);
        
        val[0][0] = m00;
        val[1][0] = m10;
        val[2][0] = m20;
        
        val[0][1] = m01;
        val[1][1] = m11;
        val[2][1] = m21;
        
        val[0][2] = m02;
        val[1][2] = m12;
        val[2][2] = m22;
        
        val[0][3] = m03;
        val[1][3] = m13;
        val[2][3] = m23;

        return this;
	}
	
	/**
	 * <i>Target</i> becomes a translation matrix for <i>v</i>
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f translation(Vec2fi v, Mat4f target) {
		target.toIdentity();

		target.val[3][0] = v.x();
		target.val[3][1] = v.y();
		
		return target;
	}
	
	/**
	 * <i>Target</i> becomes a translation matrix for <i>v</i>
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f translation(Vec3fi v, Mat4f target) {
		target.toIdentity();

		target.val[3][0] = v.x();
		target.val[3][1] = v.y();
		target.val[3][2] = v.z();
		
		return target;
	}
	
	/**
	 * Translate this matrix
	 * @param v translation vector
	 * @return <i>this</i> translated
	 */
	public Mat4f translate(Vec2fi v) {
		val[3][0] = val[0][0]*v.x() + val[1][0]*v.y() + val[3][0];
		val[3][1] = val[0][1]*v.x() + val[1][1]*v.y() + val[3][1];
		val[3][2] = val[0][2]*v.x() + val[1][2]*v.y() + val[3][2];
		val[3][3] = val[0][3]*v.x() + val[1][3]*v.y() + val[3][3];

		return this;
	}
	
	/**
	 * Translate this matrix
	 * @param v translation vector
	 * @return <i>this</i> translated
	 */
	public Mat4f translate(Vec3fi v) {
		val[3][0] = val[0][0]*v.x() + val[1][0]*v.y() + val[2][0]*v.z() + val[3][0];
		val[3][1] = val[0][1]*v.x() + val[1][1]*v.y() + val[2][1]*v.z() + val[3][1];
		val[3][2] = val[0][2]*v.x() + val[1][2]*v.y() + val[2][2]*v.z() + val[3][2];
		val[3][3] = val[0][3]*v.x() + val[1][3]*v.y() + val[2][3]*v.z() + val[3][3];

		return this;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f scaling(Vec3fi v, Mat4f target) {
		target.toIdentity();
		
		target.val[0][0] = v.x();
		target.val[1][1] = v.y();
		target.val[2][2] = v.z();
		
		return target;
	}
	
	/**
	 * Scale this matrix
	 * @param v scaling vector
	 * @return <i>this</i> scaled
	 */
	public Mat4f scale(Vec2fi v) {
		val[0][0] *= v.x();
		val[1][0] *= v.y();
		
		val[0][1] *= v.x();
		val[1][1] *= v.y();
		
		val[0][2] *= v.x();
		val[1][2] *= v.y();
		
		val[0][3] *= v.x();
		val[1][3] *= v.y();
		
		return this;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f scaling(Vec2fi v, Mat4f target) {
		target.toIdentity();
		
		target.val[0][0] = v.x();
		target.val[1][1] = v.y();

		return target;
	}
	
	/**
	 * Scale this matrix
	 * @param v scaling vector
	 * @return <i>this</i> scaled
	 */
	public Mat4f scale(Vec3fi v) {
		val[0][0] *= v.x();
		val[1][0] *= v.y();
		val[2][0] *= v.z();
		
		val[0][1] *= v.x();
		val[1][1] *= v.y();
		val[2][1] *= v.z();
		
		val[0][2] *= v.x();
		val[1][2] *= v.y();
		val[2][2] *= v.z();
		
		val[0][3] *= v.x();
		val[1][3] *= v.y();
		val[2][3] *= v.z();
		
		return this;
	}
	
	/**
	 * Create a transform matrix for a 3D object
	 * @param loc
	 * @param rot
	 * @param scale
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f transform( Vec3fi loc, QuatI rot, Vec3fi scale, Mat4f target ) {
		return target.toIdentity().translate(loc).rotate(rot).scale(scale);
	}
	
	/**
	 * Create a transform matrix for a 2D object
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
	 * Set <i>this</i> to a view matrix
	 * @param loc
	 * @param rot
	 * @return <i>this</i> as a view matrix
	 */
	public Mat4f viewMatrix(Vec3fi loc, QuatI rot) {
		toIdentity();
		val[3][0] = -loc.x();
		val[3][1] = -loc.y();
		val[3][2] = -loc.z();
		rotate( Quat.conjugate(rot) );
		return this;
	}
	
	/**
	 * Set <i>this</i> to a symmetric perspective projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param yFOV vertical field of view (degrees)
	 * @param ratio aspect ratio (width/height)
	 * @param target
	 * @return <i>this</i> as a perspective projection matrix
	 */
	public Mat4f perspective( float near, float far, float yFOV, float ratio ) {
		toIdentity();
		
		float top = (float) (Math.tan(Math.toRadians(yFOV)/2)*near);
		float right = top*ratio;
		val[0][0] = near/right;
		val[1][1] = near/-top;
		val[2][2] = -(far+near)/(far-near);
		val[3][2] = -(2*far*near)/(far-near);
		val[2][3] = -1;
		
		return this;
	}
	
	/**
	 * Set <i>this</i> to an orthographic projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param left 
	 * @param bottom 
	 * @param right 
	 * @param top 
	 * @param target
	 * @return <i>this</i> as an orthographic projection matrix
	 */
	public Mat4f ortho( float near, float far, float left, float bottom, float right, float top ) {
		toIdentity();
		
		val[0][0] = 2f/(right-left);
		val[1][1] = 2f/(top-bottom);
		val[2][2] = -2f/(far-near);
		val[3][3] = 1f;
		
		val[3][0] = -(right+left)/(right-left);
		val[3][1] = -(top+bottom)/(top-bottom);
		val[3][2] = -(far+near)/(far-near);
		
		return this;
	}
	
	/**
	 * Multiplies 2 matrices and stores the result in <i>target</i>
	 * @param m1
	 * @param m2
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2, Mat4f target ) {
		float[] val1 = m1.toArray(new float[16]), val2 = m2.toArray(new float[16]);
		
		target.val[0][0] = val1[0]*val2[0]  + val1[4]*val2[1]  + val1[8]*val2[2]  + val1[12]*val2[3];
		target.val[1][0] = val1[0]*val2[4]  + val1[4]*val2[5]  + val1[8]*val2[6]  + val1[12]*val2[7];
		target.val[2][0] = val1[0]*val2[8]  + val1[4]*val2[9]  + val1[8]*val2[10] + val1[12]*val2[11];
		target.val[3][0] = val1[0]*val2[12] + val1[4]*val2[13] + val1[8]*val2[14] + val1[12]*val2[15];
		
		target.val[0][1] = val1[1]*val2[0]  + val1[5]*val2[1]  + val1[9]*val2[2]  + val1[13]*val2[3];
		target.val[1][1] = val1[1]*val2[4]  + val1[5]*val2[5]  + val1[9]*val2[6]  + val1[13]*val2[7];
		target.val[2][1] = val1[1]*val2[8]  + val1[5]*val2[9]  + val1[9]*val2[10] + val1[13]*val2[11];
		target.val[3][1] = val1[1]*val2[12] + val1[5]*val2[13] + val1[9]*val2[14] + val1[13]*val2[15];
		
		target.val[0][2] = val1[2]*val2[0]  + val1[6]*val2[1]  + val1[10]*val2[2]  + val1[14]*val2[3];
		target.val[1][2] = val1[2]*val2[4]  + val1[6]*val2[5]  + val1[10]*val2[6]  + val1[14]*val2[7];
		target.val[2][2] = val1[2]*val2[8]  + val1[6]*val2[9]  + val1[10]*val2[10] + val1[14]*val2[11];
		target.val[3][2] = val1[2]*val2[12] + val1[6]*val2[13] + val1[10]*val2[14] + val1[14]*val2[15];
		
		target.val[0][3] = val1[3]*val2[0]  + val1[7]*val2[1]  + val1[11]*val2[2]  + val1[15]*val2[3];
		target.val[1][3] = val1[3]*val2[4]  + val1[7]*val2[5]  + val1[11]*val2[6]  + val1[15]*val2[7];
		target.val[2][3] = val1[3]*val2[8]  + val1[7]*val2[9]  + val1[11]*val2[10] + val1[15]*val2[11];
		target.val[3][3] = val1[3]*val2[12] + val1[7]*val2[13] + val1[11]*val2[14] + val1[15]*val2[15];
		
		return target;
	}
	
	/**
	 * Multiplies 2 matrices
	 * @param m1
	 * @param m2
	 * @return m1*m2
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2 ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = m1.m00()*m2.m00() + m1.m10()*m2.m01() + m1.m20()*m2.m02() + m1.m30()*m2.m03();
		res.val[1][0] = m1.m00()*m2.m10() + m1.m10()*m2.m11() + m1.m20()*m2.m12() + m1.m30()*m2.m13();
		res.val[2][0] = m1.m00()*m2.m20() + m1.m10()*m2.m21() + m1.m20()*m2.m22() + m1.m30()*m2.m23();
		res.val[3][0] = m1.m00()*m2.m30() + m1.m10()*m2.m31() + m1.m20()*m2.m32() + m1.m30()*m2.m33();
		
		res.val[0][1] = m1.m01()*m2.m00() + m1.m11()*m2.m01() + m1.m21()*m2.m02() + m1.m31()*m2.m03();
		res.val[1][1] = m1.m01()*m2.m10() + m1.m11()*m2.m11() + m1.m21()*m2.m12() + m1.m31()*m2.m13();
		res.val[2][1] = m1.m01()*m2.m20() + m1.m11()*m2.m21() + m1.m21()*m2.m22() + m1.m31()*m2.m23();
		res.val[3][1] = m1.m01()*m2.m30() + m1.m11()*m2.m31() + m1.m21()*m2.m32() + m1.m31()*m2.m33();
		
		res.val[0][2] = m1.m02()*m2.m00() + m1.m12()*m2.m01() + m1.m22()*m2.m02() + m1.m32()*m2.m03();
		res.val[1][2] = m1.m02()*m2.m10() + m1.m12()*m2.m11() + m1.m22()*m2.m12() + m1.m32()*m2.m13();
		res.val[2][2] = m1.m02()*m2.m20() + m1.m12()*m2.m21() + m1.m22()*m2.m22() + m1.m32()*m2.m23();
		res.val[3][2] = m1.m02()*m2.m30() + m1.m12()*m2.m31() + m1.m22()*m2.m32() + m1.m32()*m2.m33();
		
		res.val[0][3] = m1.m03()*m2.m00() + m1.m13()*m2.m01() + m1.m23()*m2.m02() + m1.m33()*m2.m03();
		res.val[1][3] = m1.m03()*m2.m10() + m1.m13()*m2.m11() + m1.m23()*m2.m12() + m1.m33()*m2.m13();
		res.val[2][3] = m1.m03()*m2.m20() + m1.m13()*m2.m21() + m1.m23()*m2.m22() + m1.m33()*m2.m23();
		res.val[3][3] = m1.m03()*m2.m30() + m1.m13()*m2.m31() + m1.m23()*m2.m32() + m1.m33()*m2.m33();
		
		return res;
	}

	@Override
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
