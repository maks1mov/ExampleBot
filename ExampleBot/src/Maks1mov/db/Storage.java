package Maks1mov.db;

import java.io.File;

import bukkitYaml.YAML;
import bukkitYaml.file.FileConfiguration;

public class Storage {

	private YAML yamlStorage;
	
	public Storage() {
		
		try {
			yamlStorage = new YAML(new File("storage.yml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void set(String path, Object value) {
		
		yamlStorage.getYAML().set(path, value);
		yamlStorage.save();
	}
	
	public Object get(String path) {
		return yamlStorage.getYAML().get(path);
	}
	
	public int getInt(String path) {
		return yamlStorage.getYAML().getInt(path);
	}
	
	public boolean getBoolean(String path) {
		return yamlStorage.getYAML().getBoolean(path);
	}
	
	public String getString(String path) {
		return yamlStorage.getYAML().getString(path);
	}
	
	public FileConfiguration getInsYAML() {
		return yamlStorage.getYAML();
	}
	
	public YAML getInstance() {
		return yamlStorage;
	}
}