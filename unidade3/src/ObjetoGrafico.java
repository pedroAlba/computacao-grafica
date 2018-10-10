import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	
	private int r, g, b, currentColor;
	
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
	
	public ObjetoGrafico() {
		this.primitiva = GL.GL_LINE_STRIP;
		this.bbox = new BoundingBox();
	}
	/**
	 * Itera sobre todos os pontos, e inicia um desenho na tela, com base na primitiva definida na variavel {@link primitiva}
	 * @param gl
	 */
	private void desenhaLinhas(GL gl) {
		
		gl.glLineWidth(2.0f);
		
		gl.glBegin(primitiva);
		gl.glColor3f(r,g,b);		
		for (Ponto4D p : pontos) {
			gl.glVertex2d(p.getX(), p.getY());
		}
		
		gl.glEnd();		
		
		if  (!pontos.isEmpty()) {
			bbox.desenharOpenGLBBox(gl);
		}
	}
	
	void atualizaBBox() {
		for (Ponto4D p : pontos) {
			bbox.atualizarBBox(p);			
		}
	}
	
	void adicionarPonto(double x, double y, double z, double w) {
		if(pontos.isEmpty()) {
			pontos.add(new Ponto4D(x, y, z, w));	
			bbox.atribuirBoundingBox(x, y, z, x, y, z);
		}
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

	public void mudaCor() {
		switch (currentColor) {
		case 0:
			r = 255;
			g = 0;
			b = 0;
			break;
		case 1:
			r = 0;
			g = 255;
			b = 0;
			break;
		case 2:
			r = 0;
			g = 0;
			b = 255;
			break;

		default:
			break;
		}
		
		currentColor++;
		if(currentColor > 2) {
			currentColor = 0;
		}
		
	}

	public Ponto4D searchClosest(int x, int y) {
		Map<Double, Ponto4D> distancias = new TreeMap();
		for (Ponto4D ponto4d : pontos) {
			distancias.put(Math.pow((x - ponto4d.getX()),2) + Math.pow((y - ponto4d.getY()),2), ponto4d);
		}
		return distancias.values().stream().findFirst().get();
	}
}
