package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class Bullet implements Actualizable {
    private enum Estado { ACTIVA, DESTRUIDA }
    private final float xSpeed;
    private final float ySpeed;
    private final Sprite spr;
    private Estado estado = Estado.ACTIVA;

    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) {
        if (tx == null) throw new IllegalArgumentException("Texture no puede ser null");
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override public void update(float delta) {
        if (estado == Estado.DESTRUIDA) return;
        float newX = spr.getX() + xSpeed * delta;
        float newY = spr.getY() + ySpeed * delta;
        spr.setPosition(newX, newY);
        if (newX < 0 || newX + spr.getWidth() > Gdx.graphics.getWidth()
            || newY < 0 || newY + spr.getHeight() > Gdx.graphics.getHeight()) {
            estado = Estado.DESTRUIDA;
        }
    }

    public void draw(SpriteBatch batch) { spr.draw(batch); }

    public boolean checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            estado = Estado.DESTRUIDA;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() { return estado == Estado.DESTRUIDA; }
}
