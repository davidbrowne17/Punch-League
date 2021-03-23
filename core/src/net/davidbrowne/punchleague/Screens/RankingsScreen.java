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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;


public class RankingsScreen implements Screen {
    private static final int PLAYER_WIDTH=300;
    private static final int PLAYER_HEIGHT=300;
    private Game game;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background,belt;
    private HomeScreen homeScreen;
    public Stage stage;
    private Array<String> labels;
    private boolean champ=false;
    private TextureRegion playerImage;


    public RankingsScreen(final Game game, final AssetManager manager, final HomeScreen homeScreen) {
        this.game = game;
        this.manager = manager;
        this.homeScreen=homeScreen;
        background=new Texture(Gdx.files.internal("bg4.png"));
        belt=new Texture(Gdx.files.internal("belt.png"));
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

        final Table scrollTable = new Table(homeScreen.getMySkin());
        for(int i=0;i<labels.size;i++){
            // Text Button
            Button button;
            if(i==0)
                button = new TextButton("C. "+labels.get(i),homeScreen.getMySkin(),"default");
            else
                button = new TextButton(i+". "+labels.get(i),homeScreen.getMySkin(),"default");
            button.setTransform(true);
            int id =homeScreen.getPlayerId();
            if(i == homeScreen.getRank())
                id =homeScreen.getPlayerId();
            for(int z=0;z<homeScreen.getBoxersList().size();z++) {
                if (homeScreen.getBoxersList().get(z).getName().matches(labels.get(i)))
                    id = homeScreen.getBoxersList().get(z).getId();
            }
            final int finalId = id;
            final int finalI = i;
            button.addListener(new InputListener(){
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(finalI ==0)
                        champ=true;
                    else
                        champ=false;
                    if(finalI != homeScreen.getRank())
                        playerImage = new TextureRegion(homeScreen.getAtlas().findRegion("fighter"+ finalId));
                    else
                        playerImage = new TextureRegion(homeScreen.getAtlas().findRegion("player"+ finalId));
                    return true;
                }
            });
            scrollTable.add(button);
            scrollTable.row();
        }
        final ScrollPane scroller = new ScrollPane(scrollTable,homeScreen.getMySkin());
        scroller.setForceScroll(false,true);
        final Table table1 = new Table(homeScreen.getMySkin());
        table1.setFillParent(true);
        table1.add(scroller).fill().expand();
        this.stage.addActor(table1);
        table.row();
        table.add(button2).center().padBottom(100).expandY().padRight(900);
        stage.addActor(table);
        if(homeScreen.getRank()==0)
            champ=true;
        else
            champ=false;
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
        if(champ)
            Game.batch.draw(belt,(viewport.getWorldWidth()/1.06f- (PLAYER_WIDTH) ),((viewport.getWorldHeight()-viewport.getWorldHeight()/3.5f)),PLAYER_WIDTH,PLAYER_HEIGHT/2);
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
