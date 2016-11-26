package ru.dev87.mrnom.models;

import java.util.ArrayList;
import java.util.List;

import ru.dev87.mrnom.Assets;
import ru.dev87.mrnom.Settings;
import ru.dev87.mrnom.SnakeBody;

public class Nom {
	public List<SnakeBody> body = new ArrayList<SnakeBody>();
	public List<Stain> stains = new ArrayList<Stain>();
	
	// размер мира
	public static int worldWidth = 9;
	public static int worldHeight = 12;
	
	public float speed;						// скорость движения в секунду
	public static float incSpeed = 0.5f;	// увеличение скорости при поедании кляксы
	private float pathPart;					// сколько пройдено от сектора за прошлый update()
	
	public int rotate;		// направление движения (0-вправо, 1-вниз, 2-влево, 3-вверх)
	public boolean death;	// признак конца игры
	
	public Nom() {
		// создаем змея головой в середине мира
		int x = (int)(worldWidth / 2);
		int y = (int)(worldHeight / 2);
		for(int i=0; i<3; i++)
			body.add(new SnakeBody(x-i, y));
		// задаем начальную скорость
		speed = 1.5f;
		// изначально направляем вправо
		rotate = 0;
		// добавим в мир первую кляксу
		addStain();
		// пока живой
		death = false;
	}
	
	/*
	 * Обновление среды
	 */
	public void update(float deltaTime) {
		if(death) return;
		
		pathPart += deltaTime * speed;
		// проходим каждую клетку
		while(pathPart > 1f) {
			pathPart--;
			move();
		}
	}

	/*
	 * Перемещения змея на клетку вперед по направлению
	 */
	private void move() {
		if(death) return;
		
		// хвост змея перемещается за головой
		for(int i=body.size()-1; i>0; i--) {
			body.get(i).x = body.get(i-1).x;
			body.get(i).y = body.get(i-1).y;
		}
		// голова змея движется по направлению
		switch(rotate) {
		case 0:	// шаг вправо
			body.get(0).x++;
			if(body.get(0).x >= worldWidth) body.get(0).x -= worldWidth;
			break;
		case 1:	// шаг вниз
			body.get(0).y++;
			if(body.get(0).y >= worldHeight) body.get(0).y -= worldHeight;
			break;
		case 2:	// шаг влево
			body.get(0).x--;
			if(body.get(0).x < 0) body.get(0).x += worldWidth;
			break;
		case 3:	// шаг влево
			body.get(0).y--;
			if(body.get(0).y < 0) body.get(0).y += worldHeight;
			break;
		}
		// проверка на поедание кляксы
		int i = checkStainColision(body.get(0).x, body.get(0).y);
		if(i > -1) {
			// змей ест кляксу
			stains.remove(i);
			// змей растет
			grow();
			// новая клякса появляется в мире
			addStain();
			// змей растет, растут и характеристики
			// например, скорость
			speed += incSpeed;
			// змей поедает кляксу с хрустом
			Assets.playNom();
		}
		// проверка на ганибализм
		i = checkTailColision(body.get(0).x, body.get(0).y);
		if(i > -1) {
			// змей укусил сам себя, он не выжил после такого
			death = true;
			// играет похоронный марш
			Assets.playDeath();
		}
	}
	
	/*
	 * Проверка на столкновение с кляксой
	 */
	private int checkStainColision(int x, int y) {
		for(int i=0; i<stains.size(); i++)
			if(stains.get(i).x == x & stains.get(i).y == y)
				return i;
		return -1;
	}
	
	/*
	 * Проверка на столкновение с хвостом
	 */
	private int checkTailColision(int x, int y) {
		for(int i=1; i<body.size(); i++)
			if(body.get(i).x == x & body.get(i).y == y)
				return i;
		return -1;
	}
	
	/*
	 * Проверка на столкновение с головой
	 */
	private boolean checkHeadColision(int x, int y) {
		if(body.get(0).x == x & body.get(0).y == y)
			return true;
		return false;
	}
	
	/*
	 * Змей поворачивает направо
	 */
	public void turnRight() {
		if(death) return;
		
		rotate++;
		if(rotate >= 4) rotate -= 4;
	}
	
	/*
	 * Змей поворачивает налево
	 */
	public void turnLeft() {
		if(death) return;
		
		rotate--;
		if(rotate < 0) rotate += 4;
	}
	
	/*
	 * По какой-то причине змей вырос
	 */
	public void grow() {
		if(death) return;
		
		int len = body.size();
		body.add(new SnakeBody(body.get(len-1).x, body.get(len-1).y));
	}
	
	/*
	 * В мире появляется новая клякса
	 */
	public void addStain() {
		if(death) return;
		
		int x, y;
		
		for(int i=0; i<worldWidth*worldHeight; i++) {
			x = Settings.rand.nextInt(worldWidth);
			y = Settings.rand.nextInt(worldHeight);
			// проверка с другими кляксами
			if(checkStainColision(x, y) >= 0)
				continue;
			// проверка с хвостом
			if(checkTailColision(x, y) >= 0)
				continue;
			// проверка с головой
			if(checkHeadColision(x, y))
				continue;
			// новая клякса ни с чем в мире не пересекается
			// новой кляксе - жить!
			stains.add(new Stain(x, y, Settings.rand.nextInt(3)));
			return;
		}
	}
}
