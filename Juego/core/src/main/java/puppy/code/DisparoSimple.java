package puppy.code;

public class DisparoSimple implements EstrategiaDisparo {

    @Override
    public void disparar(Nave4 nave, PantallaJuego juego) {
        Bullet bala = new Bullet(
            nave.getX() + nave.getWidth() / 2f - 5f,
            nave.getY() + nave.getHeight(),
            0f, 500f,
            nave.getTxBala()
        );
        juego.agregarBala(bala);
        nave.getSoundBala().play();
    }
}
