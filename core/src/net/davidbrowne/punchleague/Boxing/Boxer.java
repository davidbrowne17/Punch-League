package net.davidbrowne.punchleague.Boxing;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class  Boxer extends Sprite {
    public boolean canPunch;
    public abstract void punch(Player player);
    // Computes the bearing in degrees from the point A(a1,a2) to
    // the point B(b1,b2). A and B are given in terms of
    // screen coordinates.
    public float bearing(float x1,float x2,float y1,float y2){
        final double TWOPI = 6.2831853071795865;
        final double RAD2DEG = 57.2957795130823209;
        double theta = Math.atan2(y1 - x1, x2 - y2);
        if (theta < 0.0)
            theta += TWOPI;
        return (float) (RAD2DEG * theta);
    }

    public abstract void setCanPunch(boolean canPunch);
}
