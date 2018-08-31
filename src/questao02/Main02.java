package questao02;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main02 implements GLEventListener, KeyListener {

	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private int left = -400;
	private int right = 400;
	private int top = 400;
	private int bottom = -400;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void display(GLAutoDrawable arg0) {
		System.out.println("DISPLAY");
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(left, right, top, bottom);

		SRU();

		gl.glColor3f(0, 0, 0);
		gl.glLineWidth(1.0f);
		gl.glPointSize(10.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(0, 0);
		gl.glEnd();

		for (int i = 0; i < 360;) {
			gl.glColor3f(0, 0, 255);
			gl.glLineWidth(1.0f);
			gl.glPointSize(3.0f);
			gl.glBegin(GL.GL_POINTS);
			gl.glVertex2d(getX(i, 100), getY(i, 100));
			gl.glEnd();
			i = i + 5;
		}

		gl.glFlush();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void keyTyped(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case 'o':
			right += 10;
			left -= 10;
			bottom -= 10;
			top += 10;
			glDrawable.display();
			break;
		case 'i':
			right -= 10;
			left += 10;
			bottom += 10;
			top -= 10;
			glDrawable.display();
			break;
		case 'e':
			System.out.println(left);
			if (left < -200) {
				right += 10;
				left += 10;
			} else {
				System.err.println("limite esquerda");
			}

			glDrawable.display();
			break;
		case 'd':
			if (right > 200) {
				right -= 10;
				left -= 10;
			} else {
				System.err.println("limite direita");
			}
			glDrawable.display();
			break;
		case 'c':
			if (top < 600) {
				top += 10;
				bottom += 10;
			} else {
				System.out.println("limite cima");
			}

			glDrawable.display();
			break;
		case 'b':
			System.out.println("BOTTOM " + bottom);
			if (bottom > -600) {
				top -= 10;
				bottom -= 10;
			} else {
				System.out.println("limite baixo");
			}

			glDrawable.display();
			break;

		default:
			break;
		}
	}

	public double getX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double getY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
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

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}
}
