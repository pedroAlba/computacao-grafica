import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class ObjetoGrafico {

	private List<Ponto4D> pontos = new ArrayList<>();
	
	private int primitiva;
	
	private BoundingBox bbox;
	
	private String color;
	
	void desenha(GL gl) {
		
		gl.glLineWidth(1.0f);
		gl.glPointSize(3.0f);
		gl.glColor3f(0, 0, 0);
		
		gl.glBegin(GL.GL_LINE);
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

	public void alteraUltimoPonto(int x, int y) {
		if(! this.pontos.isEmpty()) {
			Ponto4D p = this.pontos.get(this.pontos.size() - 1);
			p.setX(x);
			p.setY(y);			
		}
	}
}
