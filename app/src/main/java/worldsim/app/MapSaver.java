package worldsim.app;

import java.util.List;

interface MapSaver {
	
	public void saveToFile(List<OpenMapSpace> mapspace, List<EnvObject> environment);


}