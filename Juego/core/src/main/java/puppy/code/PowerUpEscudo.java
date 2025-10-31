package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpEscudo extends PowerUp{
    public PowerUpEscudo(float x, float y, Texture tx) {
        super(x, y, tx, -100f);
    }
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.activarEscudo();
        this.destruir();
    }
}
