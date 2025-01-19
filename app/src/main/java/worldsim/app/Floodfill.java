package worldsim.app;

import java.lang.Math;
import java.util.function.BiPredicate;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;


public class Floodfill {

	int maxDepth = 10000; 
	int achivedDepth = 0;

	public Floodfill(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	List<Coordinate> floodFill(int startx, int starty, BiPredicate<Integer, Integer> spacetobefilled) {
		LinkedList<Coordinate> positionQueue = new LinkedList<Coordinate>();
		Set<Coordinate> used = new HashSet<>();
		List<Coordinate> rtn = new ArrayList<>();

		positionQueue.add(new Coordinate(startx, starty));
		used.add(new Coordinate(startx, starty));

		for(int iteration = 0; !positionQueue.isEmpty() && iteration < maxDepth; iteration++ ) {
			Coordinate c = positionQueue.pollFirst();

			if(spacetobefilled.test(c.getX(), c.getY())) {
				rtn.add(c);
				achivedDepth++;

				for(int i = -1; i < 2; i++ ) {
					for(int j = -1; j < 2; j++ ) {
						int diff = Math.abs(i) + Math.abs(j); 
						if(diff == 1) {
							Coordinate testNewC = new Coordinate(c.getX() + i, c.getY() + j);
							if(!used.contains(testNewC)) {
								positionQueue.add(testNewC);
								used.add(testNewC);
							}
						}
					}	
				}
			}
		}

		return rtn;
	}

	@Getter
	@AllArgsConstructor
	public static class Coordinate {
		int x;
		int y;
	}

}
