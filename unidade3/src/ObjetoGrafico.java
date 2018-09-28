import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class ObjetoGrafico {

	private List<Ponto4D> pontos = new ArrayList<>();
	
	private int primitiva;
	
	private BoundingBox bbox;
	
	private String color;
	
	void desenha(GL gl) {
		
		desenhaPontos(gl);
		desenhaLinhas(gl);
	}

	private void desenhaLinhas(GL gl) {
		
		gl.glLineWidth(5.0f);
		gl.glPointSize(5);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		for (Ponto4D p : pontos) {
			gl.glVertex2d(p.getX(), p.getY());
			System.out.println("desenhou linha");
		}
		gl.glEnd();		
	}

	private void desenhaPontos(GL gl) {
		gl.glLineWidth(5.0f);
		gl.glPointSize(5);
		gl.glBegin(GL.GL_POINTS);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		for (Ponto4D p : pontos) {
			gl.glVertex2d(p.getX(), p.getY());
			System.out.println("Desenhou ponto " + p.getX() + " - " + p.getY());
		}
		
		gl.glEnd();
	}
	
	void adicionarPonto(double x, double y, double z, double w) {
		pontos.add(new Ponto4D(x, y, z, w));
	}
	
	public List<Ponto4D> getPontos() {
		return pontos;
	}

	public void alteraUltimoPonto(int x, int y) {
		if(! this.pontos.isEmpty()) {
			Ponto4D p = this.pontos.get(this.pontos.size() - 1);
			p.setX(x);
			p.setY(y);			
		}
	}
}
