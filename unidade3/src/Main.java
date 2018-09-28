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

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	private float minX = -400.0f;
	private float maxX = 400.0f;
	private float minY = -400.0f;
	private float maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private int x = 0;
	private int y = 0;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1f, 1f, 1f, 0f);

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
		glu.gluOrtho2D(0, maxX, 0, maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		SRU();

		Mundo.getInstance().getObjetos().forEach(o -> o.desenha(gl));
		gl.glFlush();
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

		System.out.println("posMouse: " + movtoX + " / " + movtoY);

		x = e.getX();
		y = e.getY();

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		System.out.println(String.format("Moved %s - %s", arg0.getX(), arg0.getY()));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println(String.format("Clicked %s - %s", arg0.getX(), arg0.getY()));
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		System.out.println(String.format("MouseEntered %s - %s", arg0.getX(), arg0.getY()));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		System.out.println(String.format("Exited %s - %s", arg0.getX(), arg0.getY()));
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		System.out.println(String.format("Pressed %s - %s", x,y));
		Mundo.getInstance().adicionaObjeto(x, y);
		glDrawable.display();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}