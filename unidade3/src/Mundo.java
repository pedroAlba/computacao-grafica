import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import javax.media.opengl.GL;

/**
 * Objeto singleton, único para toda a aplicação, que mantem informação tanto de objetos tanto quanto da camera.
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
		//Gambiarra aqui:
		//Verifica se selecionado é nulo, se sim, insere um objeto no mundo
		//Isso foi feito pq ao inserir um objeto ao apertar espaço, ele sempre vai ser filho
		//do anterior
		if  (selecionado == null) {
			current = new ObjetoGrafico();
			this.objetos.add(current);
			selecionado = current;
		}
		if  (getLast().getPontos().isEmpty()) {
			atualizaSelecionado();
		}
		selecionado.adicionarPonto(x, y,0,0);
	}

	private ObjetoGrafico atualizaSelecionado() {
		return selecionado = getLast();
	}
	
	/**
	 * Adiciona um novo objetoGrafico ao final da lista
	 */
	public void adicionarObjeto() {
		current = new ObjetoGrafico();
		selecionado.atualizaBBox();
		if  (selecionado == null) {
			//Esse código nunca vai executar, pois esse método é chamado ao apertar espaço
			//para inserir um novo objeto, e o selecionado sempre vai ser ultimo objeto inserido.
			//Logo, o objeto adicionado aqui sempre vai ser filho do anterior.
			this.objetos.add(current);
		} else {
			selecionado.setFilhos(current);
//			selecionado.getFilhos().add(current);
		}
	}

	public void setPrimitiva(int primitiva) {
		if(temSelecionado()) {
			selecionado.setPrimitiva(primitiva);
		}
	}

	public void mudaCor() {
		if(temSelecionado()) {
			selecionado.mudaCor();
		}
	}

	public void dragCurrent(int x, int y) {
		current.drag(x, y);
	}
	
	private ObjetoGrafico getLast() {
		if (temSelecionado()) {
			if  (!selecionado.getFilhos().isEmpty())
				return selecionado.getFilhos().get(selecionado.getFilhos().size() - 1);
		} 
		return objetos.get(objetos.size() - 1);
	}

	private boolean temSelecionado() {
		return selecionado != null;
	}
	public void dragClosestPoint(int newX, int newY) {
		if (temSelecionado()) {
			currentPoint.setX(newX);
			currentPoint.setY(newY);
			selecionado.atualizaBBox();
		}
	}

	public void setupClosestPoint(int x, int y) {
		if(temSelecionado()) {
			currentPoint = selecionado.searchClosest(x, y);
		}
	}

	public void deleteCurrentPoint() {
		if(temSelecionado()) {
			selecionado.getPontos().removeIf(p -> p.getX() == currentPoint.getX() &&
				      p.getY() == currentPoint.getY());
		}
	}

	public void desenhaBBox(GL gl) {
		if(temSelecionado()) {
			selecionado.desenhaBBox(gl);
		}
	}
	
	public void translacaoXYZ(double tx, double ty, double tz){
		if(temSelecionado()) {
			selecionado.translacaoXYZ(tx, ty, tz);
		}
	}
	
	public void escalaXYZ(double Sx,double Sy) {
		if(temSelecionado()) {
			selecionado.escalaXYZ(Sx, Sy);
		}
	}
	
	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		if(temSelecionado()) {
			selecionado.escalaXYZPtoFixo(escala, getCentro());
		}
	}

	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		if(temSelecionado()) {
			selecionado.rotacaoZPtoFixo(angulo, getCentro() );
		}
	}

	private Ponto4D getCentro() {
		if(temSelecionado()) {
		
			List<Ponto4D> pontos = selecionado.getPontos();
		
			DoubleSummaryStatistics x = pontos.stream().map(Ponto4D::getX).collect(Collectors.summarizingDouble(Double::doubleValue));
			DoubleSummaryStatistics y = pontos.stream().map(Ponto4D::getY).collect(Collectors.summarizingDouble(Double::doubleValue));
		
			return new Ponto4D( (((x.getMax() + x.getMin()) / 2) * -1),
								(((y.getMax() + y.getMin()) / 2) * -1));
		} 
		return null;		
	}
	
//	Metodo que deve ser migrado para OG
	public List<ObjetoGrafico> procuraFilhos(ObjetoGrafico pai){
		List<ObjetoGrafico> encontrados = new ArrayList<>();
		for (ObjetoGrafico og : pai.getFilhos()) {
			encontrados.add(og);
			encontrados.addAll(procuraFilhos(og));
		}
		return encontrados;
	}

	public void changeSelection(int x, int y) {
		
		List<ObjetoGrafico> objetosEncontrados = objetos.stream().filter(o -> o.isInside(x, y)).collect(Collectors.toList());
//		Gambi 1
		List<ObjetoGrafico> filhos = new ArrayList<>();
		for (ObjetoGrafico o : objetos) {
			filhos.addAll(procuraFilhos(o));
		}
		List<ObjetoGrafico> filhosEncontrados = filhos.stream().filter(o -> o.isInside(x, y)).collect(Collectors.toList());
		objetosEncontrados.addAll(filhosEncontrados);
//		Fim da Gambi 1
		
		System.out.printf("Existem %d objetos no ponto selecionado\n", objetosEncontrados.size());
		
		if(!objetosEncontrados.isEmpty()) {
			selecionado = objetosEncontrados.get(0);
		} else {
			selecionado = null;
		}
	}


	public void deleteCurrent() {
		if(temSelecionado()) {
			objetos.stream()
		   		   .filter(s -> s.equals(selecionado))
				   .findFirst()
				   .ifPresent(selected -> {
					  objetos.remove(selected);
					  selected.deletaBBox();
				   });
		
		}
	}
}
