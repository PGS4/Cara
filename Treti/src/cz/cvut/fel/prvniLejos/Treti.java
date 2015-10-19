package cz.cvut.fel.prvniLejos;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Treti {

	private static LightSensor svetlo = new LightSensor(SensorPort.S3);
	private static int deska = 0, cara = 0;
	private static final int KONST_PROP = 4;
	private static final int VYCHOZI_HODNOTA = 250;

	public static void main(String[] args) {
		nastavHodnoty();
		sledujCaru();
	}

	private static void nastavHodnoty() {
		System.out.println("Enter - Spustit \n" + "Levy - deska \n"
				+ "Pravy - cara");
		while (!Button.ENTER.isDown()) {
			svetlo.setFloodlight(true);
			if (Button.LEFT.isDown()) {
				deska = svetlo.getLightValue();
				System.out.println(deska);
			}
			if (Button.RIGHT.isDown()) {
				cara = svetlo.getLightValue();
				System.out.println(cara);
			}
		}
	}

	private static void sledujCaru() {
		Motor.B.forward();
		Motor.C.forward();
		int history = 0;
		final double KONST_I = 0.25;
		while (!Button.ESCAPE.isDown()) {
			
			//proporcionální část
			int prumer = (cara + deska) / 2;
			int svetelnaHodnota = svetlo.getLightValue();
			int nastavPropor = prumer-svetelnaHodnota;
			
			//I
			history += nastavPropor*2;
			if(history > VYCHOZI_HODNOTA)
				history = VYCHOZI_HODNOTA;
			if(history < -VYCHOZI_HODNOTA)
				history = -VYCHOZI_HODNOTA;
			
			//Nastavení rychlosti motoru                                      
			int nastav = (int) Math.round((nastavPropor*KONST_PROP)+history*KONST_I);
			if(nastav > VYCHOZI_HODNOTA)
				nastav = VYCHOZI_HODNOTA;
			if(nastav < -VYCHOZI_HODNOTA)
				nastav = -VYCHOZI_HODNOTA;
			
			System.out.println(nastav);
			Motor.B.setSpeed(VYCHOZI_HODNOTA + nastav);
			Motor.C.setSpeed(VYCHOZI_HODNOTA - nastav);
		}
	}

}
