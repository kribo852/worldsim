package worldsim.app;

import java.util.Random;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
class Environment {

	List<EnvObject> env = new CopyOnWriteArrayList<>();


	public List<EnvObject> getEnvObjectList() {
		return env;
	}

	public void addEnvObject(int x, int y, int z) {
		Random rnd = new Random();

		env.add(new EnvObject((float)(x+0.5-rnd.nextFloat()), (float)(y+0.5-rnd.nextFloat()), z, EnvType.TREE));
	}

}
