package com.evoliteengine.test;


import com.evoliteengine.io.OBJLoader;
import com.evoliteengine.io.normalMappingObjConverter.NormalMappedObjLoader;
import com.evoliteengine.render.DisplayManager;
import com.evoliteengine.render.entities.Camera;
import com.evoliteengine.render.entities.Entity;
import com.evoliteengine.render.entities.Light;
import com.evoliteengine.render.entities.Player;
import com.evoliteengine.render.font.FontType;
import com.evoliteengine.render.font.GUIText;
import com.evoliteengine.render.font.TextMaster;
import com.evoliteengine.render.globjects.Fbo;
import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.models.Material;
import com.evoliteengine.render.models.TexturedModel;
import com.evoliteengine.render.renderers.GuiRenderer;
import com.evoliteengine.render.renderers.MasterRenderer;
import com.evoliteengine.render.renderers.PostProcessing;
import com.evoliteengine.render.renderers.WaterRenderer;
import com.evoliteengine.render.shader.WaterShader;
import com.evoliteengine.render.terrain.Terrain;
import com.evoliteengine.render.texture.GuiTexture;
import com.evoliteengine.render.texture.TerrainTexturePack;
import com.evoliteengine.render.texture.Texture;
import com.evoliteengine.render.water.WaterFrameBuffers;
import com.evoliteengine.render.water.WaterTile;
import com.evoliteengine.util.EEFile;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {


	public static void main (String[] args) {

		DisplayManager.createDisplay();
		TextMaster.init();
		Vao playerModel = OBJLoader.loadObjModel(new EEFile("models/person.obj"));
		Material playerMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/playerTexture.png")).anisotropic().create());
		TexturedModel playertm = new TexturedModel(playerModel, playerMaterial);

		Player player = new Player(playertm, new Vector3f(100, 5, -150), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);

		MasterRenderer renderer = new MasterRenderer(camera);

		FontType font = new FontType(Texture.newTexture(new EEFile("font/candara.png")).create(), new EEFile("font/candara.fnt"));
		GUIText text = new GUIText("This is a test text", 3, font, new Vector2f(0.0f, 0.4f), 1.0f, true);
		text.setColour(0.1f, 0.1f, 0.1f);

		// *********TERRAIN TEXTURE STUFF**********

		Texture backgroundTexture = Texture.newTexture(new EEFile("textures/terrain/grassy2.png")).anisotropic().create();
		Texture rTexture = Texture.newTexture(new EEFile("textures/terrain/mud.png")).anisotropic().create();
		Texture gTexture = Texture.newTexture(new EEFile("textures/terrain/grassFlowers.png")).anisotropic().create();
		Texture bTexture = Texture.newTexture(new EEFile("textures/terrain/path.png")).anisotropic().create();

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		Texture blendMap = Texture.newTexture(new EEFile("textures/terrain/blendMap.png")).create();

		// *****************************************

		Vao grassVao = OBJLoader.loadObjModel(new EEFile("models/grassModel.obj"));
		Material grassMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/grassTexture.png")).anisotropic().create())
				.setUseFakeLighting(true)
				.setTransparent(true);

		TexturedModel grass = new TexturedModel(grassVao, grassMaterial);

		Material flowerMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/flower.png")).anisotropic().create())
				.setUseFakeLighting(true)
				.setTransparent(true);

		TexturedModel flowers = new TexturedModel(grassVao, flowerMaterial);

		Vao fernVao = OBJLoader.loadObjModel(new EEFile("models/fern.obj"));
		Material fernMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/fern.png")).anisotropic().create())
				.setNumberOfRows(2)
				.setTransparent(true);
		TexturedModel fern = new TexturedModel(fernVao, fernMaterial);

		Vao bobbleVao = OBJLoader.loadObjModel(new EEFile("models/pine.obj"));
		Material bobbleMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/pine.png")).anisotropic().create())
				.setTransparent(true);

		TexturedModel bobble = new TexturedModel(bobbleVao, bobbleMaterial);

		// Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, new EEFile("textures/terrain/heightmap/customHeightMap.png"));
		Terrain terrain = new Terrain(0, -1, texturePack, blendMap, 256);
		List<Terrain> terrains = new ArrayList<>();
		terrains.add(terrain);

		List<Entity> entities = new ArrayList<>();
		List<Entity> normalMapEntities = new ArrayList<>();

		entities.add(player);

		Vao barrelVao = NormalMappedObjLoader.loadOBJ(new EEFile("models/boulder.obj"));
		Material barrelMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/boulder.png")).anisotropic().create())
				.setNormal(Texture.newTexture(new EEFile("textures/normal/boulderNormal.png")).create())
				.setShineDamper(10)
				.setReflectivity(0.5F);

		TexturedModel barrelModel = new TexturedModel(barrelVao, barrelMaterial);

		normalMapEntities.add(new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f));


		Vao cherryVao = OBJLoader.loadObjModel(new EEFile("models/cherry.obj"));
		Material cherryMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/cherry.png")).anisotropic().create())
				.setTransparent(true)
				.setShineDamper(10)
				.setReflectivity(0.5F)
				.setSpecular(Texture.newTexture(new EEFile("textures/specular/cherryS.png")).create());

		TexturedModel cherryModel = new TexturedModel(cherryVao, cherryMaterial);

		Random random = new Random();
		for (int i = 0; i < 400; i++) {

			float x = random.nextFloat() * 800;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			if (y <= 0) continue;

			if (i % 4 == 0) {
				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1));
			}

			if (i % 5 == 0) {
				entities.add(new Entity(flowers, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1));
			}

			if (i % 3 == 0) {
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, 0.9f));
			}

			if (i % 2 == 0) {
				entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 1.0f + 0.6f));

			}

			if (i % 9 == 0) {
				entities.add(new Entity(cherryModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 1.8f + 2.0f));
			}
		}

		Light light = new Light(new Vector3f(0, 10000, -7000), new Vector3f(0.4f, 0.4f, 0.4f));
		List<Light> lights = new ArrayList<>();
		//lights.add(light);
		Light sun = new Light(new Vector3f(10000, 20000, -10000), new Vector3f(1.0f, 1.0f, 1.0f));
		lights.add(sun);
		lights.add(new Light(new Vector3f(185, terrain.getHeightOfTerrain(185, -293) + 30, -293), new Vector3f(4, 0, 0),
				new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, terrain.getHeightOfTerrain(370, -300) + 30, -300), new Vector3f(0, 2, 2),
				new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, terrain.getHeightOfTerrain(293, -305) + 30, -305), new Vector3f(2, 2, 0),
				new Vector3f(1, 0.01f, 0.002f)));


		Vao lampVao = OBJLoader.loadObjModel(new EEFile("models/lantern.obj"));
		Material lampMaterial = new Material()
				.setDiffuse(Texture.newTexture(new EEFile("textures/diffuse/lantern.png")).anisotropic().create())
				.setSpecular(Texture.newTexture(new EEFile("textures/specular/lanternS.png")).create())
				.setUseFakeLighting(true);

		TexturedModel lamp = new TexturedModel(lampVao, lampMaterial);

		entities.add(new Entity(lamp, new Vector3f(185, terrain.getHeightOfTerrain(185, -293), -293), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(370, terrain.getHeightOfTerrain(370, -300), -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, terrain.getHeightOfTerrain(293, -305), -305), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));


		List<GuiTexture> guiTextures = new ArrayList<>();
		GuiRenderer guiRenderer = new GuiRenderer();


