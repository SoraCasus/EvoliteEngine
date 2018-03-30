package com.evoliteengine.render.models;

public class ModelData {

	private static final int DIMENSIONS = 3;

	private float[] vertices;
	private float[] texCoords;
	private float[] normals;
	private float[] tangents;
	private int[] indices;
	private int[] jointIDs;
	private float[] vertexWeights;
	private float furthestPoint;

	public float[] getVertices () {
		return vertices;
	}

	public float getFurthestPoint () {
		return furthestPoint;
	}

	public void setFurthestPoint (float furthestPoint) {
		this.furthestPoint = furthestPoint;
	}

	public void setVertices (float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getTexCoords () {
		return texCoords;
	}

	public void setTexCoords (float[] texCoords) {
		this.texCoords = texCoords;
	}

	public float[] getNormals () {
		return normals;
	}

	public void setNormals (float[] normals) {
		this.normals = normals;
	}

	public float[] getTangents () {
		return tangents;
	}

	public void setTangents (float[] tangents) {
		this.tangents = tangents;
	}

	public int[] getIndices () {
		return indices;
	}

	public void setIndices (int[] indices) {
		this.indices = indices;
	}

	public int[] getJointIDs () {
		return jointIDs;
	}

	public void setJointIDs (int[] jointIDs) {
		this.jointIDs = jointIDs;
	}

	public float[] getVertexWeights () {
		return vertexWeights;
	}

	public void setVertexWeights (float[] vertexWeights) {
		this.vertexWeights = vertexWeights;
	}

	public int getVertexCount () {
		return vertices.length / DIMENSIONS;
	}

}
