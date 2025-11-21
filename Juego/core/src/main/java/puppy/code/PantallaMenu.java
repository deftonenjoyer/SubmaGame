package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaMenu implements Screen {

    private final SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture fondo;
    private BitmapFont fontTitulo;
    private BitmapFont fontTexto;
    private GlyphLayout layout;

    private float blinkTimer = 0f;
    private boolean mostrarTexto = true;

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1200, 1200);

        fondo = new Texture(Gdx.files.internal("PortadaSubmarino.png"));

        fontTitulo = new BitmapFont();
        fontTitulo.setColor(Color.CYAN);
        fontTitulo.getData().setScale(3.5f);

        fontTexto = new BitmapFont();
        fontTexto.setColor(Color.WHITE);
        fontTexto.getData().setScale(2.0f);

        layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        blinkTimer += delta;
        if (blinkTimer > 0.6f) {
            blinkTimer = 0f;
            mostrarTexto = !mostrarTexto;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String titulo = "BIENVENIDO A SUBMAGAME";
        layout.setText(fontTitulo, titulo);
        float tituloX = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float tituloY = (Gdx.graphics.getHeight() / 2f) + 100;
        fontTitulo.draw(batch, layout, tituloX, tituloY);

        if (mostrarTexto) {
            String textoStart = "Presiona ESPACIO para comenzar";
            layout.setText(fontTexto, textoStart);
            float textoX = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float textoY = (Gdx.graphics.getHeight() / 2f) - 40;
            fontTexto.draw(batch, layout, textoX, textoY);
        }

        String textoInstr = "Presiona I para instrucciones";
        layout.setText(fontTexto, textoInstr);
        float instrX = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float instrY = (Gdx.graphics.getHeight() / 2f) - 100;
        fontTexto.draw(batch, layout, instrX, instrY);

        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new PantallaJuego(game, 1, 3, 0, 3, 3, 5));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            game.setScreen(new PantallaInstrucciones(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        fondo.dispose();
        fontTitulo.dispose();
        fontTexto.dispose();
    }
}
