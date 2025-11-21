package puppy.code;

public class DisparoDoble implements EstrategiaDisparo {

    @Override
    public void disparar(Nave4 nave, PantallaJuego juego) {
        Bullet b1 = new Bullet(
            nave.getX() + 10,
            nave.getY() + nave.getHeight(),
            0f, 500f,
            nave.getTxBala()
        );
        juego.agregarBala(b1);

        Bullet b2 = new Bullet(
            nave.getX() + nave.getWidth() - 20,
            nave.getY() + nave.getHeight(),
            0f, 500f,
            nave.getTxBala()
        );
        juego.agregarBala(b2);

        nave.getSoundBala().play();
    }
}
