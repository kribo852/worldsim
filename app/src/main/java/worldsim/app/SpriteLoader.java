package worldsim.app;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;


import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class SpriteLoader {

	List<SpriteMetadata> spritemetadata;
	BufferedImage sprites;

	
	public SpriteLoader(String fileName) {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(fileName + "_metadata.json"));
			Type listType = new TypeToken<ArrayList<SpriteMetadata>>(){}.getType();

			spritemetadata = gson.fromJson(reader, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			sprites = ImageIO.read(new File(fileName + ".png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	public BufferedImage getSpriteFromName(String spritename) {
		for (int i=0; i<spritemetadata.size(); i++) {
			if (spritename.equals(spritemetadata.get(i).getSpritename())) {
				return sprites.getSubimage(
					spritemetadata.get(i).getXcoordinate(), 
					spritemetadata.get(i).getYcoordinate(),
					20,
					20
				);
			}		
		}	
		return null;
	}

	public List<String> getSpritenames() {
		return spritemetadata.stream().map(sprite -> sprite.getSpritename()).collect(Collectors.toList());
	}


	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	class SpriteMetadata {
		Integer xcoordinate;
		Integer ycoordinate;
		String spritename;
		Integer width;
		Integer height; 
	}

} 