//		GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(),
//				new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f), 180);
//		guiTextures.add(shadowMap);


		WaterShader waterShader = new WaterShader();
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<>();
		WaterTile water = new WaterTile(400, 400, 0);
		waters.add(water);

		/*

		GuiTexture refraction = new GuiTexture(fbos.getRefractionTexture(),new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
		GuiTexture reflection = new GuiTexture(fbos.getReflectionTexture(),new Vector2f(-0.5f,0.5f), new Vector2f(0.25f,0.25f));
		guiTextures.add(refraction);
		guiTextures.add(reflection);

		*/

		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init();

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();

			renderer.renderShadowMap(entities, sun);

			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			//reflection
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 0f));

			camera.getPosition().y += distance;
			camera.invertPitch();

			//refraction
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));


			//Screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();

			multisampleFbo.bindFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
			waterRenderer.render(waters, camera, light);

			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToScreen();
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());

			guiRenderer.render(guiTextures);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();
		multisampleFbo.cleanUp();
		TextMaster.cleanUp();
		fbos.cleanUp();
		waterShader.delete();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		DisplayManager.closeDisplay();

	}


	// Main 2
	
	
	
	
	
	
	
	
	/*public static void main2() {
		 
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);
         
        FontType font = new FontType(loader.loadTexture("harrington"), new File("res/harrington.fnt"));
        GUIText text = new GUIText("This is some text!", 3f, font, new Vector2f(0f, 0f), 1f, true);
        text.setColour(1, 0, 0);
 
        // *********TERRAIN TEXTURE STUFF**********
         
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
 
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
 
        // *****************************************
 
        TexturedModel rocks = new TexturedModel(OBJLoader.loadObjModel("rocks", loader),
                new ModelTexture(loader.loadTexture("rocks")));
 
        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);
 
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                fernTextureAtlas);
 
        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
                new ModelTexture(loader.loadTexture("pine")));
        bobble.getTexture().setHasTransparency(true);
 
        fern.getTexture().setHasTransparency(true);
 
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "customHeightMap");
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);
 
        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);
 
        List<Entity> com.evoliteengine.render.entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();
         
        //******************NORMAL MAP MODELS************************
         
        TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
                new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);
         
        TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
                new ModelTexture(loader.loadTexture("crate")));
        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);
         
        TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);
         
         
        //************ENTITIES*******************
         
        Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);
         
        Random random = new Random(5666778);
        for (int i = 0; i < 60; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
 
                    com.evoliteengine.render.entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, 0.9f));
                }
            }
            if (i % 2 == 0) {
 
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
 
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
                    com.evoliteengine.render.entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                }
            }
        }
        com.evoliteengine.render.entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
         
        //*******************OTHER SETUP***************
 
        List<Light> lights = new ArrayList<Light>();
        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);
 
        MasterRenderer renderer = new MasterRenderer(loader);
 
        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
                loader.loadTexture("playerTexture")));
 
        Player player = new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        com.evoliteengine.render.entities.add(player);
        Camera camera = new Camera(player);
        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
     
        //**********Water Renderer Set-up************************
         
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(75, -75, 0);
        for(int i = 1; i < 5; i++){
        	for(int j = 1; j < 5; j++){
        		waters.add(new WaterTile(i * 150, -j * 150, -20));
        	}
        }
        //waters.add(water);
         
        
        
        //****************Game Loop Below*********************
 
        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.move();
            picker.update();
            entity.increaseRotation(0, 1, 0);
            entity2.increaseRotation(0, 1, 0);
            entity3.increaseRotation(0, 1, 0);
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
             
            //render reflection teture
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(com.evoliteengine.render.entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));
            camera.getPosition().y += distance;
            camera.invertPitch();
             
            //render refraction texture
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(com.evoliteengine.render.entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
             
            //render to screen
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer(); 
            renderer.renderScene(com.evoliteengine.render.entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);
            TextMaster.render();
             
            DisplayManager.updateDisplay();
        }
 
        //*********Clean Up Below**************
         
        //ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
 
    }*/
}
