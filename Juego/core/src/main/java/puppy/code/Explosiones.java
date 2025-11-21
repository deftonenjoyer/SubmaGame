package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosiones {

    private Texture textura;
    private float x, y, size;
    private float tiempoDeVida;
    private float temporizador;

    public Explosiones(Texture tx, float x, float y, float size) {
        this.textura = tx;
        this.x = x;
        this.y = y;
        this.size = size;
        this.tiempoDeVida = 0.5f;
        this.temporizador = 0f;
    }

    public void update(float delta) {
        temporizador += delta;
    }
    public void draw(SpriteBatch batch) {
        batch.draw(textura, x, y, size, size);
    }
    public boolean isFinished() {
        return temporizador >= tiempoDeVida;
    }
}
