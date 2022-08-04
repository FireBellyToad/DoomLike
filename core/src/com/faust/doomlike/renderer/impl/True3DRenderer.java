package com.faust.doomlike.renderer.impl;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.faust.doomlike.data.SectorData;
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
    List<ModelInstance> modelList = new ArrayList<>();

    public True3DRenderer(List<SectorWrapper> sectors) {
        camera = new PerspectiveCamera();
        modelBatch = new ModelBatch();

        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;

        sectors.forEach(sectorData ->  modelList.add(createModel(sectorData)));

    }

    private ModelInstance createModel(SectorWrapper sectorData) {
        MeshPartBuilder meshPartBuilder;
        this.modelBuilder.begin();

        for(WallData wallData: sectorData.getWalls()){

            meshPartBuilder = modelBuilder.part(wallData.getWallUuid(), GL20.GL_TRIANGLES , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
            VertexInfo v1 = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomRightPoint().y, sectorData.getBottomZ()).setNor(0, 0, 1).setCol(wallData.getColor()).setUV(0.5f, 0.0f);
            VertexInfo v2 = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sectorData.getBottomZ()).setNor(0, 0, 1).setCol(wallData.getColor()).setUV(0.0f, 0.0f);
            VertexInfo v3 = new VertexInfo().setPos(wallData.getBottomLeftPoint().x, wallData.getBottomRightPoint().y, sectorData.getTopZ()).setNor(0, 0, 1).setCol(wallData.getColor()).setUV(0.0f, 0.5f);
            VertexInfo v4 = new VertexInfo().setPos(wallData.getBottomRightPoint().x, wallData.getBottomRightPoint().y, sectorData.getTopZ()).setNor(0, 0, 1).setCol(wallData.getColor()).setUV(0.5f, 0.5f);
            meshPartBuilder.rect(v1, v2, v3, v4);

        }

        return new ModelInstance(modelBuilder.end());
    }

    @Override
    public void drawWorld(MapWrapper map, PlayerInstance playerInstance) {
        camera.update();

        modelBatch.begin(camera);
        modelList.forEach(m -> modelBatch.render(m));
        modelBatch.end();

    }


    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
