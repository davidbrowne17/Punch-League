package net.davidbrowne.punchleague.Boxing;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Screens.PlayScreen;

public class Player extends Boxer {
    public enum State {standing,punchingRight,punchingLeft,dead}
    public State currentState,previousState;
    private float spawnX,spawnY;
    private PlayScreen screen;
    private boolean punching=false;
    private Animation<TextureRegion> playerRun;
    private int punchesRecieved=0;
    private Animation<TextureRegion> stand,jab,straight,left_hook,right_hook;
    private boolean rotatedLeft=false,rotatedRight=false;
    public Body b2body;
    private float stateTimer;
    private Fixture fix;
    private FixtureDef punchdef = new FixtureDef();
    private int spriteId;
    public Player(PlayScreen screen, int spawnX, int spawnY,int spriteid) {
        this.setOrigin(getWidth() /2f, getHeight() /2f);
        currentState=State.standing;
        spriteId=spriteid;
        this.screen=screen;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("player"+spriteId));
        frames.add(screen.getAtlas().findRegion("player"+spriteId+"_walk"));
        frames.add(screen.getAtlas().findRegion("player"+spriteId+"_walk2"));
        playerRun = new Animation(0.1f,frames);
        this.spawnX=spawnX;
        this.spawnY=spawnY;
        stateTimer=0;
        stand = new Animation(0.1f,frames);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("player"+spriteId)));
        setBounds(spawnX/Game.PPM,spawnY/Game.PPM,64,64);
        definePlayer();
        setOrigin(getWidth()/2, getHeight()/2);

    }

    public int getPunchesReceived() {
        return punchesRecieved;
    }

    public void addPunchesReceived() {
        punchesRecieved++;
        screen.getManager().get("sounds/punch.wav", Sound.class).play(screen.getGame().getVolume());
    }

    @Override
    public void punch(Player player) {

    }

    public void updateSprite(float dt){
        setRotation(bearing(getX(),getY(),screen.getFighter().getX(),screen.getFighter().getY()));
        switch (currentState){
            case standing:
                setRegion(playerRun.getKeyFrame(stateTimer,true));
                break;
            case punchingLeft:
                setRegion(screen.getAtlas().findRegion("player"+spriteId+"_left_hook"));
                break;
            case punchingRight:
                setRegion(screen.getAtlas().findRegion("player"+spriteId+"_right_hook"));
                break;
            case dead:
                setRegion(screen.getAtlas().findRegion("player"+spriteId+"_dead"));
                break;
        }
        if(getPunchesReceived()>=100)
            currentState = State.dead;
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
    }

    public void update(float dt){
        updateSprite(dt);
        setPosition(b2body.getPosition().x  - getWidth()/2 ,b2body.getPosition().y - getHeight()/2 );
        if(currentState==State.dead)
            b2body.setLinearVelocity(0,0);
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX/Game.PPM,spawnY/Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = screen.getWorld().createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f);
        fdef.filter.categoryBits = Game.PLAYER_BIT;
        fdef.filter.maskBits = Game.FIGHTER_BIT | Game.GROUND_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public Fixture createPunch(){
        CircleShape shape1 = new CircleShape();
        shape1.setRadius(15f);
        punchdef.shape = shape1;
        punchdef.isSensor = true;
        punchdef.filter.categoryBits = Game.PUNCH_BIT;
        punchdef.filter.maskBits = Game.FIGHTER_BIT;
        Fixture fix1 = b2body.createFixture(punchdef);
        fix1.setUserData(this);
        return fix1;
    }

    public void punch(boolean left){
        Timer timer = new Timer();
        if(left)
            currentState = State.punchingLeft;
        else
            currentState = State.punchingRight;
        Timer.Task task = timer.scheduleTask(new Timer.Task() {
            @Override
            public void run () {
                currentState = State.standing;
            }
        },0.2f);
        fix = createPunch();
        if(canPunch==true && !punching) {

            punching = true;
            screen.getFighter().addPunchesRecieved();
            Timer.Task task1 = timer.scheduleTask(new Timer.Task() {
                @Override
                public void run () {
                    punching=false;
                }
            },0.5f);
            canPunch=false;
            while (b2body.getFixtureList().size > 1) {
                b2body.destroyFixture(b2body.getFixtureList().pop());
            }
        }

    }
    @Override
    public void setCanPunch(boolean canPunch) {
        this.canPunch = canPunch;
    }

}
