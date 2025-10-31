package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
public class Nave4 {

    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private boolean tieneEscudo = false;
    private long ultimoDisparo = 0;
    private static final long COOLDOWN_DISPARO = 300_000_000L;
    private boolean conBoost = false;
    private float tiempoBoostRestante = 0f;
    private final float VELOCIDAD_NAVE_BOOST = 600.0f; // Velocidad MÁS RÁPIDA
    private final float VELOCIDAD_NAVE_NORMAL = 300.0f;
    private boolean dobleCohete = false;
    private float tiempoDobleCohete = 0f;


    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 64, 64);
    }

    public void activarEscudo() {
        this.tieneEscudo = true;
    }

    public void activarBoost(float duracion) {
        this.conBoost = true;
        this.tiempoBoostRestante = duracion;
    }

    public void ganarVida(int cantidad) {
        this.vidas += cantidad;
    }

    public Rectangle getHitbox() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch, PantallaJuego juego, float delta) {
        float x = spr.getX();
        float y = spr.getY();
        float velocidadActual;

        if (!herido) {
            if (conBoost) {
                velocidadActual = VELOCIDAD_NAVE_BOOST;
                tiempoBoostRestante -= delta;
                if (tiempoBoostRestante <= 0) {
                    conBoost = false;
                }
            } else {
                velocidadActual = VELOCIDAD_NAVE_NORMAL;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                xVel = -velocidadActual;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                xVel = velocidadActual;
            } else {
                xVel = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                yVel = -velocidadActual;
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                yVel = velocidadActual;
            } else {
                yVel = 0;
            }
            float newX = x + xVel * delta;
            float newY = y + yVel * delta;
            if (newX < 0) newX = 0;
            if (newX + spr.getWidth() > Gdx.graphics.getWidth()) newX = Gdx.graphics.getWidth() - spr.getWidth();
            if (newY < 0) newY = 0;
            if (newY + spr.getHeight() > Gdx.graphics.getHeight()) newY = Gdx.graphics.getHeight() - spr.getHeight();

            spr.setPosition(newX, newY);
            spr.draw(batch);

        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            long ahora = TimeUtils.nanoTime();
            if (ahora - ultimoDisparo >= COOLDOWN_DISPARO) {
                if (dobleCohete) {
                    Bullet b1 = new Bullet(
                        spr.getX() + 10,
                        spr.getY() + spr.getHeight(),
                        0, 500,
                        txBala
                    );
                    juego.agregarBala(b1);

                    Bullet b2 = new Bullet(
                        spr.getX() + spr.getWidth() - 20,
                        spr.getY() + spr.getHeight(),
                        0, 500,
                        txBala
                    );
                    juego.agregarBala(b2);

                } else {
                    // bala normal
                    Bullet bala = new Bullet(
                        spr.getX() + spr.getWidth() / 2 - 5,
                        spr.getY() + spr.getHeight(),
                        0, 500,
                        txBala
                    );
                    juego.agregarBala(bala);
                }

                soundBala.play();
                ultimoDisparo = ahora;
            }
        }

    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {

            if (tieneEscudo) {
                // Tienes escudo, no pierdes vida
                tieneEscudo = false; // El escudo se gasta
                sonidoHerido.play(); // Sonido de golpe, pero sin daño

                // Hacemos rebotar la mina
                b.setXSpeed(b.getXSpeed() * -1);
                b.setySpeed(b.getySpeed() * -1);

                return true; // Colisión manejada (sin perder vida)
            }

            // rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            // actualizar vidas y herido
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0)
                destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public com.badlogic.gdx.math.Rectangle getArea() {
        return spr.getBoundingRectangle();
    }


    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }

    public void activarDobleCohete(float duracion) {
        this.dobleCohete = true;
        this.tiempoDobleCohete = duracion;
    }

}
