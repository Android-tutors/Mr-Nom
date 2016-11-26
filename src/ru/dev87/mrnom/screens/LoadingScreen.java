package ru.dev87.mrnom.screens;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

import ru.dev87.mrnom.Assets;
import ru.dev87.mrnom.LoadingAdapter;
import ru.dev87.mrnom.Settings;

import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;

/**
 * Объект класса создается лишь единожды в начале игры.
 * Это стартовый экран игры. Первый вызов метода update() происходит
 * сразу после создания экрана. Методом setScreen() меняем экран
 * после загрузки настроек и медиа.
 * Остальные методы - лишь заглушки, никогда не используются.
 * 
 * @author Ifgeny
 *
 */

public class LoadingScreen extends Screen {
	float esLogoTimeDelta;
	int esLogoIndex;
	Pixmap esLogo;
	Pixmap loading;
	//
	LoadingAdapter loader;
	//
	int loadingStep = 0;
	// время прошедшее с начала загрузки
	float deltaTime = 0;
	
	public LoadingScreen(Game game) {
		super(game);
		
		// кнопка BACK - выход из игры
		game.getInput().addIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
		
		// загрузчик контента
		loader = new LoadingAdapter(game.getGraphics(), game.getAudio());
	}
	
	@Override
	public void update(float deltaTime) {
		// сколько прошло с начала загрузки
		this.deltaTime += deltaTime;
				
		loader.update();
		if(loader.isComplete) updateLoadingList();
	}
	
	void updateLoadingList() {
		loadingStep++;
		
		// первым шагом загрузчик загружает картинки для заставки
		if(loadingStep == 1) {
			loader.addImage("image/es.png", PixmapFormat.ARGB4444);
			loader.addImage("image/loading.png", PixmapFormat.ARGB4444);
			loader.addImage("image/background.png", PixmapFormat.RGB565);
			return;
		}
		
		// вторым шагом привязываем загруженные объекты
		if(loadingStep == 2) {
			esLogo = loader.getImage(0);
			loading = loader.getImage(0);
			Assets.background = loader.getImage(0);
			return;
		}
		
		// третим шагом добавляем в очередь оставшийся контент
		if(loadingStep == 3) {
			loader.addImage("image/logo.png", PixmapFormat.ARGB4444);
			loader.addImage("image/mainmenu.png", PixmapFormat.ARGB4444);
			loader.addImage("image/buttons.png", PixmapFormat.ARGB4444);
			loader.addImage("image/help1.png", PixmapFormat.ARGB4444);
			loader.addImage("image/help2.png", PixmapFormat.ARGB4444);
			loader.addImage("image/help3.png", PixmapFormat.ARGB4444);
			loader.addImage("image/numbers.png", PixmapFormat.ARGB4444);
			loader.addImage("image/ready.png", PixmapFormat.ARGB4444);
			loader.addImage("image/pause.png", PixmapFormat.ARGB4444);
			loader.addImage("image/gameover.png", PixmapFormat.ARGB4444);
			loader.addImage("image/nom.png", PixmapFormat.ARGB4444);
			
			loader.addSound("sound/pock1.ogg");
			loader.addSound("sound/pock2.ogg");
			loader.addSound("sound/pock3.ogg");
			loader.addSound("sound/nom1.ogg");
			loader.addSound("sound/nom2.ogg");
			loader.addSound("sound/nom3.ogg");
			loader.addSound("sound/start1.ogg");
			loader.addSound("sound/start2.ogg");
			loader.addSound("sound/death.ogg");
			return;
		}
		
		// четвертым шагом привязываем все объекты
		// обязательно в том порядке, в котором они добавлены!!
		if(loadingStep == 4) {
			Assets.logo = loader.getImage(0);
			Assets.mainMenu = loader.getImage(0);
			Assets.buttons = loader.getImage(0);
			Assets.help[0] = loader.getImage(0);
			Assets.help[1] = loader.getImage(0);
			Assets.help[2] = loader.getImage(0);
			Assets.numbers = loader.getImage(0);
			Assets.ready = loader.getImage(0);
			Assets.pause = loader.getImage(0);
			Assets.gameOver = loader.getImage(0);
			Assets.nom = loader.getImage(0);
			
			Assets.pock[0] = loader.getSound(0);
			Assets.pock[1] = loader.getSound(0);
			Assets.pock[2] = loader.getSound(0);
			Assets.eat[0] = loader.getSound(0);
			Assets.eat[1] = loader.getSound(0);
			Assets.eat[2] = loader.getSound(0);
			Assets.start[0] = loader.getSound(0);
			Assets.start[1] = loader.getSound(0);
			Assets.death = loader.getSound(0);
			return;
		}
		
		// пятым шагом загружаются настройки игры
		// обычно это минимально быстрая операция
		if(loadingStep == 5) {
			Settings.load(game.getFileIO());
		}
		
		// если все загружено, переходим к меню
		// но должно пройти минимальное время с начала загрузки, пусть 1 сек
		if(deltaTime > 1f) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void present(float deltaTime) {
		if(loadingStep < 2) return;
		
		Graphics g = game.getGraphics();
		// logo
		esLogoTimeDelta += deltaTime;
		if(esLogoTimeDelta > .1f) {
			esLogoTimeDelta -= .1f;
			esLogoIndex++;
			if(esLogoIndex > 13) esLogoIndex-=14;
		}
		g.clear(0xffffffff);
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(esLogo, 100, 200, esLogoIndex*92, 0, 92, 163);
		g.drawPixmap(loading, 32, 155);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		esLogo.dispose();
		loading.dispose();
	}

}
