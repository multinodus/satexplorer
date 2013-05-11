/*
Copyright (c) 2012 Aaron Perkins

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.multinodus.satteliteexplorer.ui.scene.models.planet;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 * Quad
 * <p/>
 * Credits
 * This code has been adapted from OgrePlanet
 * Copyright (c) 2010 Anders Lingfors
 * https://bitbucket.org/lingfors/ogreplanet/
 */
public class Planet extends Node {

  protected Material terrainMaterial;
  protected Material oceanMaterial;
  protected Material atmosphereMaterial;
  protected Node planetNode;
  protected Node atmosphereNode;
  protected float baseRadius;
  protected float atmosphereRadius;
  protected HeightDataSource dataSource;
  // Number of planer quads per patch. This value directly controls the
  // complexity of the geometry generated.
  protected int quads = 32;
  // Minimal depth for spliting. The planet will start at this depth
  // no matter the distance from camera
  protected int minDepth = 1;
  // Max depth for splitting. The planet will only split down to this depth
  // no matter the distance from the camera
  protected int maxDepth = 10;
  protected Quad[] atmosphereSide = new Quad[6];
  protected boolean wireframeMode;
  protected Vector3f planetToCamera;
  protected float distanceToCamera;

  protected ColorRGBA atmosphereFogColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f);
  protected float atmosphereFogDistance = 0.5f; // percentage range 0 to 1
  protected float atmosphereFogDensity = 10f; // multipler range 0.1 - 100

  /**
   * <code>Planet</code>
   *
   * @param name       Name of the node
   * @param baseRadius The radius of the planet
   * @param dataSource The <code>HeightDataSource</code> used for the terrain
   */
  public Planet(String name, float baseRadius, Material material, HeightDataSource dataSource) {
    super(name);
    this.baseRadius = baseRadius;
    this.dataSource = dataSource;

    this.planetNode = new Node("PlanetNode");

    Sphere rock = new Sphere(32, 32, 63781f);
    Geometry shiny_rock = new Geometry("Shiny rock", rock);
    rock.setTextureMode(Sphere.TextureMode.Projected); // better quality on spheres
    //TangentBinormalGenerator.generate(rock);           // for lighting effect
    shiny_rock.setMaterial(material);
    shiny_rock.setLocalTranslation(0, 2, -2); // Move it a bit
    shiny_rock.rotate(1.6f, 0, 0);          // Rotate it a bit
    planetNode.attachChild(shiny_rock);

    this.attachChild(planetNode);
  }

  public void createAtmosphere(Material material, float atmosphereRadius) {
    this.atmosphereMaterial = material;
    this.atmosphereRadius = atmosphereRadius;

    if (atmosphereNode == null)
      prepareAtmosphere();
  }

  public void setCameraPosition(Vector3f position) {
    // get vector between planet and camera
    this.planetToCamera = position.subtract(this.getLocalTranslation());
    // get distance to surface
    this.distanceToCamera = this.planetToCamera.length() - this.baseRadius;

    for (int i = 0; i < 6; i++) {
      if (atmosphereSide[i] != null) {
        atmosphereSide[i].setCameraPosition(position);
      }
    }
  }

  public Node getPlanetNode() {
    return this.planetNode;
  }

  public Node getAtmosphereNode() {
    return this.atmosphereNode;
  }

  public float getRadius() {
    return this.baseRadius;
  }

  public float getAtmosphereRadius() {
    return this.atmosphereRadius;
  }

  public float getHeightScale() {
    return dataSource.getHeightScale();
  }

  public Vector3f getPlanetToCamera() {
    return this.planetToCamera;
  }

  public float getDistanceToCamera() {
    return this.distanceToCamera;
  }

  public ColorRGBA getAtmosphereFogColor() {
    return this.atmosphereFogColor;
  }

  public void setAtmosphereFogColor(ColorRGBA atmosphereFogColor) {
    this.atmosphereFogColor = atmosphereFogColor;
  }

  public float getAtmosphereFogDistance() {
    return this.atmosphereFogDistance;
  }

  public void setAtmosphereFogDistance(float atmosphereFogDistance) {
    this.atmosphereFogDistance = atmosphereFogDistance;
  }

  public float getAtmosphereFogDensity() {
    return this.atmosphereFogDensity;
  }

  public void setAtmosphereFogDensity(float atmosphereFogDensity) {
    this.atmosphereFogDensity = atmosphereFogDensity;
  }

  public void toogleWireframe() {
    if (this.wireframeMode)
      wireframeMode = false;
    else
      wireframeMode = true;
  }

  private void prepareAtmosphere() {
    this.atmosphereNode = new Node("AtmosphereNode");
    planetNode.attachChild(atmosphereNode);

    atmosphereNode.setQueueBucket(Bucket.Transparent);

    int quads = this.quads;
    int minDepth = 2;
    int maxDepth = 4;

    SimpleHeightDataSource dataSource = new SimpleHeightDataSource();

    Vector3f rightMin = new Vector3f(1.0f, 1.0f, 1.0f);
    Vector3f rightMax = new Vector3f(1.0f, -1.0f, -1.0f);
    atmosphereSide[0] = new Quad(
        "AtmosphereRight",
        this.atmosphereMaterial,
        this.atmosphereNode,
        rightMin,
        rightMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);

    Vector3f leftMin = new Vector3f(-1.0f, 1.0f, -1.0f);
    Vector3f leftMax = new Vector3f(-1.0f, -1.0f, 1.0f);
    atmosphereSide[1] = new Quad(
        "AtmosphereLeft",
        this.atmosphereMaterial,
        this.atmosphereNode,
        leftMin,
        leftMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);

    Vector3f topMin = new Vector3f(-1.0f, 1.0f, -1.0f);
    Vector3f topMax = new Vector3f(1.0f, 1.0f, 1.0f);
    atmosphereSide[2] = new Quad(
        "AtmosphereTop",
        this.atmosphereMaterial,
        this.atmosphereNode,
        topMin,
        topMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);

    Vector3f bottomMin = new Vector3f(-1.0f, -1.0f, 1.0f);
    Vector3f bottomMax = new Vector3f(1.0f, -1.0f, -1.0f);
    atmosphereSide[3] = new Quad(
        "AtmosphereBottom",
        this.atmosphereMaterial,
        this.atmosphereNode,
        bottomMin,
        bottomMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);

    Vector3f backMin = new Vector3f(1.0f, 1.0f, -1.0f);
    Vector3f backMax = new Vector3f(-1.0f, -1.0f, -1.0f);
    atmosphereSide[5] = new Quad(
        "AtmosphereBack",
        this.atmosphereMaterial,
        this.atmosphereNode,
        backMin,
        backMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);

    Vector3f frontMin = new Vector3f(-1.0f, 1.0f, 1.0f);
    Vector3f frontMax = new Vector3f(1.0f, -1.0f, 1.0f);
    atmosphereSide[4] = new Quad(
        "AtmosphereFront",
        this.atmosphereMaterial,
        this.atmosphereNode,
        frontMin,
        frontMax,
        0f,
        FastMath.pow(2.0f, 20f),
        0f,
        FastMath.pow(2.0f, 20f),
        this.atmosphereRadius,
        dataSource,
        quads,
        0,
        minDepth,
        maxDepth,
        null,
        0);
  }
}