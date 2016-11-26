package ru.dev87.mrnom.screens;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Input.TouchEvent;

import ru.dev87.mrnom.Assets;
import ru.dev87.mrnom.Settings;

public class HighScoreScreen extends Screen {

	public HighScoreScreen(Game game) {
		super(game);
		
		// кнопка BACK - возвращает в меню
		game.getInput().removeIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
	}
	
	@Override
	public void update(float deltaTime) {
		// флаги событий
		boolean clickBack = false;
		
		// проверяем управление с точпада
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(TouchEvent event: touchEvents)
			clickBack = (event.type == TouchEvent.TOUCH_UP);
		
		// проверяем управление с клавиатуры
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		for(KeyEvent event: keyEvents)
			if(event.type == KeyEvent.KEY_UP)
				clickBack = event.keyCode == android.view.KeyEvent.KEYCODE_BACK;
		
		// проверка флагов событий
		if(clickBack) {
			Assets.playPock();
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		// фон
		g.drawPixmap(Assets.background, 0, 0);
		// заголовок
		g.drawPixmap(Assets.mainMenu, 64, 32, 0, 42, 192, 42);
		// кнопка дальше
		g.drawPixmap(Assets.help[0], 0, 390, 0, 390, 320, 90);
		// призовые места
		for(int i=0; i<5; i++)
			drawNumber(g, Integer.toString(i+1) + ". " + Settings.highscores[i], 64, 128 + i * 42);
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
		// TODO Auto-generated method stub

	}
	
	/*
	 * Выводит цифры
	 */
	protected static void drawNumber(Graphics g, String text, int x, int y) {
		int b;
		for(int i=0; i<text.length(); i++) {
			b = text.codePointAt(i);
			if(b == 46) {
				// символ - точка
				g.drawPixmap(Assets.numbers, x, y, 200, 0, 10, 32);
				x += 10;
			} else
			if(b >= 48 & b < 58) {
				// цифры от 0 до 9
				g.drawPixmap(Assets.numbers, x, y, (b-48)*20, 0, 20, 32);
				x += 20;
			} else
				// все остальные символы, включая пробел
				x += 20;
		}
	}

}
