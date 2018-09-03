package questao04;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main04 implements GLEventListener, KeyListener {
	
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private int figura = GL.GL_POINTS;
	private int control = 0;
	
	
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
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-400f, 400.0f, 400.0f, -400f);

		SRU();

		gl.glLineWidth(5.0f);
		gl.glPointSize(5.0f);
		gl.glBegin(figura);
			gl.glColor3f(0,0,255);
				gl.glVertex2d(-200, 200);
			gl.glColor3f(255,0,0);
				gl.glVertex2d(-200, -200);
			gl.glColor3f(255,0,255);
				gl.glVertex2d(200, -200);
			gl.glColor3f(0,255,0);
				gl.glVertex2d(200, 200);
		gl.glEnd();


		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) {
//		System.out.println(" --- keyPressed ---");	
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
//		System.out.println(" --- displayChanged ---");
	}

	public void keyReleased(KeyEvent arg0) {
//		System.out.println(" --- keyReleased ---");
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println(" --- keyTyped ---");
		switch (arg0.getKeyChar()) {
		case ' ':
			switch (control) {
			case -1:
				figura = GL.GL_POINTS;
				glDrawable.display();
				control++;
				break;
			case 0:
				figura = GL.GL_LINES;
				glDrawable.display();
				control++;
				break;
			case 1:
				figura = GL.GL_LINE_LOOP;
				glDrawable.display();
				control++;
				break;
			case 2:
				figura = GL.GL_LINE_STRIP;
				glDrawable.display();
				control++;
				break;
			case 3:
				figura = GL.GL_TRIANGLES;
				glDrawable.display();
				control++;
				break;
			case 4:
				figura = GL.GL_TRIANGLE_FAN;
				glDrawable.display();
				control++;
				break;
			case 5:
				figura = GL.GL_TRIANGLE_STRIP;
				glDrawable.display();
				control++;
				break;
			case 6:
				figura = GL.GL_QUADS;
				glDrawable.display();
				control++;
				break;
			case 7:
				figura = GL.GL_QUAD_STRIP;
				glDrawable.display();
				control++;
				break;
			case 8:
				figura = GL.GL_POLYGON;
				glDrawable.display();
				control = -1;
				break;
			default:
				break;
			}
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
}
