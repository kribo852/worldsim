package worldsim.app;

import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class SpriteLoader {

	SpriteMetadata spritemetadata;
	BufferedImage sprites;

	
	public SpriteLoader(String fileName) {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(fileName + "_metadata.json"));
			spritemetadata = gson.fromJson(reader, SpriteMetadata.class);
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
		for (int i=0; i<spritemetadata.getSpritenames().size(); i++) {
			if (spritename.equals(spritemetadata.getSpritenames().get(i))) {
				return sprites.getSubimage(
					spritemetadata.getXcoordinates().get(i), 
					spritemetadata.getYcoordinates().get(i),
					20,
					20
				);
			}		
		}	
		return null;
	}

	public List<String> getSpritenames() {
		return spritemetadata.getSpritenames();
	}


	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	class SpriteMetadata {
		List<Integer> xcoordinates;
		List<Integer> ycoordinates;
		List<String> spritenames; 
	}

} 
