package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp implements Actualizable {
    protected float x, y, ySpeed;
    protected final Sprite spr;
    private boolean destruido = false;

    public PowerUp(float x, float y, Texture tx, float velocidadCaida) {
        if (tx == null) throw new IllegalArgumentException("Texture no puede ser null");
        this.x = x; this.y = y; this.ySpeed = velocidadCaida;
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setSize(64, 64);
    }

    public abstract void aplicarEfecto(Nave4 nave);

    @Override public final void update(float delta) {
        y += ySpeed * delta;
        spr.setPosition(x, y);
        if (y + spr.getHeight() < 0) this.destruido = true;
    }

    public final void draw(SpriteBatch batch) { spr.draw(batch); }

    public Rectangle getAreaDeColision() {
        return new Rectangle(spr.getX(), spr.getY(), spr.getWidth(), spr.getHeight());
    }

    public void destruir() { this.destruido = true; }
    public boolean estaDestruido() { return this.destruido; }
}

