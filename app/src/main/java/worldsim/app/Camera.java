package worldsim.app;


import lombok.Getter;


@Getter
public class Camera {
	
	int px = 0;
	int py = 0;
	int pz = 0;
	int cursorx = 10;
	int cursory = 10;


	void move(int dx, int dy){
		px += dx;
		py += dy;
	}

	void moveZaxis(int dz) {
		pz += dz;
	}

	void moveCursor(int dcursorx, int dcursory) {
		cursorx += dcursorx;
		cursory += dcursory;
	}


}