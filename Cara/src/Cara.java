import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Cara {
	public static void main(String[] args) {
		LightSensor svetlo = new LightSensor(SensorPort.S1);
		int deska = 0;
		int cara = 0;
		int aktual = 0;
		int history = 0;
		long nastav;
		int kp = 4;
		double ki = 0.2;
		double kd = 1;
		int klimit = 220;
		int klnastav = 350;
		int posledni = 0;
		int normovana_hodnota=0;
		int k_senzor=2;
		
		System.out.println("Enter - Spustit \n" + "Levy - deska \n"
				+ "Pravy - cara");
		while (!Button.ENTER.isDown()) {
			if (Button.LEFT.isDown()) {
				deska = svetlo.getLightValue();
				System.out.println(deska);
			}
			if (Button.RIGHT.isDown()) {
				cara = svetlo.getLightValue();
				System.out.println(cara);
			}

		}
		int prumer = (deska + cara) / 2;
		while (!Button.ESCAPE.isDown()) {
			posledni = normovana_hodnota;
			
			aktual = svetlo.getLightValue();
			normovana_hodnota = (prumer - aktual) * k_senzor;
			
						
			int derivace = normovana_hodnota - posledni;
			
			history+=normovana_hodnota;
			
			if (history > klimit) {
				history = klimit;
			}
			nastav = Math.round(normovana_hodnota*kp + history*ki + derivace*kd);
			if(nastav > klnastav){
				nastav = klnastav;
			}
			System.out.println(nastav);
			Motor.B.forward();
			Motor.C.forward();
			Motor.C.setSpeed(350 - (nastav));
			Motor.B.setSpeed(350 + (nastav));
		}
	}
}
