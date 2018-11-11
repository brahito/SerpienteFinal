package brayan_valeria_taller2;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PImage;

public class Serpiente extends Thread {
	private PApplet app;
	private PImage cabeza1, cabeza2, cabeza3, cabeza4, segmento;

	private int up = 1, down = 2, left = 3, right = 4;
	private int direction, contadorHielo, contadorCafe, contadorDientes;
	private boolean vivo, congelado, encafeinado, dientes, hongo;
	private int n = 1;
	private float tam;
	private LinkedList<Float> x, y;
	private Mundo mundo;

	public Serpiente(PApplet app, Mundo mundo) {
		this.app = app;
		this.mundo = mundo;
		x = new LinkedList<Float>();
		y = new LinkedList<Float>();
		cabeza1 = app.loadImage("data/headdown.png");
		cabeza2 = app.loadImage("headup.png");
		cabeza3 = app.loadImage("headright.png");
		cabeza4 = app.loadImage("headleft.png");
		segmento = app.loadImage("body.png");

		tam = 20;
		contadorHielo = 0;
		contadorCafe = 0;
		contadorDientes = 0;
		vivo = true;
		encafeinado = false;
		congelado = false;
		dientes = false;
		hongo = false;
		direction = right;
		x.add(50.0f);
		y.add(50.0f);

	}

	public void pintar() {
		// serpiente
		crearSerpiente();

	}

