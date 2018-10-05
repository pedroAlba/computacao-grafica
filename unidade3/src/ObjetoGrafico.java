import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Classe que representa um ObjetoGrafico, armazena informações de todos os seus pontos, sua primitiva, boundingBox e cor.
 *
 */
public class ObjetoGrafico {

	private List<Ponto4D> pontos = new ArrayList<>();
	
	private int primitiva;
	
	private BoundingBox bbox;
	
	private String color;
	
	/**
	 * Controle para ver se precisa desenhar o rastro
	 */
	private boolean desenhando = true;
	
	void desenha(GL gl) {
		desenhaLinhas(gl);
	}

	public ObjetoGrafico(int primitiva) {
		this.primitiva = primitiva;
		this.bbox = new BoundingBox();
	}
	/**
	 * Itera sobre todos os pontos, e inicia um desenho na tela, com base na primitiva definida na variavel {@link primitiva}
	 * @param gl
	 */
	private void desenhaLinhas(GL gl) {
		
		gl.glLineWidth(2.0f);
		
		gl.glBegin(primitiva);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		for (Ponto4D p : pontos) {
			gl.glVertex2d(p.getX(), p.getY());
		}
		
		gl.glEnd();		
		
	}
	
	void adicionarPonto(double x, double y, double z, double w) {
		pontos.add(new Ponto4D(x, y, z, w));
	}
	
	public List<Ponto4D> getPontos() {
		return pontos;
	}
	
	@Override
	public String toString() {
		return "Objeto Gráfico: " + pontos.toString() + "\n";
	}

	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	public void drag(int movtoX, int movtoY) {
		if(pontos.isEmpty())
			return;
		
		Ponto4D p = pontos.get(pontos.size() - 1);
		p.setX(movtoX);
		p.setY(movtoY);
	}
}
