import java.util.ArrayList;
import java.util.List;

public class Mundo {

	private static Mundo instance = new Mundo();
	
	private List<ObjetoGrafico> objetos = new ArrayList<ObjetoGrafico>();
	
	private Camera camera;
	
	private Mundo() {
		this.camera = new Camera();
		objetos.add(new ObjetoGrafico());
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
	
	void adicionarPonto(double x, double y) {
		this.objetos.get(this.objetos.size() -1).adicionarPonto(x, y,0,0);
	}
	
	void adicionarObjeto() {
		ObjetoGrafico o = new ObjetoGrafico();		
		this.objetos.add(o);
	}
}
