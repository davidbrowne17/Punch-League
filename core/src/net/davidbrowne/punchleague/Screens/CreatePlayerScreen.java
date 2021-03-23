package net.davidbrowne.punchleague.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;

public class CreatePlayerScreen implements Screen {
    private static final int PLAYER_WIDTH=300;
    private static final int PLAYER_HEIGHT=300;
    private Game game;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    public Stage stage;
    private Skin mySkin;
    private int playerId=1;
    private TextureRegion playerImage;
    private TextureAtlas atlas;

    public CreatePlayerScreen(final Game game, final AssetManager manager) {
        this.game=game;
        background=new Texture(Gdx.files.internal("bg2.png"));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("fighter.atlas");
        playerImage = new TextureRegion(atlas.findRegion("player"+playerId));
        stage = new Stage(viewport,game.batch);
        Label label = new Label("CREATE YOUR FIGHTER", mySkin);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button = new TextButton("Change Appearance",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(playerId<=3)
                    playerId++;
                else
                    playerId=1;

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        final TextField usernameTextField = new TextField("", mySkin);
        final SelectBox<String> selectBox=new SelectBox<String>(mySkin.get(SelectBox.SelectBoxStyle.class));
        selectBox.setItems("Flyweight","Bantamweight","Featherweight","Lightweight","Welterweight","Middleweight","Cruiserweight","Light Heavyweight","Heavyweight");

        // Text Button
        Button button2 = new TextButton("Continue",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(usernameTextField.getText()!=null&&!usernameTextField.getText().equalsIgnoreCase("")){
                    //setup home screen
                    game.setScreen(new HomeScreen(game,manager,usernameTextField.getText(),playerId,selectBox.getSelected().toString()));
                    dispose();
                }
                Dialog dialog = new Dialog("", mySkin);
                dialog.text("Please enter a name for your fighter");
                dialog.button("OK");
                dialog.show(stage);
                dialog.scaleBy(1f);
                dialog.setPosition(viewport.getWorldWidth()/2 -(dialog.getWidth()),viewport.getWorldHeight()/2);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        Label label2 = new Label("ENTER NAME", mySkin);
        table.add(label).center().padTop(250);
        table.row();
        table.add(button).center().padTop(0);
        table.row();
        table.add(selectBox).center().padTop(50);
        table.row();
        table.add(label2).center().padTop(25);
        table.row();
        table.add(usernameTextField).center().padTop(25);
        table.row();
        table.add(button2).center().padTop(50);
        stage.addActor(table);

    }

    @Override
    public void show() {

    }
    public void update(float dt){
        playerImage = new TextureRegion(atlas.findRegion("player"+playerId));
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        Game.batch.draw(playerImage,(viewport.getWorldWidth()/2- PLAYER_WIDTH/2 ),((viewport.getWorldHeight()-PLAYER_HEIGHT)),PLAYER_WIDTH,PLAYER_HEIGHT);
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
        atlas.dispose();
        background.dispose();
        stage.dispose();

    }
}
