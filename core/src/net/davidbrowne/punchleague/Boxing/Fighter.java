package net.davidbrowne.punchleague.Boxing;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Screens.PlayScreen;
import java.util.Random;

public class Fighter extends Boxer {
    public enum State {standing,punchingRight,punchingLeft,dead}
    public State currentState,previousState;
    private float stateTimer=0;

    private Animation<TextureRegion> stand;
    private int stamina=100;
    private float spawnX,spawnY;
    private Animation<TextureRegion> fighterRun;
    private PlayScreen screen;
    private Body b2body;
    private int spriteId;
    private int punchesRecieved=0;
    public Fighter(PlayScreen screen,int spawnX,int spawnY,int spriteid) {
        currentState= State.standing;
        this.screen=screen;
        spriteId=spriteid;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("fighter"+spriteId));
        frames.add(screen.getAtlas().findRegion("fighter"+spriteId+"_walk"));
        frames.add(screen.getAtlas().findRegion("fighter"+spriteId+"_walk2"));
        fighterRun = new Animation(0.1f,frames);
        this.spawnX=spawnX;
        this.spawnY=spawnY;
        stand = new Animation(0.1f,frames);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("fighter"+spriteId)));
        setBounds(spawnX/ Game.PPM,spawnY/Game.PPM,64 ,64);
        definePlayer();
        Timer timer = new Timer();
        Timer.Task task = timer.schedule(new Timer.Task() {
            @Override
            public void run () {
                if(stamina<100)
                    stamina+=5;
            }
        }, 1.5f, 1);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    public int getPunchesRecieved() {
        return punchesRecieved;
    }

    public void addPunchesRecieved() {
        punchesRecieved++;
        screen.getManager().get("sounds/punch.wav", Sound.class).play(screen.getGame().getVolume());
    }

    public void updateSprite(float dt){
        setRotation(bearing(getX(),getY(),screen.getPlayer().getX(),screen.getPlayer().getY()));
        switch (currentState){
            case standing:
                setRegion(fighterRun.getKeyFrame(stateTimer,true));
                break;
            case punchingLeft:
                setRegion(screen.getAtlas().findRegion("fighter"+spriteId+"_left_hook"));
                break;
            case punchingRight:
                setRegion(screen.getAtlas().findRegion("fighter"+spriteId+"_right_hook"));
                break;
            case dead:
                setRegion(screen.getAtlas().findRegion("fighter"+spriteId+"_dead"));
                break;
        }
        if(getPunchesRecieved()>=100)
            currentState = State.dead;
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
    }
    public void update(float dt){
        updateSprite(dt);
        setPosition(b2body.getPosition().x  - getWidth()/2 ,b2body.getPosition().y - getHeight()/2 );
        if(screen.getPlayer().currentState!= Player.State.dead&&currentState!= Fighter.State.dead)
            AI();
        else{
            b2body.setLinearVelocity(0,0);
        }
    }

    public void punch(Player player){
        Random rand = new Random();
        if(rand.nextBoolean()==true)
            currentState = State.punchingRight;
        else{
            currentState = State.punchingLeft;
        }
        Timer timer = new Timer();
        Timer.Task task = timer.schedule(new Timer.Task() {
            @Override
            public void run () {
                currentState = State.standing;
            }
        }, 0.2f);
        if(stamina>70){
        boolean left = rand.nextBoolean();
        if(getPunchesRecieved()!=100)
            player.addPunchesReceived();
        stamina-=5;
        }
    }

    @Override
    public void setCanPunch(boolean canPunch) {

    }

    public void AI(){
        if(stamina>70){
            if(screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x){
                b2body.setLinearVelocity(50,b2body.getLinearVelocity().y);
            }
            else if(screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x){
                b2body.setLinearVelocity(-50,b2body.getLinearVelocity().y);
            }
            if(screen.getPlayer().b2body.getPosition().y > b2body.getPosition().y){
                b2body.setLinearVelocity(b2body.getLinearVelocity().x,50);
            }
            else if(screen.getPlayer().b2body.getPosition().y < b2body.getPosition().y){
                b2body.setLinearVelocity(b2body.getLinearVelocity().x,-50);
            }
        }else{
            if(screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x){
                b2body.setLinearVelocity(-50,b2body.getLinearVelocity().y);
            }
            else if(screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x){
                b2body.setLinearVelocity(50,b2body.getLinearVelocity().y);
            }
            if(screen.getPlayer().b2body.getPosition().y > b2body.getPosition().y){
                b2body.setLinearVelocity(b2body.getLinearVelocity().x,-50);
            }
            else if(screen.getPlayer().b2body.getPosition().y < b2body.getPosition().y) {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 50);
            }
        }

    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX/Game.PPM,spawnY/Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = screen.getWorld().createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f);
        fdef.filter.categoryBits = Game.FIGHTER_BIT;
        fdef.filter.maskBits = Game.PLAYER_BIT |
                Game.GROUND_BIT |
                Game.PUNCH_BIT;
        fdef.shape = shape;
        fdef.restitution=1f;
        b2body.createFixture(fdef).setUserData(this);
    }

}
