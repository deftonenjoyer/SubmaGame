package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class PowerUpVidaExtra extends PowerUp {
    public PowerUpVidaExtra(float x, float y, Texture tx, Sound sound) {
        super(x, y, tx, -100f, sound);
    }

    @Override
    protected void aplicarEfectoUnico(Nave4 nave) {
        nave.ganarVida(1);
    }
}
