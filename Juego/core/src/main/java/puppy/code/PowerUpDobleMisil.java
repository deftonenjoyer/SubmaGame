package puppy.code;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerUpDobleMisil {

    private float x;
    private float y;
    private float velocidadY = -299f; // cae hacia abajo
    private Sprite spr;
    private Texture tx; // textura generada (círculo)
    private boolean destruido = false;

    public PowerUpDobleMisil(float x, float y) {
        this.x = x;
        this.y = y;

        // Generar una textura circular 64x64 (rellena) vía Pixmap
        int size = 64;
        Pixmap pm = new Pixmap(size, size, Format.RGBA8888);
        pm.setColor(1f, 0.85f, 0.1f, 1f); // color amarillo/naranja
        pm.fillCircle(size / 2, size / 2, size / 2);
        this.tx = new Texture(pm);
        pm.dispose();

        spr = new Sprite(tx);
        spr.setSize(size, size);
        spr.setOriginCenter();
        spr.setPosition(x, y - spr.getHeight()); // asegurar que entra en pantalla
    }

    public void update(float delta) {
        y += velocidadY * delta;
        spr.setY(y);
        // si sale de pantalla → destruir (y liberar textura)
        if (y + spr.getHeight() < 0) {
            destruir();
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean estaDestruido() {
        return destruido;
    }

    public void destruir() {
        if (!destruido) {
            destruido = true;
            if (tx != null) {
                tx.dispose();
                tx = null;
            }
        }
    }

    public Sprite getSprite() {
        return spr;
    }
}
