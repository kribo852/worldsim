package worldsim.app;

import java.util.Random;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.lang.Math;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
class Environment {

	List<EnvObject> env = new CopyOnWriteArrayList<>();


	public List<EnvObject> getEnvObjectList() {
		return env;
	}

	public void addEnvObject(float x, float y, int z) {
		Random rnd = new Random();

		env.add(new EnvObject(x, y, z, EnvType.TREE));
	}

	public void removeInProximity(float x, float y) {
		env = env.stream().filter(envObj -> Math.hypot(envObj.x() - x, envObj.y() - y) > 1.0)
		.collect(Collectors.toCollection(CopyOnWriteArrayList::new));
	}

}
