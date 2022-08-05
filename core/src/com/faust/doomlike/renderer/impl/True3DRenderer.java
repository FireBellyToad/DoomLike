package com.faust.doomlike.renderer.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
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
    private ModelBuilder modelBuilder = new ModelBuilder();
    public Environment environment;
    public List<Model> modelList = new ArrayList<>();
    public List<ModelInstance> instanceList = new ArrayList<>();

    public True3DRenderer(MapWrapper mapWrapper) {

        // Add environment Light
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        WallData firstWall = mapWrapper.getSectors().get(0).getWalls().get(0);
        modelBatch = new ModelBatch();

        //Camera is player
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Vector3.Zero);
        camera.lookAt(firstWall.getBottomRightPoint().x,firstWall.getBottomRightPoint().y,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        // Init all the model
        createModel(mapWrapper.getSectors());
    }

    /**
     * Create a model from a
     * @param sectorList
     */
    private void createModel(List<SectorWrapper> sectorList) {
        this.modelBuilder.begin();

        final List<Vector3> bottomSurfaceVertexes = new ArrayList<>();
        final List<Vector3> topSurfaceVertexes = new ArrayList<>();

        MeshPartBuilder meshPartBuilder = null;
        VertexInfo bottomLeftCorner;
        VertexInfo bottomRightCorner;
        VertexInfo topRightCorner;
        VertexInfo topLeftCorner;
        Material material;

        //For all sectors, create walls and surfaces that will be rendered
        for (SectorWrapper sector : sectorList) {

            //Needed as placeholder
            bottomSurfaceVertexes.clear();
            topSurfaceVertexes.clear();

            for (WallData wallData : sector.getWalls()) {

                //Create Walls
                material = new Material(ColorAttribute.createDiffuse(wallData.getColor()));

                meshPartBuilder = modelBuilder.part(wallData.getWallUuid(), GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
                bottomLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sector.getBottomZ()).setNor(0, 0, 1);
                bottomRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sector.getBottomZ()).setNor(0, 0, 1);
                topRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sector.getTopZ()).setNor(0, 0, 1);
                topLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sector.getTopZ()).setNor(0, 0, 1);

                meshPartBuilder.rect(bottomLeftCorner, bottomRightCorner, topRightCorner, topLeftCorner);

                //Save vertexes for surface rendering
                bottomSurfaceVertexes.add(bottomLeftCorner.position);
                bottomSurfaceVertexes.add(bottomRightCorner.position);
                topSurfaceVertexes.add(topLeftCorner.position);
                topSurfaceVertexes.add(topRightCorner.position);
            }

            //Generate bottom surface mesh
            for (int surf = 1; surf < bottomSurfaceVertexes.size() - 1; surf++) {

                material = new Material(ColorAttribute.createDiffuse(sector.getBottomColor()));
                meshPartBuilder = modelBuilder.part(sector.getSectorUuid() + "-B", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

                //TODO This triagulation must be improved!
                bottomLeftCorner = new VertexInfo().setPos(bottomSurfaceVertexes.get(0).x, bottomSurfaceVertexes.get(0).y, bottomSurfaceVertexes.get(0).z).setNor(0, 0, 1);
                bottomRightCorner = new VertexInfo().setPos(bottomSurfaceVertexes.get(surf).x, bottomSurfaceVertexes.get(surf).y, bottomSurfaceVertexes.get(surf).z).setNor(0, 0, 1);
                topRightCorner = new VertexInfo().setPos(bottomSurfaceVertexes.get(surf + 1).x, bottomSurfaceVertexes.get(surf + 1).y, bottomSurfaceVertexes.get(surf + 1).z).setNor(0, 0, 1);

                meshPartBuilder.triangle(topRightCorner, bottomRightCorner, bottomLeftCorner);
            }

            //Generate top surface mesh
            for (int surf = 1; surf < topSurfaceVertexes.size() - 1; surf++) {

                material = new Material(ColorAttribute.createDiffuse(sector.getBottomColor()));
                meshPartBuilder = modelBuilder.part(sector.getSectorUuid() + "-B", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

                //TODO This triagulation must be improved!
                bottomLeftCorner = new VertexInfo().setPos(topSurfaceVertexes.get(0).x, topSurfaceVertexes.get(0).y, topSurfaceVertexes.get(0).z).setNor(0, 0, 1);
                bottomRightCorner = new VertexInfo().setPos(topSurfaceVertexes.get(surf).x, topSurfaceVertexes.get(surf).y, topSurfaceVertexes.get(surf).z).setNor(0, 0, 1);
                topRightCorner = new VertexInfo().setPos(topSurfaceVertexes.get(surf + 1).x, topSurfaceVertexes.get(surf + 1).y, topSurfaceVertexes.get(surf + 1).z).setNor(0, 0, 1);

                meshPartBuilder.triangle(bottomLeftCorner, bottomRightCorner, topRightCorner);
            }


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
        instanceList.forEach(mi -> modelBatch.render(mi, environment));
        modelBatch.end();

    }


    @Override
    public void dispose() {
        modelBatch.dispose();
        modelList.forEach(mi -> mi.dispose());
    }
}
