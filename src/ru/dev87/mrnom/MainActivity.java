package ru.dev87.mrnom;

import android.content.Context;
import android.os.Bundle;
import ru.dev87.mrnom.screens.LoadingScreen;

import java.io.File;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

/**
 * Класс MrNomGame - точка входа в приложение
 * 
 * Класс наследует активность AndroidGame, поэтому его можно в манифесте 
 * сделать заглавным и назначить как стартовую активность.
 * Но, для того чтобы что-то началось, нужно создать первый экран. Это
 * будет экран класса LoadingScreen. Для стартовой активности подменяется
 * метод getStartScreen(), который назначается для получения первого экрана -
 * точки входа.
 * 
 * @author Ifgeny
 *
 */

/*
 * Логи:
 * 
 * 2013-04-14 1542: Apk весит 910 Кб, папка Assets 752 Кб, попробую 
 * оптимизировать. Не хватило выдержки. Не стал тратить время на такую простую
 * игрушку. 
 */

public class MainActivity extends AndroidGame {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Settings.init(this);
		// загрузка шрифта
		//getGraphics().loadFont("Pro", "font/ProFontWindows1.ttf");
	}
	
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
