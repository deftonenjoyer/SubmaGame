package puppy.code;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class PowerUpDobleMisil extends PowerUp {

    private static final float VELOCIDAD_CAIDA = -299f;

    public PowerUpDobleMisil(float x, float y) {
        super(x, y, crearTexturaCircular(), VELOCIDAD_CAIDA);
    }

    private static Texture crearTexturaCircular() {
        int size = 64;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 0.85f, 0.1f, 1f); // amarillo-naranja
        pm.fillCircle(size / 2, size / 2, size / 2);
        Texture tx = new Texture(pm);
        pm.dispose();
        return tx;
    }

    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.activarDobleCohete(Float.MAX_VALUE);
    }
}
