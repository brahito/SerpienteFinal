package brayan_valeria_taller2;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;

public class Mundo {
	private PApplet app;

	private ArrayList<Arana> aranas;
	private LinkedList<Bonificador> bonificadores;
	private int contadorFresas, generarBonificador;
	private Serpiente serpiente;
	private Recurso fresa;

	/**
	 * Inicializar mundo
	 * 
	 * @param app - PApplet
	 */

	public Mundo(PApplet app) {
		this.app = app;
		fresa = new Recurso(app);
		aranas = new ArrayList<Arana>();
		bonificadores = new LinkedList<Bonificador>();

		serpiente = new Serpiente(app, this);

		for (int i = 0; i < 3; i++) {
			Arana a = new AranaA(app, this);
			aranas.add(a);
			a.start();
		}
		for (int i = 0; i < 3; i++) {
			Arana a = new AranaR(app, this);
			aranas.add(a);
			a.start();
		}

		serpiente.start();

		contadorFresas = 0;
		for (int i = 0; i < bonificadores.size(); i++) {

			if (bonificadores.get(i) instanceof Hielo) {
				Hielo h = (Hielo) bonificadores.get(i);
				h.start();
			}
			if (bonificadores.get(i) instanceof Hongo) {
				Hongo h = (Hongo) bonificadores.get(i);
				h.start();
			}
		}

	}

	/**
	 * Pintar los enemigos(arañas), los bonificadores
	 */
	public void pintar() {
		fresa.pintar();
		serpiente.pintar();

		generarBonificador++;
		for (int i = 0; i < aranas.size(); i++) {
			aranas.get(i).pintar();
		}
		if (generarBonificador >= 400) {
			int generar = (int) app.random(1, 5);
			switch (generar) {
			case 1:
				bonificadores.add(new Cafe(app));
				break;
			case 2:
				bonificadores.add(new Hielo(app));

				break;
			case 3:
				bonificadores.add(new Dientes(app));
				break;
			case 4:
				bonificadores.add(new Hongo(app));
				break;
			}
			generarBonificador = 0;
		}
		for (int i = 0; i < bonificadores.size(); i++) {
			bonificadores.get(i).pintar();

			if (bonificadores.get(i) instanceof Hielo) {
				Hielo h = (Hielo) bonificadores.get(i);
				h.start();
			}
			if (bonificadores.get(i) instanceof Hongo) {
				Hongo h = (Hongo) bonificadores.get(i);
				h.start();
			}
		}

	}

	public LinkedList<Bonificador> getBonificadores() {
		return bonificadores;
	}

	public Serpiente getSer() {
		return serpiente;
	}

	public void serpienteTecla() {
		serpiente.mover();
	}

	public Recurso getRecurso() {
		return fresa;
	}

	public ArrayList<Arana> getAranas() {
		return aranas;
	}

	public void setRecurso(Recurso fresa) {
		this.fresa = fresa;
	}

	/**
	 * suma el numero de fresas de la serpiente
	 * 
	 * @param a - la cantidad que sumaran las fresas
	 */
	public void masContadorFresa(int a) {
		contadorFresas += a;
	}

	/**
	 * resta el numero de fresas de la serpiente
	 * 
	 * @param a - la cantidad que restaran las fresas
	 */
	public void BajarContadorFresa(int a) {
		contadorFresas -= a;
	}

	public int getContadorFresa() {
		return contadorFresas;
	}

	/**
	 * si el numero de fresas de la serpiente es mayor a la de las serpientes, gano,
	 * de lo contrario pierde.
	 * 
	 * @return gano- si gano o no el jugador
	 */
	public boolean ganar() {
		boolean gano = false;
		for (int i = 0; i < aranas.size(); i++) {

			if (contadorFresas > aranas.get(i).getFresas()) {
				gano = true;
			} else if (contadorFresas < aranas.get(i).getFresas()) {
				gano = false;
			}

		}
		return gano;
	}

}
