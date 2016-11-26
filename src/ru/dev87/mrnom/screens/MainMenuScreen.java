package ru.dev87.mrnom.screens;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import ru.dev87.mrnom.Assets;
import ru.dev87.mrnom.Settings;

public class MainMenuScreen extends Screen {
	
	float r = 0;
	
	public MainMenuScreen(Game game) {
		super(game);
		// кнопка BACK - выход из игры
		//game.getInput().addIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
	}
	
	@Override
	public void update(float deltaTime) {
		// флаги событий
		boolean clickStart = false,
				clickScores = false,
				clickHelp = false,
				clickMute = false,
				clickExit = false;
		
		// проверяем управление с точпада
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(TouchEvent event: touchEvents) {
			if(event.type == TouchEvent.TOUCH_UP) {
				event = event.mul(game.getScreenScale());
				// кнопка звука
				clickMute = inBounds(event, 16, 390, 64, 64);
				clickStart = inBounds(event, 64, 220, 192, 42);
				clickScores = inBounds(event, 64, 220+42, 192, 42);
				clickHelp = inBounds(event, 64, 220+84, 192, 42);
			}
		}
		
		// проверяем управление с клавиатуры
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		for(KeyEvent event: keyEvents) {
			if(event.type == KeyEvent.KEY_UP) {
				clickHelp = event.keyCode == android.view.KeyEvent.KEYCODE_SEARCH;
				clickScores = event.keyCode == android.view.KeyEvent.KEYCODE_MENU;
				clickExit = event.keyCode == android.view.KeyEvent.KEYCODE_BACK;
			}
		}
		
		// проверка флагов событий
		if(clickMute) {
			Settings.soundEnabled = !Settings.soundEnabled;
			Settings.save(game.getFileIO());
			Assets.playPock();
		}
		else if(clickStart) {
			Assets.playStart();
			game.setScreen(new GameScreen(game));
		}
		else if(clickScores) {
			Assets.playPock();
			game.setScreen(new HighScoreScreen(game));
		}
		else if(clickHelp) {
			Assets.playPock();
			game.setScreen(new HelpScreen(game));
		}
		else if(clickExit) {
			Assets.playPock();
			((AndroidGame)game).finish();
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		
		r+= deltaTime;
		
		int
			x = 16 + (int)(Math.cos(r) * 10),
			y = 390 + (int)(Math.sin(r) * 10);
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.logo, 32, 20);
		g.drawPixmap(Assets.mainMenu, 64, 220);
		g.drawPixmap(Assets.buttons, x, y, Settings.soundEnabled ? 0 : 64, 0, 64, 64);
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	/*
	 * Входит ли точка в регион
	 */
	protected static boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		return (event.x > x && event.x < x + width 
				&& event.y > y && event.y < y + height);
	}
	
}
