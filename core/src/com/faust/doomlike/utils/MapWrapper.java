package com.faust.doomlike.utils;

import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.TextureData;
import com.faust.doomlike.test.PlayerInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for rendering Sector
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class MapWrapper {

    private final Map<String, TextureData> textureMap;
    private MapData mapData;
    private final List<SectorWrapper> sectors = new ArrayList<>();
    private final PlayerInstance playerInstance;
    private final HeaderFormatLoader mapLoader;

    public MapWrapper(HeaderFormatLoader mapLoader) {
        this.mapLoader = mapLoader;
        this.textureMap = mapLoader.loadTextures();
        this.mapData = mapLoader.loadMap(textureMap);
        this.playerInstance = new PlayerInstance();

        this.mapData.getSectors().forEach(sd -> sectors.add(new SectorWrapper(sd)));
    }

    public List<SectorWrapper> getSectors() {
        return sectors;
    }

    public void doLogic() {
        playerInstance.doLogic();

        //Reload level on enter key
        if(playerInstance.isReloadLevel()){
            //clear sectors and destroy map
            sectors.clear();
            this.mapData = mapLoader.loadMap(textureMap);
            //Reload map
            this.mapData.getSectors().forEach(sd -> sectors.add(new SectorWrapper(sd)));
            playerInstance.setReloadLevel(false);
        }
    }

    public PlayerInstance getPlayerInstance() {
        return playerInstance;
    }
}
