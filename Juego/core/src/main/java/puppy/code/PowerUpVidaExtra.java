package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpVidaExtra extends PowerUp{
    public PowerUpVidaExtra(float x, float y, Texture tx) {
        // Llama al constructor de PowerUp, pasándole una velocidad de caída (ej: -100 p/s)
        super(x, y, tx, -100f);
    }

    // Implementación del método abstracto.
    // Esto es lo que hace este power-up en particular.
    @Override
    public void aplicarEfecto(Nave4 nave) {
        // Llama a un método en Nave4 (que deberás crear)
        nave.ganarVida(1);

        // Marcar para eliminar después de usar
        this.destruir();
    }
}
