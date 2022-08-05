package com.faust.doomlike.renderer.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.WorldRenderer;
import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.utils.MapWrapper;
import com.faust.doomlike.utils.SectorWrapper;

import java.util.ArrayList;
import java.util.List;

public class True3DRenderer implements WorldRenderer {

    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private ModelBuilder  modelBuilder = new ModelBuilder();
    public Environment environment;
    public List<Model> modelList = new ArrayList<>();
    public List<ModelInstance> instanceList = new ArrayList<>();

    public True3DRenderer(List<SectorWrapper> sectors, PlayerInstance playerInstance) {

        // Add environment Light
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        WallData firstWall = sectors.get(0).getWalls().get(0);

        modelBatch = new ModelBatch();

        //Camera is player
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(playerInstance.getPosition());
        camera.lookAt(firstWall.getBottomRightPoint().x,firstWall.getBottomRightPoint().y,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        // Init all the mode
        sectors.forEach(sectorData -> createModel(sectorData));
    }

    private void createModel(SectorWrapper sectorData) {
        this.modelBuilder.begin();

        MeshPartBuilder meshPartBuilder;
        VertexInfo bottomLeftCorner;
        VertexInfo bottomRightCorner;
        VertexInfo topRightCorner;
        VertexInfo topLeftCorner;
        Material wallMaterial;

        for(WallData wallData: sectorData.getWalls()){

            //Create Walls
            wallMaterial = new Material(ColorAttribute.createDiffuse(wallData.getColor()));

            meshPartBuilder = modelBuilder.part(wallData.getWallUuid(), GL20.GL_TRIANGLES , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, wallMaterial);
            bottomLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sectorData.getBottomZ()).setNor(0, 0, -1);
            bottomRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sectorData.getBottomZ()).setNor(0, 0, -1);
            topRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sectorData.getTopZ()).setNor(0, 0, 1);
            topLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sectorData.getTopZ()).setNor(0, 0, 1);

            meshPartBuilder.rect(bottomLeftCorner, bottomRightCorner, topRightCorner, topLeftCorner);

            //TODO create polygon surfaces
        }

        //Add new model to list
        Model resultModel = modelBuilder.end();
        modelList.add(resultModel);
        instanceList.add(new ModelInstance(resultModel));
    }

    @Override
    public void drawWorld(MapWrapper map, PlayerInstance playerInstance) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.position.set(playerInstance.getPosition());
        camera.lookAt(playerInstance.getPosition());
        camera.update();

        modelBatch.begin(camera);
        instanceList.forEach(mi -> modelBatch.render(mi,environment));
        modelBatch.end();

    }


    @Override
    public void dispose() {
        modelBatch.dispose();
        modelList.forEach(mi -> mi.dispose());
    }
}
