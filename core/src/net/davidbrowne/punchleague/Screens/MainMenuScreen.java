package net.davidbrowne.punchleague.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Models.BoxerDetails;

public class MainMenuScreen implements Screen {
    private Game game;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    private Texture logo;
    public Stage stage;
    private Preferences prefs;
    private Skin mySkin;
    private static final int LOGO_HEIGHT= 200;
    private static final int LOGO_WIDTH= 600;
    public MainMenuScreen(final Game game, final AssetManager manager){
        logo = new Texture(Gdx.files.internal("logo.png"));
        this.game=game;
        this.manager=manager;
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        Gdx.input.setInputProcessor(stage);
        prefs = Gdx.app.getPreferences("MyPreferences");
        background=new Texture(Gdx.files.internal("bg.png"));
        stage = new Stage(viewport,game.batch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button = new TextButton("New Game",mySkin,"default");
        button.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //game.setScreen(new PlayScreen(game,manager,1,2));
                game.setScreen(new CreatePlayerScreen(game,manager));
                dispose();

                return true;
            }
        });
        // Text Button
        Button button1 = new TextButton("Continue Game",mySkin,"default");
        button1.setTransform(true);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, final int pointer, int button) {

                    if(!prefs.getString("name").isEmpty()){
                        String name = prefs.getString("name");
                        int id = prefs.getInteger("id");
                        String weight = prefs.getString("weight");
                        HomeScreen screen = new HomeScreen(game,manager,name,id,weight);
                        screen.setRank(prefs.getInteger("rank"));
                        screen.setWins(prefs.getInteger("wins"));
                        screen.setLosses(prefs.getInteger("losses"));
                        screen.setFans(prefs.getInteger("fans"));
                        screen.setMoney(prefs.getInteger("earnings"));
                        screen.setMonth(prefs.getInteger("month"));
                        screen.setYear(prefs.getInteger("year"));
                        BoxerDetails opponent;
                        for(int i=0;i<screen.getBoxersList().size();i++){
                            if(screen.getBoxersList().get(i).getId()==prefs.getInteger("nextOpponent")){
                                opponent=new BoxerDetails(screen.getBoxersList().get(i).getName(),screen.getBoxersList().get(i).getId());
                                screen.setNextOpponent(opponent);
                            }
                        }
                        game.setScreen(screen);
                        dispose();
                    }

                return true;
            }
        });
        Button button2 = new TextButton("Settings",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game,manager));
                dispose();
                return true;
            }
        });
        Button button3 = new TextButton("Controls",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ControlsScreen(game,manager));
                dispose();
                return true;
            }
        });

        Button button4 = new TextButton("Exit",mySkin,"default");
        button4.setTransform(true);
        button4.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        table.add(button).center().padTop(200);
        table.row();
        table.add(button1).center().padTop(25).expandX();
        table.row();
        table.add(button2).center().padTop(25).expandX();
        table.row();
        table.add(button3).center().padTop(25).expandX();
        table.row();
        table.add(button4).center().padTop(25).expandX();
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
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        game.batch.draw(logo,(viewport.getWorldWidth()/2 - LOGO_WIDTH/2),((viewport.getWorldHeight()-LOGO_HEIGHT*1.1f)),LOGO_WIDTH,LOGO_HEIGHT);
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
        logo.dispose();
        stage.dispose();
    }
}
