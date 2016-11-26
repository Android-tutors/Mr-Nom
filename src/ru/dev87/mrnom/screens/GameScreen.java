package ru.dev87.mrnom.screens;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Input.TouchEvent;

import ru.dev87.mrnom.Assets;
import ru.dev87.mrnom.Settings;
import ru.dev87.mrnom.models.Nom;

public class GameScreen extends Screen {
	Nom nom;
	String score;
	boolean gamePaused;
	boolean gameStarted;
	float deathTimeDelta = 0;
	// игнорируется ли качелька
	boolean isVolumeControlIgnored;

	public GameScreen(Game game) {
		super(game);
		
		// кнопка BACK - выход из игры
		game.getInput().addIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
		
		// создаем героя
		nom = new Nom();
		
		// обозначаем что игра не начиналась
		gameStarted = false;
		
		// кнопка BACK - возвращает в меню
		game.getInput().removeIgnoredKey(android.view.KeyEvent.KEYCODE_BACK);
		
		// качелька управляет поворотом
		isVolumeControlIgnored = game.getInput().checkIgnoredKey(android.view.KeyEvent.KEYCODE_VOLUME_DOWN);
		game.getInput().removeIgnoredKey(android.view.KeyEvent.KEYCODE_VOLUME_DOWN);
		game.getInput().removeIgnoredKey(android.view.KeyEvent.KEYCODE_VOLUME_UP);
	}
	
	@Override
	public void update(float deltaTime) {
		// насчитывается время с момента укуса героя
		if(nom.death)
			deathTimeDelta += deltaTime;
		
		// даем герою жить
		if(!gamePaused && gameStarted && !nom.death)
			nom.update(deltaTime);
		
		// флаги событий
		boolean 
			touchAnyWhere = false,
			touchLeft = false,
			touchRight = false,
			touchPause = false,
			touchUnpause = false,
			touchQuit = false,
			clickLeft = false,
			clickRight = false,
			clickBack = false;
		
		// проверяем управление с точпада
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(TouchEvent event: touchEvents) {
			event = event.mul(game.getScreenScale());
			if(event.type == TouchEvent.TOUCH_UP) {
				// нажато где-то на экране
				touchAnyWhere = true;
				touchUnpause = MainMenuScreen.inBounds(event, 80, 192, 160, 48); 
				touchQuit = MainMenuScreen.inBounds(event, 80, 240, 160, 48); // второе событие BACK
			}
			else if(event.type == TouchEvent.TOUCH_DOWN) {
				// нижняя четверть экрана для события BACK
				if(event.y > 440)
					touchPause = true;
				// остаток левой части экарана - поворот змея влево 
				else if(event.x < 160)
					touchLeft = true;
				else
					// правая часть - вправо
					touchRight = true;
			}
		}
		
		// проверяем управление с клавиатуры
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		for(KeyEvent event: keyEvents)
			if(event.type == KeyEvent.KEY_UP) {
				clickBack = (event.keyCode == android.view.KeyEvent.KEYCODE_BACK
						| event.keyCode == android.view.KeyEvent.KEYCODE_MENU);
				clickLeft = (event.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT
						| event.keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN);
				clickRight = (event.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT
						| event.keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP);
			}
		
		// проверка событий
		if(nom.death) {
			// когда игра закончилась, никаких нажатий не учитывается
			// дается 3 сек чтобы игроку запомнить результат
			if(deathTimeDelta > 3) {
				game.setScreen(new MainMenuScreen(game));
				Settings.addScore(10 * (nom.body.size() - 3));
			}
		}
		else if(!gameStarted) {
			// если игра еще не начата ожидается событие touchAnyWhere
			if(touchAnyWhere)
				gameStarted = true;
		}
		else if(gamePaused) {
			// во время паузы ожидается событие clickBack или clickUnpause
			if(clickBack | touchUnpause)
				gamePaused = false;
			else if(touchQuit) {
				Assets.playPock();
				game.setScreen(new MainMenuScreen(game));
			}
		}
		else	// иначе обрабатываем игрвоой режим
		{
			if(clickBack | touchPause)
				gamePaused = true;
			else if(clickLeft | touchLeft)
				nom.turnLeft();
			else if(clickRight | touchRight)
				nom.turnRight();
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		// фон
		g.drawPixmap(Assets.background, 0, 0);
		// счет
		score = Integer.toString(10 * (nom.body.size() - 3));
		HighScoreScreen.drawNumber(g, score, 160 - 10*score.length(), 410);
		// игра окончена?
		if(nom.death) {
			g.drawPixmap(Assets.gameOver, 64, 200);
		} else
		// игра в процессе
		if(!gamePaused || !gameStarted)
		{
			// отрисовка кляксы
			for(int i=nom.stains.size()-1; i>=0; i--)
				g.drawPixmap(Assets.nom, 11 + nom.stains.get(i).x * 32, 11 + nom.stains.get(i).y * 32, 
						nom.stains.get(i).type * 42, 0, 42, 42);
			// отрисовка героя
			for(int i=nom.body.size()-1; i>0; i--)
				g.drawPixmap(Assets.nom, 11 + nom.body.get(i).x * 32, 11 + nom.body.get(i).y * 32, 126, 0, 42, 42);
			// отрисовка головы
			g.drawPixmap(Assets.nom, 6 + nom.body.get(0).x * 32, 6 + nom.body.get(0).y * 32,
					(4 + nom.rotate) * 42, 0, 42, 42);
		}
		if(!gameStarted)		
			// если игра не началась еще, предлагать щелкнуть по экрану чтобы начать
			g.drawPixmap(Assets.ready, 64, 100);
		else if(gamePaused)
			// во время паузы отображается запрос снять с паузы
			g.drawPixmap(Assets.pause, 80, 192);
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
		// возвращаем кнопки игнора
		if(isVolumeControlIgnored) {
			game.getInput().addIgnoredKey(android.view.KeyEvent.KEYCODE_VOLUME_DOWN);
			game.getInput().addIgnoredKey(android.view.KeyEvent.KEYCODE_VOLUME_UP);
		}
	}

}
