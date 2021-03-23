package net.davidbrowne.punchleague.desktop;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.davidbrowne.punchleague.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.V_WIDTH;
		config.height = Game.V_HEIGHT;
		config.fullscreen=false;
		config.resizable=true;
		config.title = "Punch League";
		config.addIcon("PunchLeagueIcon.png", Files.FileType.Internal);
		new LwjglApplication(new Game(), config);
	}
}
