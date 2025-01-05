package worldsim.app;

import java.util.Random;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.lang.Math;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
class Environment {

	List<EnvObject> env = new CopyOnWriteArrayList<>();

	@Setter
 	List<String> objectnames; 

 	int selected = 0;


 	public void cycleSelected() {
		selected = (selected + 1) % objectnames.size();
	}


	public List<EnvObject> getEnvObjectList() {
		return env;
	}

	public void addEnvObject(float x, float y, int z) {
		Random rnd = new Random();

		env.add(new EnvObject(x, y, z, objectnames.get(selected)));
	}

	public void removeInProximity(float x, float y) {
		env = env.stream().filter(envObj -> Math.hypot(envObj.x() - x, envObj.y() - y) > 1.0)
		.collect(Collectors.toCollection(CopyOnWriteArrayList::new));
	}

}
