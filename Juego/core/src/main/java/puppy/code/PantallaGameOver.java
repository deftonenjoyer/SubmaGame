package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

    private final SpaceNavigation game;
    private OrthographicCamera camera;
    private Texture fondo;
    private BitmapFont fontTitulo;
    private BitmapFont fontTexto;
    private GlyphLayout layout;
    private float timer = 0f;

    public PantallaGameOver(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        fondo = new Texture(Gdx.files.internal("GameOverScreen.png"));

        fontTitulo = new BitmapFont();
        fontTitulo.setColor(Color.RED);
        fontTitulo.getData().setScale(4.0f);

        fontTexto = new BitmapFont();
        fontTexto.setColor(Color.WHITE);
        fontTexto.getData().setScale(2.0f);

        layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        timer += delta;

        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String titulo = "GAME OVER";
        layout.setText(fontTitulo, titulo);
        float tituloX = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float tituloY = (Gdx.graphics.getHeight() / 2f) + 80;
        fontTitulo.draw(game.getBatch(), layout, tituloX, tituloY);

        String mensaje = "Presiona ESPACIO para volver a jugar";
        layout.setText(fontTexto, mensaje);
        float textoX = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float textoY = (Gdx.graphics.getHeight() / 2f) - 40;

        float alpha = (float) ((Math.sin(timer * 2) + 1) / 2); // valor entre 0 y 1
        fontTexto.setColor(1, 1, 1, alpha);
        fontTexto.draw(game.getBatch(), layout, textoX, textoY);
        fontTexto.setColor(Color.WHITE);

        game.getBatch().end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new PantallaJuego(game, 1, 3, 0, 3, 3, 5));
            dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        fondo.dispose();
        fontTitulo.dispose();
        fontTexto.dispose();
    }
}
