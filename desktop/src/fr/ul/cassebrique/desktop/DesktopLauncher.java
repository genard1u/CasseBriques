package fr.ul.cassebrique.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.ul.cassebrique.CasseBrique;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1150;
		config.height = 700;
		config.a = 8;

		new LwjglApplication(new CasseBrique(), config);
	}
}
