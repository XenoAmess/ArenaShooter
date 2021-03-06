package arenashooter.engine.math;

import java.io.IOException;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.Jsonable;

import com.github.cliftonlabs.json_simple.JsonArray;

/**
 * Mutable 3 dimensional vector of floats (x, y, z)
 */
public class Vec3f implements Vec3fi, Jsonable {
	
	public float x, y, z;
	
	/**
	 * Creates a (0, 0, 0) vector
	 */
	public Vec3f() {}
	
	/**
	 * Creates a (a, a, a) vector
	 * @param a
	 */
	public Vec3f(float a) {
		x=a;
		y=a;
		z=a;
	}
	
	/**
	 * Creates a (x, y, z) vector
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a (x, y, z) vector from doubles (values will be casted to float)
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3f(double x, double y, double z) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
	}
	
	/**
	 * Appends z to a Vec2f
	 * @param xy
	 * @param z
	 */
	public Vec3f(Vec2fi xy, float z) {
		x = xy.x();
		y = xy.y();
		this.z = z;
	}
	
	/**
	 * Create a copy of a vector
	 */
	public Vec3f(Vec3fi v) {
		x = v.x();
		y = v.y();
		z = v.z();
	}
	
	@Override
	public float x() { return x; }

	@Override
	public float y() { return y; }

	@Override
	public float z() { return z; }
	
	/**
	 * This becomes Other
	 * @param other vector to copy
	 */
	public void set(Vec3fi other) {
		x = other.x();
		y = other.y();
		z = other.z();
	}
	
	/**
	 * <i>This</i> becomes (x, y, z) and is returned
	 * @param x
	 * @param y
	 * @param z
	 * @return <i>this</i> (modified)
	 */
	public Vec3f set(double x, double y, double z) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
		return this;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 * @return <i>this</i> (modified)
	 */
	public Vec3f add(Vec3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	
	/**
	 * Multiplies the vector.
	 * This becomes this*a
	 * @param a
	 * @return <i>this</i> (modified)
	 */
	public Vec3f multiply( float a ) {
		x *= a;
		y *= a;
		z *= a;
		return this;
	}
	
	@Override
	public double length() {
		return Math.sqrt( (x*x)+(y*y)+(z*z) );
	}
	
	/**
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0,0, 0) if length is 0
	 */
	public static Vec3f normalize( Vec3fi v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec3f();
		
		return new Vec3f( v.x()/len, v.y()/len, v.z()/len );
	}

	@Override
	public Vec3f clone() {
		return new Vec3f(x, y, z);
	}
	
	@Override
	public float[] toArray(float[] target) {
		target[0] = x;
		target[1] = y;
		target[2] = z;
		return target;
	}

	@Override
	public String toString() { return "( "+x+", "+y+", "+z+" )"; }
	
	//
	//Static functions
	//

	/**
	 * Add two vectors together, store the result in target and return it
	 * <br/> Avoids object creation
	 * 
	 * @param a
	 * @param b
	 * @param target
	 * @return target (a+b)
	 */
	public static Vec3f add(Vec3fi a, Vec3fi b, Vec3f target) {
		target.x = a.x()+b.x();
		target.y = a.y()+b.y();
		target.z = a.z()+b.z();
		return target;
	}
	
	/**
	 * Subtract two vectors
	 * @param a
	 * @param b
	 * @return a-b (original vectors are unchanged)
	 */
	public static Vec3f subtract(Vec3fi a, Vec3fi b) {
		return new Vec3f( a.x()-b.x(), a.y()-b.y(), a.z()-b.z() );
	}
	
	/**
	 * Multiplies a vector by a double
	 * @param v the vector
	 * @param a the double
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec3f multiply( Vec3fi v, double a ) {
		return new Vec3f( v.x()*a, v.y()*a, v.z()*a );
	}
	
	/**
	 * Cross product of two vectors
	 * @param a
	 * @param b
	 * @return a x b (original vectors are unchanged)
	 */
	public static Vec3f cross( Vec3fi a, Vec3fi b ) {
		Vec3f res = new Vec3f();
		
		res.x = a.y()*b.z() - a.z()*b.y();
		res.y = a.z()*b.x() - a.x()*b.z();
		res.z = a.x()*b.y() - a.y()*b.x();
		
		return res;
	}
	
	/**
	 * Converts a hue-saturation-value color to rgb. Input values are all normalized (0-1 range).
	 * <br/> Result is stored in <i>target</i>
	 * @param h hue
	 * @param s saturation
	 * @param v value
	 * @param target
	 * @return (r, g, b) stored in <i>target</i>
	 */
	public static Vec3f hsvToRgb(float h, float s, float v, Vec3f target) { //TODO: test
		if(s == 0) return target.set(v, v, v); //No saturation, return value
		int i = (int)(h*6);
		float f = (h*6f)-i;
		float p = v*(1f-s);
		float q = v*(1f-s*f);
		float t = v*(1f-s*(1f-f));
		i %= 6;
		switch(i) {
		case 0:
			return target.set(v, t, p);
		case 1:
			return target.set(q, v, p);
		case 2:
			return target.set(p, v, t);
		case 3:
			return target.set(p, q, v);
		case 4:
			return target.set(t, p, v);
		default:
			return target.set(v, p, q);
		}
	}
	
	/**
	 * Convert a color from RGB format to HSV. Input values are all normalized (0-1 range). 
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param target
	 * @return (hue, saturation, value) stored in <i>target</i>
	 */
	public static Vec3f rgbToHsv(float r, float g, float b, Vec3f target) { //TODO: Test
		r = Utils.clampF(r, 0, 1);
		g = Utils.clampF(g, 0, 1);
		b = Utils.clampF(b, 0, 1);
		float max = Math.max(Math.max(r, g), b);
		float min = Math.min(Math.min(r, g), b);

		float delta = max - min;
		
		if(max == 0)
			return target.set(0, 0, 0);

		// Hue
		float d = (r==min) ? g-b : ((b==min) ? r-g : b-r);
		float h = (r==min) ? 3 : ((b==min) ? 1 : 5);
		target.x = (60f*(h - d/delta))/360f;

		// Saturation
		target.y = delta/max;

		// Value
		target.z = max;
			
		return target;
	}
	
	public static Vec3f lerp( Vec3fi a, Vec3fi b, double f, Vec3f target ) {
		target.x = Utils.lerpF(a.x(), b.x(), f);
		target.y = Utils.lerpF(a.y(), b.y(), f);
		target.z = Utils.lerpF(a.z(), b.z(), f);
		return target;
	}
	
	
	/*
	 * JSON
	 */
	
	public static Vec3f jsonImport(JsonArray array) {
		double x = ((Number) array.get(0)).doubleValue();
		double y = ((Number) array.get(1)).doubleValue();
		double z = ((Number) array.get(2)).doubleValue();
		return new Vec3f(x, y, z);
	}

	private JsonArray getJson() {
		return new JsonArray().addChain(x).addChain(y).addChain(z);
	}
	
	@Override
	public String toJson() {
		return getJson().toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		getJson().toJson(writable);
	}
}
