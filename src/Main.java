
/// \file Exemplo_N2_Jogl_Eclipse.java
/// \brief Exemplo_N2_Jogl_Eclipse: desenha uma linha na diagonal.
/// \version $Revision: 1.0 $
/// \author Dalton Reis.
/// \date 03/05/13.
/// Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	
	private int zoom = 400;
	
	private Ponto4D pto1 = new Ponto4D(0.0, 0.0, 0.0, 1.0);
	private Ponto4D pto2 = new Ponto4D(200.0, 200.0, 0.0, 1.0);
	
	private Ponto4D pto3 = new Ponto4D(400, 400, 0, 1.0);
	private Ponto4D pto4 = new Ponto4D(200, 200, 0, 1.0);

	private Ponto4D pto5 = new Ponto4D(-10000, 10000, 0, 1.0);
	private Ponto4D pto6 = new Ponto4D(200, 200, 0, 1.0);
	
	private Ponto4D pto7 = new Ponto4D(10000, 10000, 0, 1.0);
	private Ponto4D pto8 = new Ponto4D(200, 200, 0, 1.0);
	
	
	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	// exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0f, 400.0f, 400.0f, 0.0f);

		SRU();

		// seu desenho ...
		gl.glColor3f(10.0f, 0.0f, 0.0f);
		gl.glLineWidth(3.0f);
		gl.glPointSize(10.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(pto1.obterX(), pto1.obterY());
		gl.glVertex2d(pto2.obterX(), pto2.obterY());
		gl.glEnd();
		
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glLineWidth(3.0f);
		gl.glPointSize(10.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(pto3.obterX(), pto1.obterY());
		gl.glVertex2d(pto4.obterX(), pto2.obterY());
		gl.glEnd();
		
		gl.glColor3f(0f, 255f, 0.0f);
		gl.glLineWidth(3.0f);
		gl.glPointSize(10.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(pto5.obterX(), pto5.obterY());
		gl.glVertex2d(pto6.obterX(), pto6.obterY());
		gl.glEnd();
		
		gl.glColor3f(255f, 0, 255f);
		gl.glLineWidth(3.0f);
		gl.glPointSize(10.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(pto7.obterX(), pto7.obterY());
		gl.glVertex2d(pto8.obterX(), pto8.obterY());
		gl.glEnd();
		
		gl.glColor3f(0, 255, 255f);
		gl.glLineWidth(3.0f);
		gl.glPointSize(20.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(200, 200);
		gl.glEnd();
		
		

		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(" --- keyPressed ---");
		switch (e.getKeyCode()) {
		}
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		System.out.println(" --- displayChanged ---");
	}

	public void keyReleased(KeyEvent arg0) {
		System.out.println(" --- keyReleased ---");
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println(" --- keyTyped ---");
	}

	public void SRU() {
//		gl.glDisable(gl.GL_TEXTURE_2D);
//		gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY);
//		gl.glDisable(gl.GL_LIGHTING); //TODO: [D] FixMe: check if lighting and texture is enabled

		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(-200.0f, 0.0f);
		gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(0.0f, -200.0f);
		gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}

}
