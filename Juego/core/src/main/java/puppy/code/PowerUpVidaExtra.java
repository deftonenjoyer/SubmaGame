package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpVidaExtra extends PowerUp{
    public PowerUpVidaExtra(float x, float y, Texture tx) {
        super(x, y, tx, -100f);
    }
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.ganarVida(1);
        this.destruir();
    }
}
