package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class PowerUpDobleMisil extends PowerUp {
    private static final float VELOCIDAD_CAIDA = -299f;

    public PowerUpDobleMisil(float x, float y, Texture tx, Sound sound) {
        super(x, y, tx, VELOCIDAD_CAIDA, sound);
    }

    @Override
    protected void aplicarEfectoUnico(Nave4 nave) {
        nave.activarDobleCohete(Float.MAX_VALUE);
    }
}
