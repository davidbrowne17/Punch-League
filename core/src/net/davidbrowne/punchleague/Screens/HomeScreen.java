package net.davidbrowne.punchleague.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Models.BoxerDetails;
import net.davidbrowne.punchleague.Models.GameEvent;

import java.util.ArrayList;
import java.util.Random;

public class HomeScreen implements Screen {
    private static final int PLAYER_WIDTH=300;
    private static final int PLAYER_HEIGHT=300;
    private String playerName;
    private String weightclass;
    private int wins=0;
    private int losses = 0;
    private Preferences prefs;
    private Game game;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background,belt;
    public Stage stage;
    private Skin mySkin;
    private int playerId;
    private int rank=30;
    private int money=0;
    private int year=2020;
    private int month=1;
    private int fans=0;
    private Label nameLabel;
    private Label rankLabel;
    private Label weightLabel;
    private Label winsLabel;
    private Label dateLabel;
    private Label moneyLabel;
    private Label fanLabel;
    private Label opponentLabel;
    private Dialog dialog;
    private BoxerDetails nextOpponent;
    private ArrayList<BoxerDetails> boxersList;
    private TextureRegion playerImage;
    private TextureRegion opponentImage;
    private TextureAtlas atlas;

    public HomeScreen(final Game game, final AssetManager manager, String playerName, final int playerId,
                      String weightclass) {
        this.game=game;
        this.manager=manager;
        this.playerName=playerName;
        this.playerId=playerId;
        this.weightclass=weightclass;
        belt=new Texture(Gdx.files.internal("belt.png"));
        prefs = Gdx.app.getPreferences("MyPreferences");
        background=new Texture(Gdx.files.internal("bg3.png"));
        atlas = new TextureAtlas("fighter.atlas");
        playerImage = new TextureRegion(atlas.findRegion("player"+playerId));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        nameLabel = new Label(playerName, mySkin);
        Group group = new Group();
        group.addActor(nameLabel);
        group.setScale(1f, 1f);
        winsLabel = new Label("Record: "+wins+"-"+losses , mySkin);
        Group recordGroup = new Group();
        recordGroup.addActor(winsLabel);
        recordGroup.setScale(1f, 1f);
        rankLabel = new Label("Ranking: "+rank , mySkin);
        Group rankGroup = new Group();
        rankGroup.addActor(rankLabel);
        rankGroup.setScale(1f, 1f);
        weightLabel = new Label("Weightclass: "+weightclass, mySkin);
        Group weightGroup = new Group();
        weightGroup.addActor(weightLabel);
        weightGroup.setScale(1f, 1f);
        moneyLabel = new Label("Earnings: "+money, mySkin);
        Group moneyGroup = new Group();
        moneyGroup.addActor(moneyLabel);
        moneyGroup.setScale(1f, 1f);
        fanLabel = new Label("Fans: "+fans, mySkin);
        Group fanGroup = new Group();
        fanGroup.addActor(fanLabel);
        fanGroup.setScale(1f, 1f);
        dateLabel = new Label(year+" Month: "+month, mySkin);
        Group dateGroup = new Group();
        dateGroup.addActor(dateLabel);
        dateGroup.setScale(1f, 1f);
        Json json = new Json();
        boxersList = json.fromJson(ArrayList.class, BoxerDetails.class, Gdx.files.internal("opponents.json"));
        Table table = new Table();
        table.top();
        table.padTop(50);
        table.setFillParent(true);
        final Random rand = new Random();
        // Text Button
        Button button;
            button = new TextButton("Advance",mySkin,"default");
            button.setTransform(true);
            button.addListener(new InputListener(){
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if((month==2| month==11 | month==6)&&nextOpponent==null){
                        int id=30;
                        if(rank==0){
                            id=rand.nextInt(30);
                            if(id==0)
                                id++;
                        }
                        else if(rank==1)
                            id=0;
                        else
                            id=rank-1;
                        GameEvent gameEvent = new GameEvent(1,mySkin,(HomeScreen) game.getScreen());
                        gameEvent.loadFightOffer(id);
                        gameEvent.getFightOffer().show(stage);
                        gameEvent.getFightOffer().scaleBy(1f);
                        gameEvent.getFightOffer().setPosition(viewport.getWorldWidth()/2 -(gameEvent.getFightOffer().getWidth()),viewport.getWorldHeight()/2);
                    }
                    if((month==5 | month == 10 | month == 1)&&nextOpponent!=null){
                        game.setScreen(new PlayScreen(game,manager,playerId,nextOpponent.getId(),(HomeScreen)game.getScreen()));
                        month++;
                    }
                    else if(month!=12){
                        month++;
                    }
                    else{
                        month=1;
                        year++;
                    }
                    if((month==5 | month == 10 | month == 1) && nextOpponent!=null){
                        GameEvent gameEvent = new GameEvent(1,mySkin);
                        gameEvent.loadDialogs();
                        gameEvent.getDialog().show(stage);
                        gameEvent.getDialog().scaleBy(1f);
                        gameEvent.getDialog().setPosition(viewport.getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),viewport.getWorldHeight()/2);
                    }
                    return true;
                }
            });
        // Text Button
        Button button2;
        button2 = new TextButton("Rankings",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new RankingsScreen(game,manager,(HomeScreen) game.getScreen()));
                return true;
            }
        });
        // Text Button
        Button button3;
        button3 = new TextButton("News",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new NewsScreen(game,manager, (HomeScreen) game.getScreen()));
                return true;
            }
        });
        // Text Button
        Button button4;
        button4 = new TextButton("Save",mySkin,"default");
        button4.setTransform(true);
        button4.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                saveInfo();
                return true;
            }
        });
        Button button5 = new TextButton("Exit",mySkin,"default");
        button5.setTransform(true);
        button5.addListener(new InputListener(){
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
        opponentLabel = new Label("Next Opponent: " , mySkin);
        Group opponentGroup = new Group();
        group.addActor(opponentLabel);
        group.setScale(1f, 1f);

        table.add(group).expandX();
        table.add(recordGroup).expandX();
        table.add(rankGroup).expandX();
        table.add(dateGroup).expandX();
        table.add(weightGroup).expandX().padRight(300f);
        table.row();
        table.add(moneyGroup).padTop(50).expandX();
        table.add(fanGroup).padTop(50).expandX();
        Table table3 = new Table();
        table3.top();
        table3.setFillParent(true);
        table3.add(opponentLabel).expandX().right().padRight(100f).padTop(200f);
        Table table2 = new Table();
        table2.top();
        table2.setFillParent(true);
        table2.add(button).center().padTop(100);
        table2.row();
        table2.add(button2).center().padTop(50);
        table2.row();
        table2.add(button3).center().padTop(50);
        table2.row();
        table2.add(button4).center().padTop(50);
        table2.row();
        table2.add(button5).center().padTop(50);
        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);

    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getWeightclass() {
        return weightclass;
    }

    public void setWeightclass(String weightclass) {
        this.weightclass = weightclass;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoxerDetails getNextOpponent() {
        return nextOpponent;
    }

    public void setNextOpponent(BoxerDetails nextOpponent) {
        this.nextOpponent = nextOpponent;
    }

    public ArrayList<BoxerDetails> getBoxersList() {
        return boxersList;
    }

    public void setBoxersList(ArrayList<BoxerDetails> boxersList) {
        this.boxersList = boxersList;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Skin getMySkin() {
        return mySkin;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public Stage getStage() {
        return stage;
    }

    public void saveInfo(){
        prefs.putString("name",playerName);
        prefs.putInteger("id",playerId);
        prefs.putString("weight",weightclass);
        prefs.putInteger("wins",wins);
        prefs.putInteger("losses",losses);
        prefs.putInteger("fans",fans);
        prefs.putInteger("rank",rank);
        prefs.putInteger("earnings",money);
        prefs.putInteger("year",year);
        prefs.putInteger("month",month);
        if(nextOpponent!=null)
            prefs.putInteger("nextOpponent",nextOpponent.getId());
        else
            prefs.remove("nextOpponent");
        prefs.flush();
        GameEvent gameEvent = new GameEvent(1,mySkin);
        gameEvent.loadSaveDialog();
        gameEvent.getDialog().show(stage);
        gameEvent.getDialog().scaleBy(1f);
        gameEvent.getDialog().setPosition(viewport.getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),viewport.getWorldHeight()/2);
    }

    public void update(float dt){

        stage.act(dt);
        nameLabel.setText(playerName);
        if(rank!=0)
            rankLabel.setText("Ranking: "+rank);
        else
            rankLabel.setText("Ranking: Champion");
        moneyLabel.setText("Earnings: "+money);
        winsLabel.setText("Record: "+wins+"-"+losses );
        weightLabel.setText("Weightclass: "+weightclass);
        dateLabel.setText(year+" Month: "+month);
        fanLabel.setText("Fans: "+fans);
        if(nextOpponent!=null){
            opponentImage = new TextureRegion(atlas.findRegion("fighter"+nextOpponent.getId()));
            opponentLabel.setText("Next opponent: "+nextOpponent.getName());
        }
        else{
            opponentLabel.setText("Next opponent: NONE BOOKED");
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        Game.batch.draw(playerImage,(viewport.getWorldWidth()/6- PLAYER_WIDTH/2 ),((viewport.getWorldHeight()-viewport.getWorldHeight()/1.5f)),PLAYER_WIDTH,PLAYER_HEIGHT);
        if(nextOpponent!=null)
            Game.batch.draw(opponentImage,(viewport.getWorldWidth()/1.06f- (PLAYER_WIDTH) ),((viewport.getWorldHeight()-viewport.getWorldHeight()/1.5f)),PLAYER_WIDTH,PLAYER_HEIGHT);
        if(rank==0)
            Game.batch.draw(belt,(viewport.getWorldWidth()/3.90f- (PLAYER_WIDTH) ),((viewport.getWorldHeight()-viewport.getWorldHeight()/3.5f)),PLAYER_WIDTH,PLAYER_HEIGHT/2);
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
