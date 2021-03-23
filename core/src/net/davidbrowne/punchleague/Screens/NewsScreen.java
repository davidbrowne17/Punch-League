package net.davidbrowne.punchleague.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Models.GameEvent;

public class NewsScreen implements Screen {
    private static final int PLAYER_WIDTH=300;
    private static final int PLAYER_HEIGHT=300;
    private Game game;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    private HomeScreen homeScreen;
    public Stage stage;
    private Array<String> labels;
    private TextureRegion playerImage;


    public NewsScreen(final Game game, final AssetManager manager, final HomeScreen homeScreen) {
        this.game = game;
        this.manager = manager;
        this.homeScreen=homeScreen;
        background=new Texture(Gdx.files.internal("bg5.png"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        playerImage = new TextureRegion(homeScreen.getAtlas().findRegion("player"+homeScreen.getPlayerId()));
        Array<String> labels=new Array<String>();
        for(int i=0;i<homeScreen.getBoxersList().size();i++){
            labels.add(homeScreen.getBoxersList().get(i).getName());
        }
        labels.insert(homeScreen.getRank(),homeScreen.getPlayerName());
        final SelectBox<String> selectBox=new SelectBox<String>(homeScreen.getMySkin().get(SelectBox.SelectBoxStyle.class));
        selectBox.setItems(labels);
        // Text Button
        Button button2;
        button2 = new TextButton("Return",homeScreen.getMySkin(),"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(homeScreen);
                return true;
            }
        });

        //table.add(selectBox).center().expandY();
        table.row();
        table.add(button2).center().padBottom(100).expandY().padRight(900);
        stage.addActor(table);
        GameEvent gameEvent = new GameEvent(1,homeScreen.getMySkin(),homeScreen);
        if(homeScreen.getRank()==30){
            gameEvent.loadNewsDialogs(1);
        }
        else if(homeScreen.getRank()>25){
            gameEvent.loadNewsDialogs(2);
        }
        else if(homeScreen.getRank()>=20){
            gameEvent.loadNewsDialogs(3);
        }
        else if(homeScreen.getRank()>=15){
            gameEvent.loadNewsDialogs(4);
        }
        else if(homeScreen.getRank()>=5){
            gameEvent.loadNewsDialogs(5);
        }

        else if(homeScreen.getRank()==0){
            gameEvent.loadNewsDialogs(6);
        }
        else{
            gameEvent.loadNewsDialogs(5);
        }

        gameEvent.getDialog().show(stage);
        gameEvent.getDialog().scaleBy(1f);
        gameEvent.getDialog().setPosition(viewport.getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),viewport.getWorldHeight()/2);
    }

    @Override
    public void show() {

    }

    public void update(float dt){
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
        Game.batch.draw(playerImage,(viewport.getWorldWidth()/1.06f- (PLAYER_WIDTH) ),((viewport.getWorldHeight()-viewport.getWorldHeight()/1.5f)),PLAYER_WIDTH,PLAYER_HEIGHT);
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
        background.dispose();
        stage.dispose();
    }
}
