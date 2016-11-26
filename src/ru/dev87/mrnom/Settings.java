package ru.dev87.mrnom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import android.content.Context;
import android.os.Environment;

public class Settings {
	public static Random rand = new Random();
	//
	private static List<String> saveDirs = null;
	//
	static AndroidGame androidGame;
	// настройка звука
	public static boolean soundEnabled = true;
	// список лучших результатов
	public static int[] highscores = new int[] { 100, 80, 50, 30, 10 };

	/**
	 * Инит приложения
	 * 
	 * @param context
	 */
	public static void init(AndroidGame androidGame) {
		Settings.androidGame = androidGame;

		// папки для сохранения
		Settings.saveDirs = new ArrayList<String>();

		// если доступно внешнее хранилище по релевантности
		if (androidGame.getFileIO().isExternalStorageWritable()) {
			saveDirs.add(Environment.getExternalStorageDirectory() + File.separator + "Game Saves");
			saveDirs.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator
					+ "Game Saves");
			saveDirs.add(androidGame.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
		}

		saveDirs.add(androidGame.getFilesDir().getAbsolutePath());
	}

	/**
	 * Чтение настроек из файла
	 * 
	 * @param files
	 */
	public static void load(FileIO files) {
		String saveFileName = androidGame.getText(R.string.app_name) + ".save";
		BufferedReader in = null;

		// по порядку пробегаю все доступные хранилища
		for (String path : saveDirs) {
			File dir = new File(path);
			if (dir.exists())
				try {
					File file = new File(dir, saveFileName);
					if (!file.canRead())
						continue;
					in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				} catch (IOException e) {
					e.printStackTrace();
					in = null;
				}
		}

		// если ни один из файлов не удалось открыть для чтения, выходим
		if (in == null)
			return;

		try {
			soundEnabled = Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < 5; i++)
				highscores[i] = Integer.parseInt(in.readLine());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Сохранение настроек в файле
	 * 
	 * @param files
	 */
	public static void save(FileIO files) {
		String saveFileName = androidGame.getText(R.string.app_name) + ".save";
		BufferedWriter out = null;

		// по порядку пробегаю все доступные хранилища
		for (String path : saveDirs) {
			File dir = new File(path);
			if (dir.mkdirs() || dir.exists())
				try {
					File file = new File(dir, saveFileName);
					if(!file.canWrite())
						continue;
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				} catch (IOException e) {
					e.printStackTrace();
					out = null;
				}
		}

		// если ни один из файлов не удалось открыть для записи, выходим
		if (out == null)
			return;

		try {
			out.write(Boolean.toString(soundEnabled) + "\n");
			for (int i = 0; i < 5; i++)
				out.write(Integer.toString(highscores[i]) + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Проверяет, доступна ли внешняя память для записи
	 * 
	 * @return
	 */

	public static void addScore(int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}
}
