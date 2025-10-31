package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp {
    protected float x;
    protected float y;
    protected float ySpeed; // Velocidad de caída
    protected Sprite spr;

    private boolean destruido = false; // Para marcar si debe eliminarse

    // Constructor común para todos los PowerUps
    public PowerUp(float x, float y, Texture tx, float velocidadCaida) {
        this.x = x;
        this.y = y;
        this.ySpeed = velocidadCaida;

        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        // Ajusta el tamaño si es necesario, ej: 32x32
        this.spr.setSize(64, 64);
    }


    public abstract void aplicarEfecto(Nave4 nave);


    // Mueve el power-up (caída) y comprueba si sale de la pantalla.
    public final void update(float delta) {
        y += ySpeed * delta; // Mover hacia abajo
        spr.setPosition(x, y);

        // Si sale por debajo de la pantalla, marcar para destruir
        if (y + spr.getHeight() < 0) {
            this.destruido = true;
        }
    }

    // Dibuja el sprite del power-up.
    public final void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    // Obtiene el rectángulo de colisión del power-up.
    public Rectangle getAreaDeColision() {
        return spr.getBoundingRectangle();
    }

    // Marca el power-up para ser eliminado (ej. después de ser recogido).
    public void destruir() {
        this.destruido = true;
    }

    // Verifica si el power-up debe ser eliminado del juego.
    public boolean estaDestruido() {
        return this.destruido;
    }
}
