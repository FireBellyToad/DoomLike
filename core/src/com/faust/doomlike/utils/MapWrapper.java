package com.faust.doomlike.utils;

import com.faust.doomlike.data.MapData;
import com.faust.doomlike.test.PlayerInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for rendering Sector
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class MapWrapper {

    private MapData mapData;
    private final List<SectorWrapper> sectors = new ArrayList<>();
    private final PlayerInstance playerInstance;
    private final Loader mapLoader;

    public MapWrapper(Loader mapLoader) {
        this.mapLoader = mapLoader;
        this.mapData = mapLoader.loadMap();
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
            this.mapData = mapLoader.loadMap();
            //Reload map
            this.mapData.getSectors().forEach(sd -> sectors.add(new SectorWrapper(sd)));
            playerInstance.setReloadLevel(false);
        }
    }

    public PlayerInstance getPlayerInstance() {
        return playerInstance;
    }
}
