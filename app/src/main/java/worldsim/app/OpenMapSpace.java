package worldsim.app;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class OpenMapSpace {
	String terrainType;
	int x;
	int y;
	int z;
}