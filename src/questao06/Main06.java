package questao06;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main06 implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	private float minX = -400.0f;
	private float maxX = 400.0f;
	private float minY = -400.0f;
	private float maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private int x = 0;
	private int y = 0;

	private int opcao = 0;
	private int qtdPontos = 10;

	private float[][] pontos = new float[4][2];

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1f, 1f, 1f, 0f);

		pontos[0][0] = -100;
		pontos[0][1] = -100;
		pontos[1][0] = -100;
		pontos[1][1] = 100;
		pontos[2][0] = 100;
		pontos[2][1] = 100;
		pontos[3][0] = 100;
		pontos[3][1] = -100;
	}

	public void SRU() {

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(-200.0f, 0.0f);
		gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();

		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(0.0f, -200.0f);
		gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(minX, maxX, minY, maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		SRU();

		linha(pontos[0][0], pontos[0][1], pontos[1][0], pontos[1][1]);
		linha(pontos[1][0], pontos[1][1], pontos[2][0], pontos[2][1]);
		linha(pontos[2][0], pontos[2][1], pontos[3][0], pontos[3][1]);

		spline();
		ponto();
		gl.glFlush();
	}

	public void ponto() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glPointSize(4.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2f(pontos[opcao][0], pontos[opcao][1]);
		gl.glEnd();
	}

	public void linha(float line1PositionX, float line1PositionY, float line2PositionX, float line2PositionY) {
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex2f(line1PositionX, line1PositionY);
		gl.glVertex2f(line2PositionX, line2PositionY);
		gl.glEnd();
	}

	public float[] interpolacao(float[] P1, float[] P2, int t) {
		float[] posicao = new float[2];
		posicao[0] = P1[0] + (P2[0] - P1[0]) * t / qtdPontos;
		posicao[1] = P1[1] + (P2[1] - P1[1]) * t / qtdPontos;
		return posicao;
	}

	public void spline() {
		float[] P1P2, P2P3, P3P4, P1P2P3, P2P3P4, P1P2P3P4;

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINE_STRIP);

		for (int i = 0; i <= qtdPontos; i++) {
			P1P2 = interpolacao(pontos[0], pontos[1], i);
			P2P3 = interpolacao(pontos[1], pontos[2], i);
			P3P4 = interpolacao(pontos[2], pontos[3], i);
			P1P2P3 = interpolacao(P1P2, P2P3, i);
			P2P3P4 = interpolacao(P2P3, P3P4, i);
			P1P2P3P4 = interpolacao(P1P2P3, P2P3P4, i);
			gl.glVertex2d(P1P2P3P4[0], P1P2P3P4[1]);
		}

		gl.glEnd();
	}

	private void move(float minX, float maxX, float minY, float maxY) {
		
		if(!validaBounds(minX, maxX, minY, maxY)) return;

		this.minX += minX;
		this.maxX += maxX;
		this.minY += minY;
		this.maxY += maxY;
	}

	private boolean validaBounds(float minX, float maxX, float minY, float maxY) {
		return !(this.minX + minX > -100) && !(this.minY + minY > -100) && !(this.minX + minX < -700) && !(this.minX + minX < -700)
				&& !(this.maxX + maxX < 100) && !(this.maxY + maxY < 100) && !(this.maxX + maxX > 700)
				&& !(this.maxY + maxY > 700);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '1':
			opcao = 0;
			break;
		case '2':
			opcao = 1;
			break;
		case '3':
			opcao = 2;
			break;
		case '4':
			opcao = 3;
			break;
		case '-':
			if (qtdPontos > 1)
				qtdPontos -= 1;
			break;
		case '+':
			qtdPontos++;
			break;
		case 'i':
			move(50f, -50f, 50f, -50f);
			break;
		case 'o':
			move(-50f, 50f, -50f, 50f);
			break;
		case 'e':
			move(50f, 50f, 0f, 0f);
			break;
		case 'd':
			move(-50f, -50f, 0f, 0f);
			break;
		case 'c':
			move(0f, 0f, -50f, -50f);
			break;
		case 'b':
			move(0f, 0f, 50f, 50f);
			break;
		}
		glDrawable.display();
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		int movtoX = e.getX() - x;
		int movtoY = e.getY() - y;
		pontos[opcao][0] += movtoX;
		pontos[opcao][1] -= movtoY;

		System.out.println("posMouse: " + movtoX + " / " + movtoY);

		x = e.getX();
		y = e.getY();

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}