package brayan_valeria_taller2;

import processing.core.PApplet;
import processing.core.PImage;

public class Logica {
	private PApplet app;
	private Mundo mundo;
	private int pantalla;

	private PImage fondo1, fondo2, inicio, iniciarJuego, irInstruc, instrucc, juego, ganar, perder, irInicio;
	private int contador, tiempo;

	public Logica(PApplet app) {
		this.app = app;
		mundo = new Mundo(app);
		// carga de imagenes de interfaz
		fondo1 = app.loadImage("data/fondo arena.png");
		fondo2 = app.loadImage("data/fondo bosque.png");
		inicio = app.loadImage("data/inicio.jpg");
		iniciarJuego = app.loadImage("data/iniciarjuego.png");
		irInstruc = app.loadImage("data/irInstrucc.png");
		instrucc = app.loadImage("data/instrucciones.png");
		juego = app.loadImage("data/juego.png");
		ganar = app.loadImage("data/ganaste.png");
		perder = app.loadImage("data/perdiste.png");
		irInicio= app.loadImage("data/irInicio.png");
		contador = 90;
		pantalla = 1;

	}

	public void pintar() {

		switch (pantalla) {
		case 1:
			app.image(inicio, 0, 0);
			if (app.mouseX > 450 && app.mouseX < 700 && app.mouseY > 400 && app.mouseY < 440) {
				app.image(iniciarJuego, 0, 0);
			}
			if (app.mouseX > 450 && app.mouseX < 720 && app.mouseY > 480 && app.mouseY < 520) {
				app.image(irInstruc, 0, 0);
			}
			break;
		case 2:
			app.image(instrucc, 0, 0);

			break;
		case 3:
			app.image(fondo1, 0, 0);
			mundo.pintar();
			app.image(fondo2, 0, 0);

			app.image(juego, 0, 0);

			app.fill(0);
			app.textSize(30);
			app.text(mundo.getContadorFresa(), 1126, 52);

			tiempo = contador - (app.millis() / 1000);
			app.text(tiempo, 972, 52);

			if (tiempo == 0) {
				if (mundo.ganar() == true) {
					pantalla = 4;
				}

				if (mundo.ganar() == false) {
					pantalla = 5;
				}
			}

			break;

		case 4:

			app.image(ganar, 0, 0);
			if (app.mouseX > 564 && app.mouseX < 662 && app.mouseY > 295 && app.mouseY < 335) {
				app.image(irInicio, 0, 0);
			}
			break;

		case 5:

			app.image(perder, 0, 0);
			if (app.mouseX > 450 && app.mouseX < 720 && app.mouseY > 480 && app.mouseY < 520) {
				app.image(irInicio, 0, 0);
			}
			break;
		}

	}

	public void tecla() {
		mundo.serpienteTecla();
	}

	public void mouse() {
		if (pantalla == 1) {
			if (app.mouseX > 450 && app.mouseX < 700 && app.mouseY > 400 && app.mouseY < 440) {
				pantalla = 3;
			}
			if (app.mouseX > 450 && app.mouseX < 720 && app.mouseY > 480 && app.mouseY < 520) {
				pantalla = 2;

			}
		}
		if (pantalla == 2) {
			if (app.mouseX > 1116 && app.mouseX < 1150 && app.mouseY > 6 && app.mouseY < 42) {
				pantalla = 1;

			}
		}
		
		if(pantalla==4) {
			if (app.mouseX > 450 && app.mouseX < 720 && app.mouseY > 480 && app.mouseY < 520) {
				pantalla=1;
			}
		}
		
		if(pantalla==5) {
			if (app.mouseX > 450 && app.mouseX < 720 && app.mouseY > 480 && app.mouseY < 520) {
				pantalla=1;
			}
		}

	}
}