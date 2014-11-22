package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Animation {
	public final int spriteOffset;
	public final int width, height;
	public final int xOffset, yOffset;
	public final int numFrames;
	public final double speed;
	public final boolean directional;

	public Animation(int spriteOffset, int width, int height, int xOffset,
			int yOffset, int numFrames) {
		this(spriteOffset, width, height, xOffset, yOffset, numFrames, 1, true);
	}

	public Animation(int spriteOffset, int width, int height, int xOffset,
			int yOffset, int numFrames, double speed, boolean directional) {
		super();
		this.spriteOffset = spriteOffset;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.numFrames = numFrames;
		this.speed = speed;
		this.directional = directional;
	}

}
