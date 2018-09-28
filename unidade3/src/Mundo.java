import java.util.ArrayList;
import java.util.List;

public class Mundo {

	private static Mundo instance = new Mundo();
	
	private List<ObjetoGrafico> objetos = new ArrayList<ObjetoGrafico>();
	
	private Camera camera;
	
	private Mundo() {
		this.camera = new Camera();
	}
	
	public static Mundo getInstance() {
		if (instance == null)
			instance = new Mundo();
		return instance;
	}	
	
	public Camera getCamera() {
		return camera;
	}

	public List<ObjetoGrafico> getObjetos() {
		return objetos;
	}
	
	void adicionaObjeto(double x, double y) {
		if(this.objetos.size() == 0) {
			ObjetoGrafico o = new ObjetoGrafico();
			o.adicionarPonto(x, y,0,0);
			this.objetos.add(o);
			return;
		}
		this.objetos.get(this.objetos.size() -1).adicionarPonto(x, y,0,0);
	}

	public void alteraUltimo(int x, int y) {
		if(! this.objetos.isEmpty())
			this.objetos.get(this.objetos.size() - 1).alteraUltimoPonto(x,y);		
	}
}
