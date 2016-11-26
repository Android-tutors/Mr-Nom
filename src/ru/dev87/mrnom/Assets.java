package ru.dev87.mrnom;

import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

public class Assets {
	public static Pixmap background;
	public static Pixmap logo;
	public static Pixmap mainMenu;
	public static Pixmap buttons;
	public static Pixmap[] help = new Pixmap[3];
	public static Pixmap numbers;
	public static Pixmap ready;
	public static Pixmap pause;
	public static Pixmap gameOver;
	public static Pixmap nom;
	
	public static Sound[] pock = new Sound[3];
	public static Sound[] eat = new Sound[3];
	public static Sound[] start = new Sound[2];
	public static Sound death;
	
	/*
	 * Если звук в настройках включен, то воспроизводит звук щелчка по меню
	 */
	public static void playPock() {
		if(!Settings.soundEnabled) return;
		pock[Settings.rand.nextInt(3)].play(1);
	}
	
	/*
	 * Если звук в настройках включен, то воспроизводит звук начала новой игры
	 */
	public static void playStart() {
		if(!Settings.soundEnabled) return;
		start[Settings.rand.nextInt(2)].play(1);
	}
	
	/*
	 * Если звук в настройках включен, то воспроизводит звук поедания кляксы
	 */
	public static void playNom() {
		if(!Settings.soundEnabled) return;
		eat[Settings.rand.nextInt(3)].play(1);
	}
	
	/*
	 * Если звук в настройках включен, то воспроизводит СТРАШНЫЙ ЗВУК СМЕЕЕЕРТИ!!!
	 */
	public static void playDeath() {
		if(!Settings.soundEnabled) return;
		death.play(1);
	}
}
