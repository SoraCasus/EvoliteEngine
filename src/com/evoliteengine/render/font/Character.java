package com.evoliteengine.render.font;

import com.evoliteengine.util.math.Vec2d;

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1.
 *
 * @author Karl
 */
public class Character {

	private int id;
	private Vec2d texCoord;
	private Vec2d maxTexCoord;
	private Vec2d offset;
	private Vec2d size;
	private double xAdvance;

	/**
	 * @param id           - the ASCII value of the character.
	 * @param texCoord     - the texture coordinate for the top left corner of the character
	 *                     in the texture atlas.
	 * @param texCoordSize - the width and height of the character in the texture atlas.
	 * @param offset       - the distance from the cursor to the top left edge of the
	 *                     character's quad
	 * @param size         - the width and height of the character's quad in screen space.
	 * @param xAdvance     - how far in pixels the cursor should advance after adding
	 *                     this character.
	 */
	protected Character (int id, Vec2d texCoord, Vec2d texCoordSize, Vec2d offset, Vec2d size, double xAdvance) {
		this.id = id;
		this.texCoord = texCoord;
		this.offset = offset;
		this.size = size;
		this.maxTexCoord = new Vec2d();
		this.maxTexCoord.x = texCoordSize.x + texCoord.x;
		this.maxTexCoord.y = texCoordSize.y + texCoord.y;
		this.xAdvance = xAdvance;
	}

	protected int getId () {
		return id;
	}

	protected double getxTextureCoord () {
		return texCoord.x;
	}

	protected double getyTextureCoord () {
		return texCoord.y;
	}

	protected double getXMaxTextureCoord () {
		return maxTexCoord.x;
	}

	protected double getYMaxTextureCoord () {
		return maxTexCoord.y;
	}

	protected double getxOffset () {
		return offset.x;
	}

	protected double getyOffset () {
		return offset.y;
	}

	protected double getSizeX () {
		return size.x;
	}

	protected double getSizeY () {
		return size.y;
	}

	protected double getxAdvance () {
		return xAdvance;
	}

}
