package arenashooter.engine.graphics.particles;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;

import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.GameMaster;

class EmitterBasic extends Emitter {
	private final EmitterTemplateBasic data;
	final float sizeInitial, sizeEnd;
	
	ArrayList<Vec2f> positions;
	ArrayList<Vec2f> velocities;
	ArrayList<Float> rotations;
	ArrayList<Float> lives;
	ArrayList<Float> livesTotal;
	
	/**
	 * Create a basic particle emitter
	 * @param owner particle system owning this emitter
	 * @param data emitter template
	 */
	protected EmitterBasic( ParticleSystem owner, EmitterTemplateBasic data  ) {
		super(owner, data);
		
		this.data = data;
		
		shader = Shader.loadShader("data/shaders/particle_simple");
		
		this.sizeInitial = data.sizeInitial;
		this.sizeEnd = data.sizeEnd;
		
		int capacity = (remaining > 0) ? remaining : (int)(rate*lifetimeMax)+1 ;
		positions = new ArrayList<Vec2f>(capacity);
		velocities = new ArrayList<Vec2f>(capacity);
		lives = new ArrayList<Float>(capacity);
		livesTotal = new ArrayList<Float>(capacity);
		rotations = new ArrayList<Float>(capacity);
	}

	@Override
	boolean update(double delta) {
		super.update(delta);
		
		//Force = current map gravity
		Vec2f force = Vec2f.multiply(GameMaster.gm.getMap().gravity, data.gravityScale);
		
		for( int i=positions.size()-1; i>=0; i-- ) {
			if( lives.get(i) > 0 ) {
				velocities.get(i).add(Vec2f.multiply(force, (float)delta));
				positions.get(i).add( Vec2f.multiply(velocities.get(i), (float) delta) );
				lives.set(i, lives.get(i)-(float)delta);
			} else {
				positions.remove(i);
				velocities.remove(i);
				lives.remove(i);
				rotations.remove(i);
				livesTotal.remove(i);
			}
		}
		
		//Return true if all particles are dead and none are left to spawn
		return remaining == 0 && positions.isEmpty();
	}
	
	@Override
	void spawn(int count) {
		for(int i=0; i<count; i++) {
			positions.add(new Vec2f( owner.position.x, owner.position.y ));

			//Random velocity vector from angle and velocity
			Vec2f vel = Vec2f.fromAngle( Utils.lerpF(angleMin, angleMax, Math.random()) );
			vel.multiply( Utils.lerpF(velocityMin, velocityMax, Math.random()) );
			velocities.add( vel );
			
			float life = (float) ((Math.random()*(lifetimeMax-lifetimeMin))+lifetimeMin);
			lives.add(life);
			livesTotal.add(life);
			
			rotations.add( (float) ((Math.random()*(angleMax-angleMin))+angleMin) );
		}
	}

	@Override
	void draw() {
		shader.bind();
		
		//Get matrices
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		
		for( int i=0; i<positions.size(); i++ ) {
			float lifetime = lives.get(i)/livesTotal.get(i);
			
			float size = Utils.lerpF(sizeEnd, sizeInitial, lifetime);
			
			Vec3f pos = new Vec3f( positions.get(i).x, positions.get(i).y, owner.position.z );
			Quat rot = Quat.fromAngle(rotations.get(i));
			Vec3f scale = new Vec3f( size, size, 1 );
			Mat4f modelM = Mat4f.transform(pos, rot, scale);
			
			shader.setUniformM4("model", modelM);
			
			
			shader.setUniformV4("baseColorMod", Vec4f.lerp(colorEnd, colorStart, lifetime));
			
			model.draw();
		}
	}

}
