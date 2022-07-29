package com.faust.doomlike.test;

import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;

public class TestMapFactory {


    private final static Color yellow = new Color(0xffff00ff);
    private final static Color darkYellow = new Color(0xaaaa00ff);
    private final static Color red = new Color(0xff0000ff);
    private final static Color darkRed = new Color(0xaa0000ff);
    private final static Color green = new Color(0x00ff00ff);
    private final static Color darkGreen = new Color(0x00aa00ff);
    private final static Color blue = new Color(0x0000ffff);
    private final static Color darkBlue = new Color(0x0000aaff);

    public static MapData getFourCubes() {
        MapData testMapData = new MapData();
        testMapData.getSectors().add(new SectorData() {{
            setTopZ(0);
            setTopZ(40);
            setBottomColor(red);
            setTopColor(darkRed);
            this.getWalls().add(new WallData(0, 0, 32, 0, yellow));
            this.getWalls().add(new WallData(32, 0, 32, 32, darkYellow));
            this.getWalls().add(new WallData(32, 32, 0, 32, yellow));
            this.getWalls().add(new WallData(0, 32, 0, 0, darkYellow));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setTopZ(0);
            setTopZ(40);
            setBottomColor(blue);
            setTopColor(darkBlue);
            this.getWalls().add(new WallData(64, 0, 96, 0, red));
            this.getWalls().add(new WallData(96, 0, 96, 32, darkRed));
            this.getWalls().add(new WallData(96, 32, 64, 32, red));
            this.getWalls().add(new WallData(64, 32, 64, 0, darkRed));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setTopZ(0);
            setTopZ(40);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(64, 64, 96, 64, green));
            this.getWalls().add(new WallData(96, 64, 96, 96, darkGreen));
            this.getWalls().add(new WallData(96, 96, 64, 96, green));
            this.getWalls().add(new WallData(64, 96, 64, 64, darkGreen));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setTopZ(0);
            setTopZ(40);
            setBottomColor(green);
            setTopColor(darkGreen);
            this.getWalls().add(new WallData(0, 64, 32, 64, blue));
            this.getWalls().add(new WallData(32, 64, 32, 96, darkBlue));
            this.getWalls().add(new WallData(32, 96, 0, 96, blue));
            this.getWalls().add(new WallData(0, 96, 0, 64, darkBlue));
        }});
        return testMapData;
    }

    public static MapData getHollowMap() {
        MapData testMapData = new MapData();
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(112, 120, 136, 144, yellow));
            this.getWalls().add(new WallData(136, 144, 136, 184, darkYellow));
            this.getWalls().add(new WallData(136, 184, 112, 208, yellow));
            this.getWalls().add(new WallData(112, 208, 112, 120, darkYellow));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(-12);
            setTopZ(6);
            setBottomColor(darkBlue);
            setTopColor(yellow);
            this.getWalls().add(new WallData(152, 168, 152, 160, red));
            this.getWalls().add(new WallData(152, 160, 160, 152, darkRed));
            this.getWalls().add(new WallData(160, 152, 168, 152, darkBlue));
            this.getWalls().add(new WallData(168, 152, 176, 160, darkRed));
            this.getWalls().add(new WallData(176, 160, 176, 168, red));
            this.getWalls().add(new WallData(176, 168, 168, 176, darkRed));
            this.getWalls().add(new WallData(168, 176, 160, 176, darkBlue));
            this.getWalls().add(new WallData(160, 176, 152, 168, darkRed));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(192, 144, 216, 120, green));
            this.getWalls().add(new WallData(216, 120, 216, 208, darkGreen));
            this.getWalls().add(new WallData(216, 208, 192, 184, green));
            this.getWalls().add(new WallData(192, 184, 192, 144, darkGreen));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(0);
            setTopZ(6);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(136, 184, 192, 184, darkBlue));
            this.getWalls().add(new WallData(192, 184, 216, 208, darkBlue));
            this.getWalls().add(new WallData(216, 208, 112, 208, blue));
            this.getWalls().add(new WallData(112, 208, 136, 184, darkBlue));
        }});
        testMapData.getSectors().add(new SectorData() {{
            setBottomZ(32);
            setTopZ(50);
            setBottomColor(yellow);
            setTopColor(darkYellow);
            this.getWalls().add(new WallData(160, 152, 168, 152, darkBlue));
            this.getWalls().add(new WallData(168, 152, 176, 160, darkRed));
            this.getWalls().add(new WallData(176, 160, 176, 168, red));
            this.getWalls().add(new WallData(176, 168, 168, 176, darkRed));
            this.getWalls().add(new WallData(168, 176, 160, 176, darkBlue));
            this.getWalls().add(new WallData(160, 176, 152, 168, darkRed));
            this.getWalls().add(new WallData(152, 168, 152, 160, red));
            this.getWalls().add(new WallData(152, 160, 160, 152, darkRed));
        }});
        return testMapData;
    }
}
