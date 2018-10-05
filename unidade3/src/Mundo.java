import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Objeto singleton, único para toda a aplicação, que mantem informação tanto de objetos tanto quanto da camera.
 *
 */
public class Mundo {

	private static Mundo instance = new Mundo();
	
	private List<ObjetoGrafico> objetos = new ArrayList<ObjetoGrafico>();
	
	//TODO: Avaliar se é o local mais correto
	private ObjetoGrafico selecionado;
	
	private ObjetoGrafico current;
	
	private Camera camera;
	
	private Mundo() {
		this.camera = new Camera();
		ObjetoGrafico o = new ObjetoGrafico(GL.GL_LINE_STRIP);
		objetos.add(o);
		selecionado = o; 
		current = o;
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
	
	/**
	 * Adiciona um novo ponto, no ultimo objeto inserido na lista
	 * @param x coordenada x
	 * @param y coordenada y
	 */
	void adicionarPonto(double x, double y) {
		getLast().adicionarPonto(x, y,0,0);
	}
	
	/**
	 * Adiciona um novo objetoGrafico ao final da lista
	 */
	public void adicionarObjeto() {
		current = new ObjetoGrafico();
		selecionado = getLast();
		this.objetos.add(current);
	}

	public void setPrimitiva(int primitiva) {
		selecionado.setPrimitiva(primitiva);
	}

	public void mudaCor() {
		selecionado.mudaCor();
	}

	public void drag(int x, int y) {
		current.drag(x, y);
	}
	
	private ObjetoGrafico getLast() {
		return objetos.get(objetos.size() - 1);
	}
}
