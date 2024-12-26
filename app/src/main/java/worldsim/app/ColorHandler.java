package worldsim.app;

import java.util.EnumMap;
import java.awt.Color;

import lombok.Getter;

public class ColorHandler {
	
	EnumMap<TerrainType, Color> colorMap = new EnumMap(TerrainType.class);

	@Getter
	TerrainType current = TerrainType.EARTH1;

	public ColorHandler() {
		colorMap.put(TerrainType.EARTH1, new Color(212, 57, 50));
		colorMap.put(TerrainType.EARTH2, new Color(212, 100, 105));
		colorMap.put(TerrainType.EARTH3, new Color(212, 150, 125));
		colorMap.put(TerrainType.BURNT_GRASS, new Color(150, 155, 50));
		colorMap.put(TerrainType.ROAD, Color.gray.darker());
		colorMap.put(TerrainType.FORREST_GRASS, Color.green.darker());
	}

	public Color getTerrainColor(TerrainType terrainType) {
		return colorMap.get(terrainType);
	}

	public void cycleTerrain() {
		int index = current.ordinal();
  		int nextIndex = index + 1;
  		nextIndex %= TerrainType.values().length;
  		current = TerrainType.values()[nextIndex];
	}

}