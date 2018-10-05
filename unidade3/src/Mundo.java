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
	private ObjetoGrafico objetoSelecionado;
	
	private Camera camera;
	
	private Mundo() {
		this.camera = new Camera();
		objetos.add(new ObjetoGrafico(GL.GL_LINE_STRIP));
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
		this.objetos.get(this.objetos.size() -1).adicionarPonto(x, y,0,0);
	}
	
	/**
	 * Adiciona um novo objetoGrafico ao final da lista
	 */
	public ObjetoGrafico adicionarObjeto() {
		ObjetoGrafico o = new ObjetoGrafico(GL.GL_LINE_STRIP);		
		this.objetos.add(o);
		return o;
	}
}
