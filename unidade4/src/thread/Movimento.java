package thread;
import object.Foguete;

/**
 * Classe responsável por movimentar os foguetes e verificar colisão
 * @author Pedro
 *
 */
class Movimento extends Thread {

    private Main main;

    public Movimento(Main main) {
        this.main = main;
    }

    public void run() {
        while (true) {
            for (Foguete foguete : main.getFoguetes()) {
                foguete.movimentar();
                foguete.setBateu(main.getAviao().tentaAtropelar(foguete.getF(), foguete.getSaida()));
            }
            if (main.getAviao().isAtropelado() && main.isTerminou()) {
                new Colisao(main).start();
            }
            try {
                sleep(30);
            } catch (InterruptedException ex) { }
        }
    }
}
