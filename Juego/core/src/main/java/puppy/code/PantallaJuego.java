package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private Texture fondoTextura;
    private boolean enPausa = false;
    private PowerUpDobleMisil itemDobleMisil = null;
    private float tiempoAcumulado = 0f;
    private boolean dobleMisilActivado = false;
    private boolean yaSpawneoAlgunaVez = false;
    private Texture texturaVidaExtra;
    private Texture texturaVelocidad;
    private Texture texturaEscudo;
    private Texture texturaCorazon;
    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private Random r;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 1200);

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        fondoTextura = new Texture(Gdx.files.internal("FondoMarino.png"));

        texturaVidaExtra = new Texture(Gdx.files.internal("PowerUpVidaExtra.png"));
        texturaVelocidad = new Texture(Gdx.files.internal("powerup_velocidad.png"));
        texturaEscudo = new Texture(Gdx.files.internal("powerup_escudo.png"));
        texturaCorazon = new Texture(Gdx.files.internal("PowerUpVidaExtra.png"));

        nave = new Nave4(
            (int)(Gdx.graphics.getWidth() / 2f - 50),
            30,
            new Texture(Gdx.files.internal("SubAmarillo.png")),
            Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
            new Texture(Gdx.files.internal("Rocket2.png")),
            Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))
        );
        nave.setVidas(vidas);
        nave.setJuego(this);

        this.r = new Random();

        float naveX = nave.getArea().x;
        float naveY = nave.getArea().y;
        float distanciaSeguraNave = 200f;
        float distanciaEntreAst = 80f;

        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 nuevoAsteroide;
            boolean posicionMala;
            do {
                float x = r.nextInt(Gdx.graphics.getWidth());
                float y = 50 + r.nextInt(Gdx.graphics.getHeight() - 50);

                float dxN = x - naveX;
                float dyN = y - naveY;
                float distNave = (float) Math.sqrt(dxN * dxN + dyN * dyN);
                boolean demasiadoCercaDeNave = distNave < distanciaSeguraNave;

                boolean demasiadoCercaDeOtro = false;
                for (Ball2 existente : balls1) {
                    float dxA = x - existente.getArea().x;
                    float dyA = y - existente.getArea().y;
                    float distAst = (float) Math.sqrt(dxA * dxA + dyA * dyA);
                    if (distAst < distanciaEntreAst) {
                        demasiadoCercaDeOtro = true;
                        break;
                    }
                }

                posicionMala = (demasiadoCercaDeNave || demasiadoCercaDeOtro);

                if (!posicionMala) {
                    nuevoAsteroide = new Ball2(
                        (int) x,
                        (int) y,
                        20 + r.nextInt(10),
                        velXAsteroides + r.nextInt(4),
                        velYAsteroides + r.nextInt(4),
                        new Texture(Gdx.files.internal("MinaSubmarino.png"))
                    );
                    balls1.add(nuevoAsteroide);
                    balls2.add(nuevoAsteroide);
                } else {
                    nuevoAsteroide = null;
                }

            } while (posicionMala);
        }
    }

    public void dibujaHUD() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float heartSize = 32f;
        for (int i = 0; i < nave.getVidas(); i++) {
            float xHeart = 10 + i * (heartSize + 5);
            float yHeart = screenH - heartSize - 10;
            batch.draw(texturaCorazon, xHeart, yHeart, heartSize, heartSize);
        }

        String textoRonda = "Ronda " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().setColor(1, 1, 1, 1);
        float rondaX = screenW - 200f;
        float rondaY = screenH - 20f;
        game.getFont().draw(batch, textoRonda, rondaX, rondaY);

        game.getFont().getData().setScale(2f);
        game.getFont().setColor(1, 1, 1, 1);
        game.getFont().draw(batch, "Score: " + this.score, 10, 60);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(), 10, 30);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            enPausa = !enPausa;
        }

        if (enPausa) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.getFont().getData().setScale(3f);
            game.getFont().setColor(com.badlogic.gdx.graphics.Color.YELLOW);
            String textoPausa = "PAUSADO";
            float xPausa = (Gdx.graphics.getWidth() - game.getFont().getRegion().getRegionWidth()) / 2f - 100;
            float yPausa = Gdx.graphics.getHeight() / 2f + 30;
            game.getFont().draw(batch, textoPausa, xPausa, yPausa);
            game.getFont().getData().setScale(2f);
            game.getFont().setColor(com.badlogic.gdx.graphics.Color.WHITE);
            game.getFont().draw(batch, "Presiona ESC para continuar",
                Gdx.graphics.getWidth() / 2f - 220,
                Gdx.graphics.getHeight() / 2f - 40);
            batch.end();
            return;
        }

        if (!dobleMisilActivado) {
            tiempoAcumulado += delta;
            if (tiempoAcumulado >= 20f && itemDobleMisil == null) {
                float xRand = new java.util.Random().nextInt(Gdx.graphics.getWidth() - 64);
                itemDobleMisil = new PowerUpDobleMisil(xRand, Gdx.graphics.getHeight());
                tiempoAcumulado = 0f;
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        dibujaHUD();

        nave.update(delta);

        if (!nave.estaHerido()) {
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update(delta);
                for (int j = 0; j < balls1.size(); j++) {
                    if (b.checkCollision(balls1.get(j))) {
                        explosionSound.play();
                        Ball2 minaDestruida = balls1.get(j);
                        float spawnX = minaDestruida.getArea().x;
                        float spawnY = minaDestruida.getArea().y;
                        if (r.nextInt(100) < 30) {
                            int tipoPowerUp = r.nextInt(3);
                            switch (tipoPowerUp) {
                                case 0:
                                    powerUps.add(new PowerUpVidaExtra(spawnX, spawnY, texturaVidaExtra));
                                    break;
                                case 1:
                                    powerUps.add(new PowerUpVelocidad(spawnX, spawnY, texturaVelocidad));
                                    break;
                                case 2:
                                    powerUps.add(new PowerUpEscudo(spawnX, spawnY, texturaEscudo));
                                    break;
                            }
                        }
                        balls1.remove(j);
                        balls2.remove(j);
                        j--;
                        score += 10;
                    }
                }
                if (b.isDestroyed()) {
                    balas.remove(b);
                    i--;
                }
            }
            for (Ball2 ball : balls1) {
                ball.update(delta);
            }
            for (int i = 0; i < balls1.size(); i++) {
                Ball2 ball1 = balls1.get(i);
                for (int j = 0; j < balls2.size(); j++) {
                    Ball2 ball2 = balls2.get(j);
                    if (i < j) {
                        ball1.checkCollision(ball2);
                    }
                }
            }
        }

        for (Bullet b : balas) {
            b.draw(batch);
        }

        nave.draw(batch);

        if (itemDobleMisil != null) {
            itemDobleMisil.update(delta);
            itemDobleMisil.draw(batch);
            if (nave.getHitbox().overlaps(itemDobleMisil.getAreaDeColision())) {
                nave.activarDobleCohete(Float.MAX_VALUE);
                dobleMisilActivado = true;
                itemDobleMisil.destruir();
            }
            if (itemDobleMisil.estaDestruido()) {
                itemDobleMisil = null;
            }
        }

        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.update(delta);
            p.draw(batch);
            if (p.getAreaDeColision().overlaps(nave.getHitbox())) {
                p.aplicarEfecto(nave);
            }
            if (p.estaDestruido()) {
                powerUps.remove(i);
            }
        }

        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            b.draw(batch);
            if (nave.checkCollision(b)) {
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }

        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        batch.end();

        if (balls1.size() == 0) {
            Screen ss = new PantallaJuego(
                game,
                ronda + 1,
                nave.getVidas(),
                score,
                velXAsteroides + 3,
                velYAsteroides + 3,
                cantAsteroides + 10
            );
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
        this.fondoTextura.dispose();
        this.texturaVidaExtra.dispose();
        this.texturaVelocidad.dispose();
        this.texturaEscudo.dispose();
        this.texturaCorazon.dispose();
    }
}
