package com.faust.doomlike.test;

import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.utils.Loader;

@Deprecated
public class TestMapFactory implements Loader {

    private final static Color yellow = new Color(0xffff00ff);
    private final static Color darkYellow = new Color(0xaaaa00ff);
    private final static Color red = new Color(0xff0000ff);
    private final static Color darkRed = new Color(0xaa0000ff);
    private final static Color green = new Color(0x00ff00ff);
    private final static Color darkGreen = new Color(0x00aa00ff);
    private final static Color blue = new Color(0x0000ffff);
    private final static Color darkBlue = new Color(0x0000aaff);

    private MapData getFourCubes() {
        MapData testMapData = new MapData();
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(40);
            setBottomColor(red);
            setTopColor(darkRed);
            this.getWalls().add(new WallData(0, 0, 32, 0, 0,0, yellow));
            this.getWalls().add(new WallData(32, 0, 32, 32, 0,0, darkYellow));
            this.getWalls().add(new WallData(32, 32, 0, 32, 0,0, yellow));
            this.getWalls().add(new WallData(0, 32, 0, 0, 0,0, darkYellow));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(40);
            setBottomColor(blue);
            setTopColor(darkBlue);
            this.getWalls().add(new WallData(96, 32, 64, 32, 0,0, red));
            this.getWalls().add(new WallData(64, 32, 64, 0, 0,0, darkRed));
            this.getWalls().add(new WallData(64, 0, 96, 0, 0,0, red));
            this.getWalls().add(new WallData(96, 0, 96, 32, 0,0, darkRed));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(40);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(96, 96, 64, 96, 0,0, green));
            this.getWalls().add(new WallData(64, 96, 64, 64, 0,0, darkGreen));
            this.getWalls().add(new WallData(64, 64, 96, 64, 0,0, green));
            this.getWalls().add(new WallData(96, 64, 96, 96, 0,0, darkGreen));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(40);
            setBottomColor(green);
            setTopColor(darkGreen);
            this.getWalls().add(new WallData(32, 96, 0, 96, 0,0, blue));
            this.getWalls().add(new WallData(0, 96, 0, 64, 0,0, darkBlue));
            this.getWalls().add(new WallData(0, 64, 32, 64, 0,0, blue));
            this.getWalls().add(new WallData(32, 64, 32, 96, 0,0, darkBlue));
        }});
        return testMapData;
    }

    private MapData getHollowMap() {
        MapData testMapData = new MapData();
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(112, 120, 136, 144, 0,0, yellow));
            this.getWalls().add(new WallData(136, 144, 136, 184, 0,0, darkYellow));
            this.getWalls().add(new WallData(136, 184, 112, 208, 0,0, yellow));
            this.getWalls().add(new WallData(112, 208, 112, 120, 0,0, darkYellow));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(-12);
            setTopZ(6);
            setBottomColor(darkBlue);
            setTopColor(yellow);
            this.getWalls().add(new WallData(152, 168, 152, 160, 0,0, red));
            this.getWalls().add(new WallData(152, 160, 160, 152, 0,0, darkRed));
            this.getWalls().add(new WallData(160, 152, 168, 152, 0,0, darkBlue));
            this.getWalls().add(new WallData(168, 152, 176, 160, 0,0, darkRed));
            this.getWalls().add(new WallData(176, 160, 176, 168, 0,0, red));
            this.getWalls().add(new WallData(176, 168, 168, 176, 0,0, darkRed));
            this.getWalls().add(new WallData(168, 176, 160, 176, 0,0, darkBlue));
            this.getWalls().add(new WallData(160, 176, 152, 168, 0,0, darkRed));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(192, 144, 216, 120, 0,0, green));
            this.getWalls().add(new WallData(216, 120, 216, 208, 0,0, darkGreen));
            this.getWalls().add(new WallData(216, 208, 192, 184, 0,0, green));
            this.getWalls().add(new WallData(192, 184, 192, 144, 0,0, darkGreen));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(136, 184, 192, 184, 0,0, darkBlue));
            this.getWalls().add(new WallData(192, 184, 216, 208, 0,0, darkBlue));
            this.getWalls().add(new WallData(216, 208, 112, 208, 0,0, blue));
            this.getWalls().add(new WallData(112, 208, 136, 184, 0,0, darkBlue));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(32);
            setTopZ(50);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(152, 168, 152, 160, 0,0, red));
            this.getWalls().add(new WallData(152, 160, 160, 152, 0,0, darkRed));
            this.getWalls().add(new WallData(160, 152, 168, 152, 0,0, darkBlue));
            this.getWalls().add(new WallData(168, 152, 176, 160, 0,0, darkRed));
            this.getWalls().add(new WallData(176, 160, 176, 168, 0,0, red));
            this.getWalls().add(new WallData(176, 168, 168, 176, 0,0, darkRed));
            this.getWalls().add(new WallData(168, 176, 160, 176, 0,0, darkBlue));
            this.getWalls().add(new WallData(160, 176, 152, 168, 0,0, darkRed));
        }});
        return testMapData;
    }

    @Override
    public MapData loadMap() {
        return getFourCubes();
    }
}
