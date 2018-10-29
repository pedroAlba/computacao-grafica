import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.media.opengl.GL;

/**
 * Objeto singleton, �nico para toda a aplica��o, que mantem informa��o tanto de objetos tanto quanto da camera.
 *
 */
public class Mundo {

	private static Mundo instance = new Mundo();
	
	private List<ObjetoGrafico> objetos = new ArrayList<ObjetoGrafico>();
	
	private ObjetoGrafico selecionado;
	
	private ObjetoGrafico current;
	
	private Camera camera;
	
	private Ponto4D currentPoint;
	
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
		if  (getLast().getPontos().isEmpty()) {
			atualizaSelecionado();
		}
		getLast().adicionarPonto(x, y,0,0);
	}

	private ObjetoGrafico atualizaSelecionado() {
		return selecionado = getLast();
	}
	
	/**
	 * Adiciona um novo objetoGrafico ao final da lista
	 */
	public void adicionarObjeto() {
		current = new ObjetoGrafico();
		getLast().atualizaBBox();
		this.objetos.add(current);
	}

	public void setPrimitiva(int primitiva) {
		selecionado.setPrimitiva(primitiva);
	}

	public void mudaCor() {
		selecionado.mudaCor();
	}

	public void dragCurrent(int x, int y) {
		current.drag(x, y);
	}
	
	private ObjetoGrafico getLast() {
		return objetos.get(objetos.size() - 1);
	}

	public void dragClosestPoint(int newX, int newY) {
		currentPoint.setX(newX);
		currentPoint.setY(newY);
		selecionado.atualizaBBox();
	}

	public void setupClosestPoint(int x, int y) {
		currentPoint = selecionado.searchClosest(x, y);
	}

	public void deleteCurrentPoint() {
		selecionado.getPontos().removeIf(p -> p.getX() == currentPoint.getX() &&
											  p.getY() == currentPoint.getY());
	}

	public void desenhaBBox(GL gl) {
		selecionado.desenhaBBox(gl);
	}
	
	public void translacaoXYZ(double tx, double ty, double tz){
		selecionado.translacaoXYZ(tx, ty, tz);		
	}
	
	public void escalaXYZ(double Sx,double Sy) {
		selecionado.escalaXYZ(Sx, Sy);
	}
	
	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		selecionado.escalaXYZPtoFixo(escala, getCentro());
	}

	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		selecionado.rotacaoZPtoFixo(angulo, getCentro() );
	}

	private Ponto4D getCentro() {
		
		double maiorX = selecionado.getPontos().stream().mapToDouble(Ponto4D::getX).max().getAsDouble();
		double maiorY = selecionado.getPontos().stream().mapToDouble(Ponto4D::getY).max().getAsDouble();
		double menorX = selecionado.getPontos().stream().mapToDouble(Ponto4D::getX).min().getAsDouble();
		double menorY = selecionado.getPontos().stream().mapToDouble(Ponto4D::getY).min().getAsDouble();
		
		Ponto4D p = new Ponto4D();
		p.setX(((maiorX + menorX)  / 2) * -1);
		p.setY(((maiorY + menorY)  / 2) * -1);
		return p;
	}

	public void changeSelection(int x, int y) {
		
		List<ObjetoGrafico> objetosEncontrados = objetos.stream().filter(o -> o.isInside(x, y)).collect(Collectors.toList());
		System.out.printf("Existem %d objetos no ponto selecionado\n", objetosEncontrados.size());
		
		if(!objetosEncontrados.isEmpty()) {
			selecionado = objetosEncontrados.get(0);
		}
	}

	public void deleteCurrent() {
		objetos.stream()
				.filter(s -> s.equals(selecionado))
				.findFirst()
				.ifPresent(selected -> {
					objetos.remove(selected);
					selected.deletaBBox();
				});
	}
}
