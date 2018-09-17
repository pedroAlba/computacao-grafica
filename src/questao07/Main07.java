package questao07;

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

public class Main07 implements GLEventListener, MouseListener, MouseMotionListener {

	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private int left = -20;
	private int right = 400;
	private int top = 20;
	private int bottom = -400;
	
	private int antigoX, antigoY = 0;
	
	private int Dx = 200;
	private int Dy = -200;
	
	private Point4D pto1 = new Point4D(getX(45, 150) + 200, getY(45, 150) - 200, 0.0, 1.0);
	private Point4D pto2 = new Point4D(getX(135, 150) + 200, getY(135, 150) - 200, 0.0, 1.0);
	private Point4D pto3 = new Point4D(getX(225, 150) + 200, getY(225, 150) - 200, 0.0, 1.0);
	private Point4D pto4 = new Point4D(getX(315, 150) + 200, getY(315, 150) - 200, 0.0, 1.0);
	private BoundingBox bBox = new BoundingBox(pto1.GetX(),pto1.GetY(),pto1.GetZ(),pto1.GetX(),pto1.GetY(),pto1.GetZ());
	
	private float r = 1;
	private float g = 0;
	private float b = 0;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		bBox.atualizarBBox(pto2);
		bBox.atualizarBBox(pto3);
		bBox.atualizarBBox(pto4);
		bBox.processarCentroBBox();
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

		//Círculo maior
		circle(200, -200, 360, 150);
		
		//Círculo menor
		circle(Dx, Dy, 360, 50);
		
		gl.glColor3f(r, g, b);
		bBox.desenharOpenGLBBox(gl);
			
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glPointSize(5.0f);
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex2d(Dx, Dy);
		gl.glEnd();		 

		gl.glFlush();
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void keyTyped(KeyEvent arg0) {
		//
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

	public void circle(double posicaoX, double posicaoY, int qtdPontos, double raio) {
		int angulo = 360 / qtdPontos;
		gl.glLineWidth(1.0f);
		gl.glColor3f(0, 0, 0);
		gl.glPointSize(3.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
		for (int i = 0; i < 360;) {
			gl.glVertex2d(getX(i, raio) + posicaoX, getY(i, raio) + posicaoY);
			i = i + angulo;
		}
		gl.glEnd();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	    int movtoX = e.getX() - antigoX;
	    int movtoY = e.getY() - antigoY;

	    Dx += movtoX;
	    Dy += movtoY;
	    
	    antigoX = e.getX();
		antigoY = e.getY();

		if  (Dx <= bBox.obterMenorX() || Dx >= bBox.obterMaiorX() 
			|| Dy <= bBox.obterMenorY() || Dy >= bBox.obterMaiorY()) {
			r = 0;
			g = 1;
		} else {
			r = 1;
			g = 0;
		}
		
		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
        antigoX = e.getX();
        antigoY = e.getY();		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
