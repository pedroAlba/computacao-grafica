package thread;

/**
 * Thread responsável pela animação de colisão entre foguete - aviao
 * @author Pedro
 *
 */
class Colisao extends Thread {

    private Main main;

    public Colisao(Main main) {
        this.main = main;
    }

    public void run() {
        main.setTerminou(false);
        int i = 0;
        while (i < 100) {
            i++;
            main.getAviao().incAngulo(i < 50);
            main.setLevel(1);
            try {
                sleep(10);
            } catch (InterruptedException ex) {}
        }
        main.getFoguetes().clear();
        main.getAviao().setAtpAngulo(0.0f);
        main.getAviao().setY(0.0f);
        main.getAviao().setX(4);
        main.getAviao().setAtropelado(false);
        main.setTerminou(true);
    }
}
