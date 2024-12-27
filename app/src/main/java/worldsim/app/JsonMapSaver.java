package worldsim.app;

import java.util.List;
import java.io.IOException;
import java.io.FileWriter;

import lombok.AllArgsConstructor;

import com.google.gson.Gson;

class JsonMapSaver implements MapSaver {
	
	
	public void saveToFile(List<OpenMapSpace> mapspace, List<EnvObject> environment) {

		Gson gson = new Gson();

		try (FileWriter writer = new FileWriter("savedMap.json")){
			gson.toJson(new SavObj(mapspace, environment), writer);
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	record SavObj(List<OpenMapSpace> mapspace, List<EnvObject> environment) {}


}