	/**
	 * Se establece los estados de la serpiente (crecer, perder fresas), la
	 * interaccion con el enemigo, con los bonificadores, las fresas
	 */
	public void run() {
		try {
			while (vivo) {
				if (congelado == false) {
					moverSerpiente();
				}
				if (congelado == true) {
					contadorHielo++;

				}
				if (contadorHielo == 40) {
					contadorHielo = 0;
					congelado = false;
				}
				if (encafeinado == true) {
					tam = 30;
					contadorCafe++;
				} else {
					tam = 20;
				}

				if (contadorCafe == 100) {
					contadorCafe = 0;
					encafeinado = false;
				}
				if (dientes == true) {
					contadorDientes++;
				}
				if (contadorDientes == 50) {
					contadorDientes = 0;
					dientes = false;
				}
				if (validar(mundo.getRecurso()) == true) {
					agregarCola();
					mundo.masContadorFresa(1);
					mundo.getRecurso().ponerFresa();
				}
				for (int i = 0; i < mundo.getBonificadores().size(); i++) {
					if (validarBonificador(mundo.getBonificadores().get(i)) == true) {
						if (mundo.getBonificadores().get(i) instanceof Hongo) {
							hongo = true;
							quitarColaMitad();
							mundo.BajarContadorFresa(mundo.getContadorFresa() / 2);
						}
						if (mundo.getBonificadores().get(i) instanceof Hielo) {
							congelado = true;
						}
						if (mundo.getBonificadores().get(i) instanceof Cafe) {
							encafeinado = true;
						}
						if (mundo.getBonificadores().get(i) instanceof Dientes) {
							dientes = true;
						}
						mundo.getBonificadores().remove(i);

					}
				}
				for (int i = 0; i < mundo.getAranas().size(); i++) {
					if (validarArana(mundo.getAranas().get(i)) == true && n > mundo.getAranas().get(i).getFresas()
							&& mundo.getAranas().get(i).getFresas() >= 1) {
						mundo.getAranas().get(i).quitarFresa();
						agregarCola();
						mundo.masContadorFresa(1);
					}
					if (validarArana(mundo.getAranas().get(i)) == true && n > mundo.getAranas().get(i).getFresas()
							&& mundo.getAranas().get(i).getFresas() >= 1 && dientes == true) {
						agregarColaRobo(mundo.getAranas().get(i));
						mundo.getAranas().get(i).quitarTodo();
					}
				}

				sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Este metodo se encarga de la creacion de la serpiente
	 */
	void crearSerpiente() {
		for (int i = 0; i < n; i++) {
			float xLocation = x.get(i);
			float yLocation = y.get(i);

			if (xLocation > app.width + tam / 2)
				x.set(i, (float) (0.0 + tam / 2));
			if (xLocation < 0 - tam / 2)
				x.set(i, (float) (app.width - tam / 2));

			if (yLocation >= app.height + tam / 2)
				y.set(i, (float) (0.0 + tam / 2));
			if (yLocation <= 0 - tam / 2)
				y.set(i, (float) (app.height - tam / 2));

			if (i == x.size() - 1) {
				app.imageMode(app.CENTER);
				if (direction == down) {
					app.image(cabeza1, xLocation, yLocation, tam, tam);
				}
				if (direction == up) {
					app.image(cabeza2, xLocation, yLocation, tam, tam);
				}
				if (direction == right) {
					app.image(cabeza3, xLocation, yLocation, tam, tam);
				}
				if (direction == left) {
					app.image(cabeza4, xLocation, yLocation, tam, tam);
				}
				app.imageMode(app.CORNER);
			} else {
				app.imageMode(app.CENTER);
				app.image(segmento, xLocation, yLocation, tam, tam);
				app.imageMode(app.CORNER);
			}
		}
	}

	/**
	 * Validacion de contacto entre serpiente y fresa
	 * 
	 * @param fresa
	 * @return verdadero si hay "contacto" con un recurso fresa, de lo contrario
	 *         falso
	 */
	public boolean validar(Recurso fresa) {
		if (PApplet.dist(x.get(x.size() - 1), y.get(y.size() - 1), fresa.getX(), fresa.getY()) < tam) {
			return true;
		} else {
			return false;

		}
	}

	/**
	 * Validacion de contacto entre serpiente y bonificador
	 * 
	 * @param boni
	 * @return verdadero si hay "contacto" con un bonificador, de lo contrario falso
	 */
	public boolean validarBonificador(Bonificador boni) {
		if (PApplet.dist(x.get(x.size() - 1), y.get(y.size() - 1), boni.getX(), boni.getY()) < tam) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validacion de contacto entre serpiente y ara�a
	 * 
	 * @param a ara�a
	 * @return verdadero si hay "contacto" con una ara�a, de lo contrario falso
	 */
	public boolean validarArana(Arana a) {
		if (PApplet.dist(x.get(x.size() - 1), y.get(y.size() - 1), a.getPos().x, a.getPos().y) < tam) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * movimiento cuerpo de la serpiente
	 */
	void moverSerpiente() {

		if (direction == right) {
			x.add(x.get(x.size() - 1) + tam);
			y.add(y.get(y.size() - 1));

			x.remove(0);
			y.remove(0);
		} else if (direction == down) {
			x.add(x.get(x.size() - 1));
			y.add(y.get(y.size() - 1) + tam);

			x.remove(0);
			y.remove(0);
		} else if (direction == left) {
			x.add(x.get(x.size() - 1) - tam);
			y.add(y.get(y.size() - 1));

			x.remove(0);
			y.remove(0);
		} else if (direction == up) {
			x.add(x.get(x.size() - 1));
			y.add(y.get(y.size() - 1) - tam);

			x.remove(0);
			y.remove(0);
		}
	}

	/**
	 * este metodo se encarga del crecimiento de la serpiente.
	 */
	void agregarCola() {
		n += 1;

		switch (direction) {
		case 1:
			x.add(x.get(x.size() - 1));
			y.add(y.get(y.size() - 1) - tam);
			n = x.size();
			break;

		case 2:
			x.add(x.get(x.size() - 1));
			y.add(y.get(y.size() - 1) + tam);
			n = x.size();
			break;

		case 3:
			x.add(x.get(x.size() - 1) - tam);
			y.add(y.get(y.size() - 1));
			n = x.size();
			break;

		case 4:
			x.add(x.get(x.size() - 1) + tam);
			y.add(y.get(y.size() - 1));
			n = x.size();
			break;
		}
	}

	void agregarColaRobo(Arana a) {
		n += 1;

		switch (direction) {
		case 1:
			x.add(a.getFresas(), x.get(x.size() - 1));
			y.add(a.getFresas(), y.get(y.size() - 1) - tam);
			n = x.size();
			break;

		case 2:
			x.add(a.getFresas(), x.get(x.size() - 1));
			y.add(a.getFresas(), y.get(y.size() - 1) + tam);
			n = x.size();
			break;

		case 3:
			x.add(a.getFresas(), x.get(x.size() - 1) - tam);
			y.add(a.getFresas(), y.get(y.size() - 1));
			n = x.size();
			break;

		case 4:
			x.add(a.getFresas(), x.get(x.size() - 1) + tam);
			y.add(a.getFresas(), y.get(y.size() - 1));
			n = x.size();
			break;
		}
	}

	/**
	 * cuando toma un bonificador malo pierde cuerpo.
	 */
	public void quitarCola() {
		if (x.size() >= 2) {
			n -= 1;
			x.remove(0);
			y.remove(0);
			n = x.size();
		}
	}

	public void quitarColaMitad() {
		if (x.size() >= 2) {
			n -= 1;
			for (int i = 0; i < x.size() / 2; i++) {
				for (int j = 0; j < y.size() / 2; j++) {
					x.remove(i);
					y.remove(j);
					n = x.size();
				}
			}
		}
	}

	public void quitarColaTodas() {
		if (x.size() > 1) {
			n -= 1;
			for (int i = 0; i < x.size(); i++) {
				for (int j = 0; j < y.size(); j++) {
					x.remove(i);
					y.remove(j);
					n = x.size();
				}
			}
		}
	}

	public void mover() {
		if (app.keyCode == app.UP)
			direction = up;
		if (app.keyCode == app.DOWN)
			direction = down;
		if (app.keyCode == app.LEFT)
			direction = left;
		if (app.keyCode == app.RIGHT)
			direction = right;

	}

	public LinkedList<Float> getX() {
		return x;
	}

	public int getN() {
		return n;
	}

	public float getTam() {
		return tam;
	}

	public LinkedList<Float> getY() {
		return y;
	}

}
