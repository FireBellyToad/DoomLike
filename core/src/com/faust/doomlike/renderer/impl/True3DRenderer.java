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
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ShortArray;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.WorldRenderer;
import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.utils.MapWrapper;
import com.faust.doomlike.utils.SectorWrapper;

import java.util.*;

public class True3DRenderer implements WorldRenderer {

    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private ModelBuilder modelBuilder = new ModelBuilder();
    public Environment environment;
    public EarClippingTriangulator triangulator = new EarClippingTriangulator();

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
        camera.lookAt(firstWall.getBottomRightPoint().x, firstWall.getBottomRightPoint().y, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        // Init all the model
        createModel(mapWrapper.getSectors());
    }

    /**
     * Create a model from a sector list
     *
     * @param sectorList
     */
    private void createModel(List<SectorWrapper> sectorList) {
        this.modelBuilder.begin();

        //LinkedHashSet because insertion order and uniqueness of x,y pairs must be preserved!
        final Set<Vector2> bottomSurfaceVertexes = new LinkedHashSet<>();
        final Set<Vector2> topSurfaceVertexes = new LinkedHashSet<>();

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

                //Create Wall mesh
                material = new Material(ColorAttribute.createDiffuse(wallData.getColor()));

                meshPartBuilder = modelBuilder.part(wallData.getWallUuid(), GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
                bottomLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sector.getBottomZ()).setNor(0, 0, 1);
                bottomRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sector.getBottomZ()).setNor(0, 0, 1);
                topRightCorner = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sector.getTopZ()).setNor(0, 0, 1);
                topLeftCorner = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomLeftPoint().y, sector.getTopZ()).setNor(0, 0, 1);

                meshPartBuilder.rect(bottomLeftCorner, bottomRightCorner, topRightCorner, topLeftCorner);

                //Save vertexes for surface rendering
                bottomSurfaceVertexes.add(new Vector2(bottomLeftCorner.position.x,bottomLeftCorner.position.y));
                bottomSurfaceVertexes.add(new Vector2(bottomRightCorner.position.x,bottomRightCorner.position.y));
                topSurfaceVertexes.add(new Vector2(topLeftCorner.position.x,topLeftCorner.position.y));
                topSurfaceVertexes.add(new Vector2(topRightCorner.position.x,topRightCorner.position.y));
            }

            createSurfaceMesh(meshPartBuilder, sector,  bottomSurfaceVertexes, sector.getBottomZ(), true);
            createSurfaceMesh(meshPartBuilder, sector, topSurfaceVertexes, sector.getTopZ(), false);

        }

        //Add new model to list
        Model resultModel = modelBuilder.end();
        modelList.add(resultModel);
        instanceList.add(new ModelInstance(resultModel));
    }

    /**
     * Create a surface mesh using EarClipping triangulation
     *
     * @param meshPartBuilder
     * @param sector
     * @param surfaceVertexes
     * @param z coordinate
     * @param isBottom should be true if is bottom surface
     */
    private void createSurfaceMesh(MeshPartBuilder meshPartBuilder, final SectorWrapper sector, final Set<Vector2> surfaceVertexes, final float z, final boolean isBottom) {

        final String idSuffix = isBottom ? "-B" : "-T";

        Material material;
        VertexInfo point1;
        VertexInfo point2;
        VertexInfo point3;

        //Helpers for triangulation
        final List<Vector2> surfaceVertexPairsList = new ArrayList<>();
        final float[] flatSurfaceVertexesArray = new float[surfaceVertexes.size() * 2];

        int i = 0;
        for(Vector2 item : surfaceVertexes){
            surfaceVertexPairsList.add(item);
            flatSurfaceVertexesArray[i] = item.x;
            flatSurfaceVertexesArray[i+1] = item.y;
            i +=2;
        }
        
        ShortArray triangulationResult = triangulator.computeTriangles(flatSurfaceVertexesArray);

        //Generate surface mesh
        for (int surf = 0; surf < triangulationResult.size; surf+=3) {
            material = new Material(ColorAttribute.createDiffuse(sector.getBottomColor()));
            meshPartBuilder = modelBuilder.part(sector.getSectorUuid() + idSuffix, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

            point1 = new VertexInfo().setPos(surfaceVertexPairsList.get(triangulationResult.get(surf)).x, surfaceVertexPairsList.get(triangulationResult.get(surf)).y, z).setNor(0, 0, 1);
            point2 = new VertexInfo().setPos(surfaceVertexPairsList.get(triangulationResult.get(surf+1)).x, surfaceVertexPairsList.get(triangulationResult.get(surf+1)).y, z).setNor(0, 0, 1);
            point3 = new VertexInfo().setPos(surfaceVertexPairsList.get(triangulationResult.get(surf+2)).x, surfaceVertexPairsList.get(triangulationResult.get(surf+2)).y, z).setNor(0, 0, 1);

            if(isBottom){
                meshPartBuilder.triangle(point1, point2, point3);
            } else {
                meshPartBuilder.triangle(point3, point2, point1);
            }

        }

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
