package net.davidbrowne.punchleague.Huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Screens.PlayScreen;


public class FightHud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private int worldTimer;
    private float timeCount;
    private Label timeLabel,playerLabel,fighterLabel;
    private PlayScreen screen;
    private Skin mySkin;

    public FightHud(SpriteBatch sb, PlayScreen screen){
        this.screen=screen;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Bromine.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        worldTimer=100;
        viewport = new FillViewport(Game.V_WIDTH, Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Table table = new Table();
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        timeLabel = new Label(String.format("Time: %03d",worldTimer), mySkin);
        playerLabel = new Label(String.format("Score: %03d",screen.getFighter().getPunchesRecieved()), mySkin);
        fighterLabel = new Label(String.format("Opponent Score: %03d",screen.getPlayer().getPunchesReceived()),mySkin);
        table.top();
        table.setFillParent(true);
        table.add(timeLabel).expandX().padTop(50);
        table.add(playerLabel).expandX().padTop(50);
        table.add(fighterLabel).expandX().padTop(50);
        stage.addActor(table);
    }

    public int getWorldTimer() {
        return worldTimer;
    }

    public void update(float dt){
        timeCount +=dt;
        while(timeCount >= 1){
            if(worldTimer!=0)
                worldTimer--;
            timeCount -= 1;
        }
        playerLabel.setText(String.format("Score: %03d",screen.getFighter().getPunchesRecieved()));
        fighterLabel.setText(String.format("Opponent Score: %03d",screen.getPlayer().getPunchesReceived()));
        timeLabel.setText(String.format("Time: %03d", worldTimer));

    }

    @Override
    public void dispose() {

    }
}
