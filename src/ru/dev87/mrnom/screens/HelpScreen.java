package ru.dev87.mrnom.screens;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Input.TouchEvent;

import ru.dev87.mrnom.Assets;

public class HelpScreen extends Screen {
	int screenIndex = 0;

	public HelpScreen(Game game) {
		super(game);
		
		// кнопка BACK - возвращает в меню
		game.getInput().removeIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
	}
	
	@Override
	public void update(float deltaTime) {
		// флаги событий
		boolean clickNext = false,
				clickBack = false;
		
		// проверяем управление с точпада
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(TouchEvent event: touchEvents)
			clickNext = (event.type == TouchEvent.TOUCH_UP);
		
		// проверяем управление с клавиатуры
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		for(KeyEvent event: keyEvents)
			if(event.type == KeyEvent.KEY_UP)
				clickBack = event.keyCode == android.view.KeyEvent.KEYCODE_BACK;
		
		// проверка флагов событий
		if(clickNext) {
			Assets.playPock();
			screenIndex++;
			if(screenIndex > 2)
				game.setScreen(new MainMenuScreen(game));
		}
		else if(clickBack) {
			Assets.playPock();
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		// фон
		g.drawPixmap(Assets.background, 0, 0);
		// лист помощи
		g.drawPixmap(Assets.help[screenIndex], 0, 0);
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

}
