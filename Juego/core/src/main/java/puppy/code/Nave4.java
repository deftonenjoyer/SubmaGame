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

public class Nave4 implements Actualizable {

    private static final long COOLDOWN_DISPARO = 300_000_000L;
    private static final float VELOCIDAD_NAVE_BOOST = 600.0f;
    private static final float VELOCIDAD_NAVE_NORMAL = 300.0f;

    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0f;
    private float yVel = 0f;

    private final Sprite spr;
    private final Sound sonidoHerido;
    private final Sound soundBala;
    private final Texture txBala;

    private boolean herido = false;
    private final int tiempoHeridoMax = 50;
    private int tiempoHerido;

    private boolean tieneEscudo = false;
    private long ultimoDisparo = 0L;

    private boolean conBoost = false;
    private float tiempoBoostRestante = 0f;

    private boolean dobleCohete = false;
    private float tiempoDobleCohete = 0f;

    private PantallaJuego juego; // inyección ligera para crear balas

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        if (tx == null || soundChoque == null || txBala == null || soundBala == null) {
            throw new IllegalArgumentException("Argumentos no pueden ser null");
        }
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setBounds(x, y, 64, 64);
    }

    public void setJuego(PantallaJuego juego) {
        this.juego = juego;
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
        return new Rectangle(spr.getX(), spr.getY(), spr.getWidth(), spr.getHeight());
    }

    @Override
    public void update(float delta) {
        float velocidadActual = conBoost ? VELOCIDAD_NAVE_BOOST : VELOCIDAD_NAVE_NORMAL;

        if (conBoost) {
            tiempoBoostRestante -= delta;
            if (tiempoBoostRestante <= 0f) conBoost = false;
        }

        if (dobleCohete) {
            tiempoDobleCohete -= delta;
            if (tiempoDobleCohete <= 0f) dobleCohete = false;
        }

        if (!herido) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) xVel = -velocidadActual;
            else if (Gdx.input.isKeyPressed(Input.Keys.D)) xVel = velocidadActual;
            else xVel = 0f;

            if (Gdx.input.isKeyPressed(Input.Keys.S)) yVel = -velocidadActual;
            else if (Gdx.input.isKeyPressed(Input.Keys.W)) yVel = velocidadActual;
            else yVel = 0f;

            float newX = spr.getX() + xVel * delta;
            float newY = spr.getY() + yVel * delta;

            if (newX < 0) newX = 0;
            if (newX + spr.getWidth() > Gdx.graphics.getWidth())
                newX = Gdx.graphics.getWidth() - spr.getWidth();
            if (newY < 0) newY = 0;
            if (newY + spr.getHeight() > Gdx.graphics.getHeight())
                newY = Gdx.graphics.getHeight() - spr.getHeight();

            spr.setPosition(newX, newY);
        } else {
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            long ahora = TimeUtils.nanoTime();
            if (ahora - ultimoDisparo >= COOLDOWN_DISPARO) {
                disparar();
                ultimoDisparo = ahora;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!herido) {
            spr.draw(batch);
        } else {
            float x = spr.getX();
            spr.setX(x + MathUtils.random(-2f, 2f));
            spr.draw(batch);
            spr.setX(x);
        }
    }

    private void disparar() {
        if (juego == null) return;
        if (dobleCohete) {
            Bullet b1 = new Bullet(
                spr.getX() + 10,
                spr.getY() + spr.getHeight(),
                0f, 500f,
                txBala
            );
            juego.agregarBala(b1);
            Bullet b2 = new Bullet(
                spr.getX() + spr.getWidth() - 20,
                spr.getY() + spr.getHeight(),
                0f, 500f,
                txBala
            );
            juego.agregarBala(b2);
        } else {
            Bullet bala = new Bullet(
                spr.getX() + spr.getWidth() / 2f - 5f,
                spr.getY() + spr.getHeight(),
                0f, 500f,
                txBala
            );
            juego.agregarBala(bala);
        }
        soundBala.play();
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            if (tieneEscudo) {
                tieneEscudo = false;
                sonidoHerido.play();
                b.setXSpeed(b.getXSpeed() * -1);
                b.setySpeed(b.getySpeed() * -1);
                return true;
            }

            if (xVel == 0) xVel += b.getXSpeed() / 2f;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) (xVel / 2f));
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2f;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) (yVel / 2f));
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public Rectangle getArea() {
        return new Rectangle(spr.getX(), spr.getY(), spr.getWidth(), spr.getHeight());
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
        this.vidas = vidas2;
    }

    public void activarDobleCohete(float duracion) {
        this.dobleCohete = true;
        this.tiempoDobleCohete = duracion;
    }
}
