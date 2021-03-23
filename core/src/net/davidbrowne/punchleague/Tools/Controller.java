package net.davidbrowne.punchleague.Tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.punchleague.Game;

public class Controller {
    Viewport viewport;
    Stage stage;
    Boolean upPressed=false, downPressed=false, leftPressed=false, rightPressed=false,aPressed=false,bPressed=false;
    OrthographicCamera cam;

    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FillViewport(Game.V_WIDTH,Game.V_HEIGHT,cam);
        stage = new Stage(viewport,Game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.left().bottom();

        Image upImg = new Image(new Texture("controller/up.png"));
        upImg.setSize(100,100);
        upImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed=false;
            }
        });

        Image downImg = new Image(new Texture("controller/down.png"));
        downImg.setSize(100,100);
        downImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed=false;
            }
        });

        Image leftImg = new Image(new Texture("controller/left.png"));
        leftImg.setSize(100,100);
        leftImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed=false;
            }
        });

        Image rightImg = new Image(new Texture("controller/right.png"));
        rightImg.setSize(100,100);
        rightImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed=false;
            }
        });

        table.add();
        table.add(upImg).size(upImg.getWidth(),upImg.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(leftImg).size(leftImg.getWidth(),leftImg.getHeight()).padLeft(100);
        table.add();
        table.add(rightImg).size(rightImg.getWidth(),rightImg.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(),downImg.getHeight());
        table.add();
        table.padBottom(40).padLeft(40);
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            stage.addActor(table);

        Image aImg = new Image(new Texture("controller/A.png"));
        aImg.setSize(100,100);
        aImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                aPressed = true;
                Timer timer = new Timer();
                Timer.Task task = timer.schedule(new Timer.Task() {
                    @Override
                    public void run () {
                        aPressed=false;
                    }
                }, 0.3f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aPressed=false;
            }
        });

        Image bImg = new Image(new Texture("controller/B.png"));
        bImg.setSize(100,100);
        bImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = true;
                Timer timer = new Timer();
                Timer.Task task = timer.schedule(new Timer.Task() {
                    @Override
                    public void run () {
                        bPressed=false;
                    }
                }, 0.3f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bPressed=false;
            }
        });
        Table table1 = new Table();
        table1.bottom();
        table1.padLeft(2700).padBottom(20);
        table1.add(aImg).size(aImg.getWidth(),aImg.getHeight()).padRight(50);
        table1.add(bImg).size(bImg.getWidth(),bImg.getHeight()).padBottom(150).padRight(40);
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            stage.addActor(table1);
    }

    public void draw(){
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setInputProcessor(stage);
        else
            Gdx.input.setInputProcessor(new InputAdapter());
        stage.draw();
    }

    public Boolean getaPressed() {
        return aPressed;
    }

    public Boolean getbPressed() {
        return bPressed;
    }

    public Boolean getUpPressed() {
        return upPressed;
    }

    public Boolean getDownPressed() {
        return downPressed;
    }

    public Boolean getLeftPressed() {
        return leftPressed;
    }

    public Boolean getRightPressed() {
        return rightPressed;
    }

    public void resize(int width,int height){
        viewport.update(width,height);
    }
}
