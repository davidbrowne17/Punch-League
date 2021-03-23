package net.davidbrowne.punchleague.Screens;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.punchleague.Game;

public class ControlsScreen implements Screen {
    private Game game;
    private AssetManager manager;
    private Texture background;
    public Stage stage;
    private Viewport viewport;
    private Skin mySkin;
    private Label controlsPC, controlsAndroid;
    public ControlsScreen(final Game game, final AssetManager manager) {
        this.game=game;
        this.manager=manager;
        background=new Texture(Gdx.files.internal("bg.png"));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        controlsPC = new Label(String.format("ARROW KEYS TO MOVE BOXER! USE A AND D TO PUNCH!"), mySkin);

        controlsAndroid = new Label(String.format("ARROW BUTTONS TO MOVE BOXER! USE A AND B TO PUNCH!"), mySkin);
        Label rulesLabel = new Label(String.format("LAND 100 PUNCHES TO WIN!"), mySkin);
        Label endLabel = new Label(String.format("IF TIME RUNS OUT THE FIGHTER WITH THE MOST LANDED PUNCHES WINS!"), mySkin);



        Button button = new TextButton("Main Menu",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game,manager));
                dispose();
                return true;
            }
        });
        if(Gdx.app.getType()!= Application.ApplicationType.Android){
            table.add(controlsPC).center().expandY().padTop(230);
        }else{
            table.add(controlsAndroid).center().expandY().padTop(230);
        }
        table.row();
        table.add(rulesLabel).center().expandY();
        table.row();
        table.add(endLabel).center().expandY();
        table.row();
        table.add(button).center().padTop(100).expandY();
        stage.addActor(table);

    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, Game.V_WIDTH,game.V_HEIGHT);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
