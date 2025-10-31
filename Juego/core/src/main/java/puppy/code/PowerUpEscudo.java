package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpEscudo extends PowerUp{
    // Constructor que llama al constructor del padre (PowerUp).
    public PowerUpEscudo(float x, float y, Texture tx) {
        // Cae a velocidad normal (ej: -100 p/s)
        super(x, y, tx, -100f);
    }

    // Implementación del método abstracto.
    // Le dice a la nave que active su escudo.
    @Override
    public void aplicarEfecto(Nave4 nave) {
        // Llama a un método en Nave4 (que deberás crear)
        nave.activarEscudo();

        // Marcar para eliminar después de usar
        this.destruir();
    }
}
