package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp implements Actualizable {
    protected float x, y, ySpeed;
    protected final Sprite spr;
    protected Sound sonido;
    private boolean destruido = false;

    public PowerUp(float x, float y, Texture tx, float velocidadCaida, Sound sonido) {
        if (tx == null) throw new IllegalArgumentException("Texture no puede ser null");
        this.x = x;
        this.y = y;
        this.ySpeed = velocidadCaida;
        this.sonido = sonido;
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setSize(64, 64);
    }
    public final void activarPowerUp(Nave4 nave) {
        reproducirSonido();
        aplicarEfectoUnico(nave);
        destruir();
    }
    private void reproducirSonido() {
        if (sonido != null) sonido.play();
    }
    protected abstract void aplicarEfectoUnico(Nave4 nave);

    @Override public final void update(float delta) {
        y += ySpeed * delta;
        spr.setPosition(x, y);
        if (y + spr.getHeight() < 0) this.destruido = true;
    }

    public final void draw(SpriteBatch batch) { spr.draw(batch); }
    public Rectangle getHitbox() { return spr.getBoundingRectangle(); }
    public void destruir() { this.destruido = true; }
    public boolean estaDestruido() { return this.destruido; }
}
