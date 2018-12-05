package thread;


/**
 * Thread respons�vel por chamar o m�todo {@code display} da classe {@link Main}
 * @author Pedro
 *
 */
class Desenhar extends Thread {
	
    private Main main;

    Desenhar(Main main) {
        this.main = main;
    }

    public void run() {
        while (true) {
            main.getGlDrawable().display();
        }
    }
}
