package cz.cvut.fel.prvniLejos;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Treti {

	private static LightSensor svetlo = new LightSensor(SensorPort.S3);
	private static int deska = 0, cara = 0;
	private static final int VYCHOZI_HODNOTA = 330; // 330

	public static void main(String[] args) {
		nastavHodnoty();

	}

	private static void nastavHodnoty() {
		System.out.println("Enter - Spustit \n" + "Levy - deska \n" + "Pravy - cara");
		while (!Button.ENTER.isDown()) {
			svetlo.setFloodlight(true);
			if (Button.LEFT.isDown()) {
				deska = svetlo.getLightValue();
				svetlo.calibrateHigh();
				System.out.println(deska);
			}
			if (Button.RIGHT.isDown()) {
				cara = svetlo.getLightValue();
				svetlo.calibrateLow();
				System.out.println(cara);
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		confirm(cara, deska);
	}

	private static void confirm(int cara, int deska) {
		LCD.clear();
		System.out.println("Deska: " + deska + "\n Cara: " + cara + "\n ENTER - potvrzeni \n ESCAPE - upravit");
		while (!Button.ENTER.isDown() && !Button.ESCAPE.isDown()) {

		}
		if (Button.ESCAPE.isDown())
			nastavHodnoty();
		if (Button.ENTER.isDown()) {
			sledujCaru();
		}

	}

	private static void sledujCaru() {
		Motor.B.forward();
		Motor.C.forward();
		int prumer = 50;
		double history = 0;
		int posledni = prumer;
		int nastavPropor = 0;
		final double KONST_I = 0.1D; // 0.1
		final double KONST_D = 1D; // 1
		final double KONST_PROP = 4.4D;// 4.4
		while (!Button.ESCAPE.isDown()) {
			posledni = nastavPropor;

			// proporcionální část
			// P
			int svetelnaHodnota = svetlo.getLightValue();
			if (Math.abs(svetelnaHodnota - prumer) < 2) {
				nastavPropor = 0;
			} else {
				nastavPropor = svetelnaHodnota - prumer;
			}
			// I
			history += nastavPropor;
			if (history > VYCHOZI_HODNOTA * 1.5D)
				history = VYCHOZI_HODNOTA * 1.5D;
			if (history < -VYCHOZI_HODNOTA * 1.5D)
				history = -VYCHOZI_HODNOTA * 1.5D;
			// D
			int derivace = nastavPropor - posledni;

			// Nastavení rychlosti motoru
			int nastav = (int) Math.round((nastavPropor * KONST_PROP) + history * KONST_I + derivace * KONST_D);
			int nastavB = nastav;
			int nastavA = nastav;
			if (nastav > VYCHOZI_HODNOTA -20)
				nastavA = VYCHOZI_HODNOTA - 20;
			if (nastav < -VYCHOZI_HODNOTA + 20)
				nastavB = -VYCHOZI_HODNOTA + 20;
			System.out.println(svetelnaHodnota);
			Motor.B.setSpeed(VYCHOZI_HODNOTA + nastavB);
			Motor.C.setSpeed(VYCHOZI_HODNOTA - nastavA);
		}
	}

}
