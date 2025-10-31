package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpVelocidad extends PowerUp{
    private static final float DURACION_BOOST = 5.0f;

    // Constructor que llama al constructor del padre (PowerUp).
    public PowerUpVelocidad(float x, float y, Texture tx) {
        // Cae un poco más rápido para diferenciarlo (ej: -120 p/s)
        super(x, y, tx, -120f);
    }

    // Implementación del método abstracto.
    // Le dice a la nave que active su boost de velocidad.
    @Override
    public void aplicarEfecto(Nave4 nave) {
        // Llama a un método en Nave4 (que deberás crear)
        nave.activarBoost(DURACION_BOOST);

        // Marcar para eliminar después de usar
        this.destruir();
    }
}
