import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class ObjetoGrafico {

	private List<Ponto4D> pontos = new ArrayList<>();
	
	private int primitiva;
	
	private BoundingBox bbox;
	
	private String color;
	
	void desenha(GL gl) {
		desenhaLinhas(gl);
	}

	private void desenhaLinhas(GL gl) {
		gl.glLineWidth(2.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		for (Ponto4D p : pontos) {
			gl.glVertex2d(p.getX(), p.getY());
			System.out.println("desenhou linha");
		}
		gl.glEnd();		
	}
	
	void adicionarPonto(double x, double y, double z, double w) {
		pontos.add(new Ponto4D(x, y, z, w));
	}
	
	public List<Ponto4D> getPontos() {
		return pontos;
	}
}
