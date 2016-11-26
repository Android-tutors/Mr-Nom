package ru.dev87.mrnom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

public class LoadingAdapter {
	public boolean isComplete = false;
	//
	Graphics graphics;
	Audio audio;
	// очередь на загрузку
	List<SoundSource> soundsQueue = new ArrayList<SoundSource>();
	List<ImageSource> imagesQueue = new ArrayList<ImageSource>();
	// загруженные объекты
	List<SoundSource> soundsLoaded = new ArrayList<SoundSource>();
	List<ImageSource> imagesLoaded = new ArrayList<ImageSource>();
	
	private class SoundSource {
		public Sound sound;
		public String assetFileName;
		
		public SoundSource(String assetFileName) {
			this.assetFileName = assetFileName;
		}
	}
	
	private class ImageSource {
		public Pixmap pixmap;
		public String assetFileName;
		public PixmapFormat format;
		
		public ImageSource(String assetFileName, PixmapFormat format) {
			this.assetFileName = assetFileName;
			this.format = format;
		}
	}
	
	/**
	 * При создании экземпляра адаптера указываем объекты-фабрики обработки графики и звука.
	 * 
	 * @param graphics
	 * @param audio
	 */
	public LoadingAdapter(Graphics graphics, Audio audio) {
		this.graphics = graphics;
		this.audio = audio;
	}
	
	public void addSound(String assetFileName) {
		soundsQueue.add(new SoundSource(assetFileName));
		isComplete = false;
	}
	
	public void addImage(String assetFileName, PixmapFormat format) {
		imagesQueue.add(new ImageSource(assetFileName, format));
		isComplete = false;
	}
	
	public int checkSound(String assetFileName) {
		for(int i=0; i<soundsLoaded.size(); i++)
			if(soundsLoaded.get(i).assetFileName.equals(assetFileName))
				return i;
		return -1;
	}
	
	public int checkImage(String assetFileName) {
		for(int i=0; i<imagesLoaded.size(); i++)
			if(imagesLoaded.get(i).assetFileName.equals(assetFileName))
				return i;
		return -1;
	}
	
	public Sound getSound(int index) {
		if(index < 0 | index >= soundsLoaded.size()) return null;
		return ((SoundSource)soundsLoaded.remove(index)).sound;
	}
	
	public Pixmap getImage(int index) {
		if(index < 0 | index >= imagesLoaded.size()) return null;
		return ((ImageSource)imagesLoaded.remove(index)).pixmap;
	}
	
	/*
	 * Метод вызывается для загрузки указанных ресурсов
	 */
	public void update() {
		if(isComplete) return;
		
		// догружаем незагруженные звуки
		if(soundsQueue.size() > 0) {
			loadNextSound();
			return;
		}
		
		// догружаем незагруженные картинки
		if(imagesQueue.size() > 0) {
			loadNextImage();
			return;
		}
		
		isComplete = true;
	}
	
	private void loadNextSound() {
		SoundSource ss = soundsQueue.remove(0);
		ss.sound = audio.newSound(ss.assetFileName);
		soundsLoaded.add(ss);
	}
	
	private void loadNextImage() {
		ImageSource is = imagesQueue.remove(0);
		is.pixmap = graphics.newPixmap(is.assetFileName, is.format);
		imagesLoaded.add(is);
	}
}
