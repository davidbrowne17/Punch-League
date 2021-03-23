package net.davidbrowne.punchleague.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Boxing.Fighter;
import net.davidbrowne.punchleague.Boxing.Player;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Huds.FightHud;
import net.davidbrowne.punchleague.Models.GameEvent;
import net.davidbrowne.punchleague.Tools.B2WorldCreator;
import net.davidbrowne.punchleague.Tools.Controller;
import net.davidbrowne.punchleague.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private TextureAtlas atlas;
    private World world;
    private Viewport gamePort;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera gamecam;
    private Game game;
    private Player player;
    private B2WorldCreator creator;
    private Fighter fighter;
    private Controller controller;
    private Texture ring;
    private FightHud hud;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private boolean OVER=false;
    private AssetManager manager;
    static final float STEP_TIME = 1f/60f;
    float accumulator = 0;
    private HomeScreen homeScreen;
    public PlayScreen(Game game, AssetManager manager, int playerId, int fighterId,HomeScreen homeScreen) {
        this.homeScreen=homeScreen;
        this.game=game;
        this.manager = manager;
        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(Game.V_WIDTH/Game.PPM,Game.V_HEIGHT/Game.PPM,gamecam);
        gamecam.setToOrtho(false, Game.V_WIDTH/2,Game.V_HEIGHT/2);
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load("ring.tmx");
        atlas = new TextureAtlas("fighter.atlas");
        world = new World(new Vector2(0,0),true);
        renderer = new OrthogonalTiledMapRenderer(map,1);
        player = new Player(this,748*Game.PPM,913*Game.PPM,playerId);
        fighter = new Fighter(this,880*Game.PPM,890*Game.PPM,fighterId);
        hud = new FightHud(Game.batch,this);
        b2dr = new Box2DDebugRenderer();
        creator =new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        ring = new Texture("ring.png");
        controller = new Controller();
        game.getMusic().stop();
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void show() {

    }
    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, 6, 2);
        }
    }
    public void update(float dt){
        hud.update(dt);
        handleInput();
        stepWorld();
        player.update(dt);
        fighter.update(dt);
        gamecam.update();
        renderer.setView(gamecam);
        gamecam.position.set(800,899,0);
        checkResult();
        if(hud.getWorldTimer()<=0)
            checkTimeUpResults();
    }

    public void checkTimeUpResults(){

        if(fighter.getPunchesRecieved()<=player.getPunchesReceived()&!OVER){
            OVER=true;
            //lose
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {


            manager.get("sounds/bell.wav", Sound.class).play(game.getVolume());
            if(homeScreen.getFans()>=1000){
                homeScreen.setFans(homeScreen.getFans()-500);
                GameEvent gameEvent = new GameEvent(3,homeScreen.getMySkin());
                gameEvent.loadDialogs();
                gameEvent.getDialog().show(homeScreen.stage);
                gameEvent.getDialog().scaleBy(1f);
                gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);

            }
            if(homeScreen.getRank()<29)
                homeScreen.setRank(homeScreen.getRank()+1);
            else if(homeScreen.getRank()!=30)
                homeScreen.setRank(homeScreen.getRank()+1);
            GameEvent gameEvent = new GameEvent(6,homeScreen.getMySkin());
            gameEvent.loadDialogs();
            gameEvent.getDialog().show(homeScreen.stage);
            gameEvent.getDialog().scaleBy(1f);
            gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            homeScreen.setLosses(homeScreen.getLosses()+1);
            homeScreen.setNextOpponent(null);
            game.setScreen(homeScreen);
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {
                    game.getMusic().play();
                }
            }, 2f);
        }
    }, 2f);
        }

        else if(fighter.getPunchesRecieved()>player.getPunchesReceived()&!OVER){
            OVER=true;
            //win
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {


            manager.get("sounds/bell.wav", Sound.class).play(game.getVolume());
            if(homeScreen.getRank()!=1 && homeScreen.getRank()!=0){
                homeScreen.setRank(homeScreen.getRank()-1);
                GameEvent gameEvent = new GameEvent(4,homeScreen.getMySkin());
                gameEvent.loadDialogs();
                gameEvent.getDialog().show(homeScreen.stage);
                gameEvent.getDialog().scaleBy(1f);
                gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            }
            homeScreen.setFans(homeScreen.getFans()+1000);
            homeScreen.setWins(homeScreen.getWins()+1);
            GameEvent gameEvent = new GameEvent(2,homeScreen.getMySkin());
            int purse=134567;
            if(homeScreen.getRank()!=0)
                gameEvent.loadMoneyDialog((purse/homeScreen.getRank()));
            else
                gameEvent.loadMoneyDialog((purse*2));
            gameEvent.getDialog().show(homeScreen.stage);
            gameEvent.getDialog().scaleBy(1f);
            gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            if(homeScreen.getRank()!=0)
                homeScreen.setMoney(homeScreen.getMoney()+(purse/homeScreen.getRank()));
            else
                homeScreen.setMoney(homeScreen.getMoney()+(purse*2));
            GameEvent gameEvent2 = new GameEvent(2,homeScreen.getMySkin());
            gameEvent2.loadDialogs();
            gameEvent2.getDialog().show(homeScreen.stage);
            gameEvent2.getDialog().scaleBy(1f);
            gameEvent2.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent2.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            GameEvent gameEvent3 = new GameEvent(7,homeScreen.getMySkin());
            gameEvent3.loadDialogs();
            gameEvent3.getDialog().show(homeScreen.stage);
            gameEvent3.getDialog().scaleBy(1f);
            gameEvent3.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent3.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            homeScreen.setNextOpponent(null);
            game.setScreen(homeScreen);
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {
                    game.getMusic().play();
                }
            }, 2f);
        }
    }, 2f);
        }
    }

    public void checkResult(){
        if(player.getPunchesReceived()>=100&!OVER){
            OVER=true;
            //lose
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {


            manager.get("sounds/bell.wav", Sound.class).play(game.getVolume());
            if(homeScreen.getFans()>=1000){
                homeScreen.setFans(homeScreen.getFans()-500);
                GameEvent gameEvent = new GameEvent(3,homeScreen.getMySkin());
                gameEvent.loadDialogs();
                gameEvent.getDialog().show(homeScreen.stage);
                gameEvent.getDialog().scaleBy(1f);
                gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);

            }
            if(homeScreen.getRank()<29)
                homeScreen.setRank(homeScreen.getRank()+2);
            else if(homeScreen.getRank()!=30)
                homeScreen.setRank(homeScreen.getRank()+1);
            GameEvent gameEvent = new GameEvent(5,homeScreen.getMySkin());
            gameEvent.loadDialogs();
            gameEvent.getDialog().show(homeScreen.stage);
            gameEvent.getDialog().scaleBy(1f);
            gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            homeScreen.setLosses(homeScreen.getLosses()+1);
            homeScreen.setNextOpponent(null);
            game.setScreen(homeScreen);
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {
                    game.getMusic().play();
                }
            }, 2f);
                }
    }, 2f);
        }
        else if(fighter.getPunchesRecieved()>=100& !OVER){
            OVER=true;
            //win
            Timer timer = new Timer();
            Timer.Task task = timer.schedule(new Timer.Task() {
                @Override
                public void run () {


            manager.get("sounds/bell.wav", Sound.class).play(game.getVolume());
            if(homeScreen.getRank()!=0){
                homeScreen.setRank(homeScreen.getRank()-1);
                GameEvent gameEvent = new GameEvent(4,homeScreen.getMySkin());
                gameEvent.loadDialogs();
                gameEvent.getDialog().show(homeScreen.stage);
                gameEvent.getDialog().scaleBy(1f);
                gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            }
            homeScreen.setFans(homeScreen.getFans()+1000);
            homeScreen.setWins(homeScreen.getWins()+1);
            GameEvent gameEvent = new GameEvent(2,homeScreen.getMySkin());
            int purse=134567;
            if(homeScreen.getRank()!=0)
                gameEvent.loadMoneyDialog((purse/homeScreen.getRank()));
            else
                gameEvent.loadMoneyDialog((purse*2));
            gameEvent.getDialog().show(homeScreen.stage);
            gameEvent.getDialog().scaleBy(1f);
            gameEvent.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            if(homeScreen.getRank()!=0)
                homeScreen.setMoney(homeScreen.getMoney()+(purse/homeScreen.getRank()));
            else
                homeScreen.setMoney(homeScreen.getMoney()+(purse*2));
            GameEvent gameEvent2 = new GameEvent(2,homeScreen.getMySkin());
            gameEvent2.loadDialogs();
            gameEvent2.getDialog().show(homeScreen.stage);
            gameEvent2.getDialog().scaleBy(1f);
            gameEvent2.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent2.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            GameEvent gameEvent3 = new GameEvent(8,homeScreen.getMySkin());
            gameEvent3.loadDialogs();
            gameEvent3.getDialog().show(homeScreen.stage);
            gameEvent3.getDialog().scaleBy(1f);
            gameEvent3.getDialog().setPosition(homeScreen.getViewport().getWorldWidth()/2 -(gameEvent3.getDialog().getWidth()),homeScreen.getViewport().getWorldHeight()/2);
            homeScreen.setNextOpponent(null);
            game.setScreen(homeScreen);
            Timer timer1 = new Timer();
            Timer.Task task1 = timer1.schedule(new Timer.Task() {
                @Override
                public void run () {
                    game.getMusic().play();
                }
            }, 2f);}
    }, 2f);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        //clear screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        //game.batch.draw(ring,0,0,Game.V_WIDTH,(Game.V_HEIGHT-Game.V_HEIGHT/7));
        game.batch.end();
        game.batch.begin();
        game.batch.setProjectionMatrix(gamecam.combined);
        renderer.render();
        fighter.draw(game.batch);
        player.draw(game.batch);
        game.batch.end();
        hud.stage.draw();
        controller.draw();
        //box2d debug lines
        //b2dr.render(world,gamecam.combined);
        if(game.getScreen()!=this)
            dispose();
    }

    public AssetManager getManager() {
        return manager;
    }

    public Game getGame() {
        return game;
    }

    public void handleInput(){
        if(player.currentState!= Player.State.dead&&fighter.currentState!= Fighter.State.dead) {

            if (controller.getRightPressed() || (Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
                player.b2body.applyLinearImpulse(new Vector2(900f, player.b2body.getLinearVelocity().y), player.b2body.getWorldCenter(), true);
            else if (controller.getLeftPressed() || Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.b2body.applyLinearImpulse(new Vector2(-100f, player.b2body.getLinearVelocity().y), player.b2body.getWorldCenter(), true);
            else if (controller.getUpPressed() || Gdx.input.isKeyPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(player.b2body.getLinearVelocity().x, 100f), player.b2body.getWorldCenter(), true);
            else if (controller.getDownPressed() || Gdx.input.isKeyPressed(Input.Keys.DOWN))
                player.b2body.applyLinearImpulse(new Vector2(player.b2body.getLinearVelocity().x, -100f), player.b2body.getWorldCenter(), true);
            else if (!controller.getRightPressed() && !controller.getLeftPressed() && !controller.getUpPressed() && !controller.getDownPressed())
                player.b2body.setLinearVelocity(new Vector2(0, 0));
            if (controller.getaPressed() || Gdx.input.isKeyJustPressed(Input.Keys.A))
                player.punch(true);
            if (controller.getbPressed() || Gdx.input.isKeyJustPressed(Input.Keys.D))
                player.punch(false);

        }
    }

    public Player getPlayer() {
        return player;
    }

    public Fighter getFighter() {
        return fighter;
    }

    @Override
    public void resize(int width, int height) {
        controller.resize(width,height);

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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        atlas.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
