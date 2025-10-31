package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    private float xSpeed; // <<< float
    private float ySpeed; // <<< float
    private boolean destroyed = false;
    private Sprite spr;

    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) { // <<< Recibe floats
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    // <<< LA MAGIA DE DELTA >>>
    public void update(float delta) { // <<< RECIBE DELTA
        float newX = spr.getX() + xSpeed * delta; // <<< MULTIPLICA POR DELTA
        float newY = spr.getY() + ySpeed * delta; // <<< MULTIPLICA POR DELTA
        spr.setPosition(newX, newY);

        if (newX < 0 || newX + spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (newY < 0 || newY + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean checkCollision(Ball2 b2) {
        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
            this.destroyed = true;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() {return destroyed;}
}
