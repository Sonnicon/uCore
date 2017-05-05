package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.util.Mathf;

public class Sounds{
	private static ObjectMap<String, Sound> map = new ObjectMap<>();
	private static float volume = 1f;
	private static float falloff = 20000f;
	
	public static void load(String... names){
		for(String s : names){
			map.put(s.split("\\.")[0], Gdx.audio.newSound(Gdx.files.internal("sounds/"+s)));
		}
	}
	
	public static void setFalloff(float afalloff){
		falloff = afalloff;
	}
	
	public static Sound get(String name){
		return map.get(name);
	}
	
	public static void playDistance(String name, float distance){
		if(distance < 1) distance = 1;
		play(name, Mathf.clamp(1f/(distance*distance/falloff)));
	}
	
	public static void play(String name){
		play(name, 1f);
	}
	
	public static void play(String name, float tvol){
		if(!map.containsKey(name)) throw new IllegalArgumentException("Sound \""+name+"\" does not exist!");
		float vol = Settings.getInt("sfxvol", 10)/10f;
		get(name).play(volume*vol*tvol);
	}
	
	public static void setVolume(float vol){
		volume = vol;
	}
}
