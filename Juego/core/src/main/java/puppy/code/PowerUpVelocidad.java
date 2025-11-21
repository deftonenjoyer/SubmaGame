package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class PowerUpVelocidad extends PowerUp {
    private static final float DURACION_BOOST = 5.0f;
    public PowerUpVelocidad(float x, float y, Texture tx, Sound sound) {
        super(x, y, tx, -120f, sound);
    }

    @Override
    protected void aplicarEfectoUnico(Nave4 nave) {
        nave.activarBoost(DURACION_BOOST);
    }
}
