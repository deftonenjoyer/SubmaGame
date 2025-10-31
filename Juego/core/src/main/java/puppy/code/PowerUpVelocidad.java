package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpVelocidad extends PowerUp{
    private static final float DURACION_BOOST = 5.0f;
    public PowerUpVelocidad(float x, float y, Texture tx) {
        super(x, y, tx, -120f);
    }
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.activarBoost(DURACION_BOOST);
        this.destruir();
    }
}
