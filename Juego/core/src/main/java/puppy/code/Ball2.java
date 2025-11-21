package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 {
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private Sprite spr;
    private static final float SIZE = 64f;

    public Ball2(int x, int y, int size, float xSpeed, float ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setSize(SIZE, SIZE);
        this.x = x - SIZE / 2f;
        this.y = y - SIZE / 2f;
        if (this.x < 0) this.x = 0;
        if (this.x + SIZE > Gdx.graphics.getWidth()) this.x = Gdx.graphics.getWidth() - SIZE;
        if (this.y < 0) this.y = 0;
        if (this.y + SIZE > Gdx.graphics.getHeight()) this.y = Gdx.graphics.getHeight() - SIZE;
        spr.setPosition(this.x, this.y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update(float delta) {
        x += xSpeed * delta * 20f;
        y += ySpeed * delta * 20f;
        if (x < 0) {
            x = 0;
            xSpeed = -xSpeed;
        } else if (x + SIZE > Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - SIZE;
            xSpeed = -xSpeed;
        }
        if (y < 0) {
            y = 0;
            ySpeed = -ySpeed;
        } else if (y + SIZE > Gdx.graphics.getHeight()) {
            y = Gdx.graphics.getHeight() - SIZE;
            ySpeed = -ySpeed;
        }
        spr.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public Rectangle getHitbox() {
        return spr.getBoundingRectangle();
    }

    public void checkCollision(Ball2 b2) {
        if (getHitbox().overlaps(b2.getHitbox())) {

            // Arreglo para acceder a variables privadas (error anterior)
            float tempX = this.getXSpeed();
            float tempY = this.getySpeed();

            this.setXSpeed(b2.getXSpeed());
            this.setySpeed(b2.getySpeed());

            b2.setXSpeed(tempX);
            b2.setySpeed(tempY);

            float overlapX = (getCenterX() - b2.getCenterX());
            float overlapY = (getCenterY() - b2.getCenterY());
            x += Math.signum(overlapX) * 2f;
            y += Math.signum(overlapY) * 2f;
            b2.x -= Math.signum(overlapX) * 2f;
            b2.y -= Math.signum(overlapY) * 2f;
            spr.setPosition(x, y);
            b2.spr.setPosition(b2.x, b2.y);
        }
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getCenterX() {
        return x + SIZE / 2f;
    }

    public float getCenterY() {
        return y + SIZE / 2f;
    }
}
