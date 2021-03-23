package net.davidbrowne.punchleague.Models;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.davidbrowne.punchleague.Screens.HomeScreen;

import java.util.Random;

public class GameEvent {
    private Dialog dialog;
    private int id;
    private Skin skin;
    private Dialog fightOffer;
    private HomeScreen homeScreen;
    public GameEvent(int id, Skin skin){
        this.id=id;
        this.skin=skin;
        dialog = new Dialog("", skin);
        dialog.button("Continue");
    }
    public GameEvent(int id, Skin skin, HomeScreen screen){
        homeScreen=screen;
        this.id=id;
        this.skin=skin;
        dialog = new Dialog("", skin);
        dialog.button("Continue");
    }

    public Dialog getFightOffer() {
        return fightOffer;
    }

    public void loadFightOffer(final int id){
        fightOffer = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    //accept fight
                    homeScreen.setNextOpponent(new BoxerDetails(homeScreen.getBoxersList().get(id).getName(),homeScreen.getBoxersList().get(id).getId()));
                }else{
                    //rejected fight
                    homeScreen.setNextOpponent(null);
                }
            }
        };
        fightOffer.text("Received fight offer from: "+homeScreen.getBoxersList().get(id).getName());
        fightOffer.button("Accept",true);
        fightOffer.button("Reject",false);
    }
    public void loadDialogs(){
        switch(id){
            case 1:
                dialog.text("Its fight time!");
                break;
            case 2:
                dialog.text("You gained 1000 fans!");
                break;
            case 3:
                dialog.text("You have lost 500 fans!");
                break;
            case 4:
                dialog.text("You moved up the rankings!");
                break;
            case 5:
                dialog.text("You got knocked out!");
                break;
            case 6:
                dialog.text("You lost a decision!");
                break;
            case 7:
                dialog.text("You won a decision!");
                break;
            case 8:
                dialog.text("You won via Knockout!");
                break;
             default:
                break;
        }
    }
    public void loadNewsDialogs(int newsId){
        Random rand= new Random();
        Boolean ran = rand.nextBoolean();
        if(homeScreen.getNextOpponent()==null) {
            switch (newsId) {
                case 2:
                    Label label = new Label(homeScreen.getPlayerName() + " gains small hype?", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " on the rise?");
                    dialog.text(label);
                    break;
                case 3:
                    label = new Label(homeScreen.getPlayerName() + " good enough to be a top fighter?", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " is getting fans excited");
                    dialog.text(label);
                    break;
                case 4:
                    label = new Label(homeScreen.getPlayerName() + " to make it to the top?", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " determined to be champion!");
                    dialog.text(label);
                    break;
                case 5:
                    label = new Label(homeScreen.getPlayerName() + " in line for a title shot soon?", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " is one of the best in the world!");
                    dialog.text(label);
                    break;
                case 6:
                    label = new Label(homeScreen.getPlayerName() + " is the champ!", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " cant be stopped");
                    dialog.text(label);
                    break;
                default:
                    label = new Label(homeScreen.getPlayerName() + " has a long road to get to the top!", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " wants to prove doubters wrong!");
                    dialog.text(label);
                    break;
            }
        }
        else{
            switch (newsId) {
                case 2:
                    Label label = new Label(homeScreen.getPlayerName() + " faces big challenge in "+homeScreen.getNextOpponent().getName(), homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getNextOpponent().getName()+" favoured to win next fight.");
                    dialog.text(label);
                    break;
                case 3:
                    label = new Label(homeScreen.getNextOpponent().getName() + " signs the contract!", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getNextOpponent().getName() + " says "+homeScreen.getPlayerName()+" is scared!");
                    dialog.text(label);
                    break;
                case 4:
                    label = new Label(homeScreen.getNextOpponent().getName() + " an underdog?", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getPlayerName() + " training hard for "+homeScreen.getNextOpponent().getName());
                    dialog.text(label);
                    break;
                case 5:
                    label = new Label(homeScreen.getPlayerName() + " wants to K.O. "+homeScreen.getNextOpponent().getName(), homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getNextOpponent().getName() + " faces toughest test!");
                    dialog.text(label);
                    break;
                case 6:
                    label = new Label(homeScreen.getNextOpponent().getName() + " eyes gold!", homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getNextOpponent().getName() + " gets his title shot!");
                    dialog.text(label);
                    break;
                default:
                    label = new Label(homeScreen.getPlayerName() + " faces "+homeScreen.getNextOpponent().getName(), homeScreen.getMySkin());
                    if (ran)
                        label.setText(homeScreen.getNextOpponent().getName() + " fears no man!");
                    dialog.text(label);
                    break;
            }
        }
    }
    public void loadSaveDialog(){
        dialog.text("Game saved!");
    }
    public void loadMoneyDialog(int money){
        dialog.text("Your fight purse is "+money+"!");
    }

    public Dialog getDialog() {
        return dialog;
    }
}
