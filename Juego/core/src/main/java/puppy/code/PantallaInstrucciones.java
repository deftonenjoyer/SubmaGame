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

public class PantallaInstrucciones implements Screen {

    private final SpaceNavigation game;
    private OrthographicCamera camera;
    private Texture fondo;
    private BitmapFont fontTitulo;
    private BitmapFont fontTexto;
    private GlyphLayout layout;
    private float timer = 0f;

    public PantallaInstrucciones(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        fondo = new Texture(Gdx.files.internal("FondoMarino.png"));

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
        timer += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getBatch().draw(fondo, 0, 0, 1200, 800);

        String titulo = "CÓMO JUGAR";
        layout.setText(fontTitulo, titulo);
        float tituloX = (1200 - layout.width) / 2f;
        float tituloY = 700;
        fontTitulo.draw(game.getBatch(), layout, tituloX, tituloY);

        String[] lineas = {
            "Mueve con W A S D",
            "Dispara con ESPACIO",
            "Evita los asteroides",
            "Agarra powerups para mejorar"
        };

        float startY = 500;
        for (int i = 0; i < lineas.length; i++) {
            layout.setText(fontTexto, lineas[i]);
            float x = (1200 - layout.width) / 2f;
            float y = startY - i * 60;
            fontTexto.draw(game.getBatch(), layout, x, y);
        }

        String textoVolver = "Presiona ESPACIO para volver al menú";
        layout.setText(fontTexto, textoVolver);
        float textoX = (1200 - layout.width) / 2f;
        float textoY = 150;

        float alpha = (float) ((Math.sin(timer * 2) + 1) / 2);
        fontTexto.setColor(1, 1, 1, alpha);
        fontTexto.draw(game.getBatch(), layout, textoX, textoY);
        fontTexto.setColor(Color.WHITE); // restaurar color

        game.getBatch().end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new PantallaMenu(game));
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
