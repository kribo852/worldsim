package worldsim.app;

import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;


public class MapGen {

	Map<Coordinate, OpenMapSpace> map;

	@Setter
	List<String> tileNames;

	int selectedTile;

	public MapGen() {
		final Random rnd = new Random();
		selectedTile = 0;
		map = new HashMap<>();
	} 

	public OpenMapSpace[][] getMap(int x, int y, int z, int width, int height) {
		OpenMapSpace[][] rtn = new OpenMapSpace[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for(int k = z; k > z-3; k--) {
					OpenMapSpace tmp = map.get(new Coordinate(i + x, j + y, k));
					if(tmp != null) {
						rtn[i][j] = tmp;
						break;
					}
				}
			}	
		}
		return rtn;
	}

	public String getSelectedTerrainType() {
		return tileNames.get(selectedTile);
	}

	public void cycleSelectedTerrain() {
		selectedTile = (selectedTile + 1) % tileNames.size();
	}

	public void putOpenSpace(int x, int y, int z) {
		map.put(new Coordinate(x, y, z), new OpenMapSpace(getSelectedTerrainType(), x, y, z));
	}

	public void removeOpenspace(int x, int y, int z) {
		map.remove(new Coordinate(x, y, z));
	}

	public void preFill(int x, int y) {
		LinkedList<Coordinate> positionQueue = new LinkedList<Coordinate>();

		positionQueue.add(new Coordinate(x, y, 0));
		for (int i = 0; !positionQueue.isEmpty(); i++) {
			Coordinate get = positionQueue.pollFirst();

			int numberOfAdjacent = calcAdjacent(get.x(), get.y());
			int addNumber = 0;
			if(numberOfAdjacent == 1 || numberOfAdjacent == 2) {
				addNumber = 2;
			}
			if(numberOfAdjacent == 3) {
				addNumber = 3;
			}
			if(numberOfAdjacent == 4 || numberOfAdjacent == 5) {
				addNumber = 2;
			}
			if(numberOfAdjacent == 6) {
				addNumber = 1;
			}

			for (int k=0; k<addNumber; k++) {
				int dx = new Random().nextInt(3) -1;
				int dy = new Random().nextInt(3) -1;

				while(Math.abs(dx) + Math.abs(dy) != 1) {
					dx = new Random().nextInt(3) -1;
					dy = new Random().nextInt(3) -1;
				}

				if(map.get(new Coordinate(get.x()+dx, get.y()+dy, 0)) == null) {
					putOpenSpace(get.x()+dx, get.y()+dy, 0);
					if(i < 1000) {
						positionQueue.add(new Coordinate(get.x()+dx, get.y()+dy, 0));
					}
				}
			}
		}
	}

	private int calcAdjacent(int x, int y) {
		int rtn = 0;
		for (int i=-1; i<2; i++) {
			for (int j=-1; j<2; j++) {
				if(Math.abs(i) + Math.abs(j) == 1) {
					if(map.get(new Coordinate(i + x, j + y, 0)) != null) {
						rtn ++;
					}
				}
			}	
		}
		return rtn;
	}

	public List<OpenMapSpace> getMapSpaceAsList() {
		return new ArrayList(map.values());
	}

	public void clearMap() {
		map.clear();
	}

	record Coordinate(int x, int y, int z){};//temporary representation of a coordinate, used to save x and y together

}
