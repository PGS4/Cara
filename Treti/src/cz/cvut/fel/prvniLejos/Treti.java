package cz.cvut.fel.prvniLejos;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Treti {

	private static LightSensor svetlo = new LightSensor(SensorPort.S3);
	private static int deska = 0, cara = 0;
	private static final int KONST_PROP = 4;
	private static final int VYCHOZI_HODNOTA = 300;

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
		int prumer = (cara + deska) / 2;
		double history = 0;
		int posledni = prumer;
		int nastavPropor = 0;
		final double KONST_I = 0.15;
		final double KONST_D = 1;
		while (!Button.ESCAPE.isDown()) {
			posledni = nastavPropor; 
			
			//proporcionální část
			//P
			int svetelnaHodnota = svetlo.getLightValue();
			nastavPropor = svetelnaHodnota-prumer;
			
			//I
			history += nastavPropor*2;
			if(history > VYCHOZI_HODNOTA*1.5)
				history = VYCHOZI_HODNOTA*1.5;
			if(history < -VYCHOZI_HODNOTA*1.5)
				history = -VYCHOZI_HODNOTA * 1.5;
			
			//D
			int derivace = nastavPropor - posledni;
			
			//Nastavení rychlosti motoru                                      
			int nastav = (int) Math.round((nastavPropor*KONST_PROP) + history*KONST_I + derivace*KONST_D);
			/*if(nastav > VYCHOZI_HODNOTA)
				nastav = VYCHOZI_HODNOTA;
			if(nastav < -VYCHOZI_HODNOTA)
				nastav = -VYCHOZI_HODNOTA;
			*/
			System.out.println(nastav);
			Motor.B.setSpeed(VYCHOZI_HODNOTA + nastav);
			Motor.C.setSpeed(VYCHOZI_HODNOTA - nastav);
		}
	}

}
