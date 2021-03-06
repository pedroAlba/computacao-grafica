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

import com.sun.glass.ui.Cursor;

/**
 * Classe destinada a fazer a intera��o com o usu�rio, delegando os eventos de
 * mouse / teclado para as respectivas classes
 *
 */
public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	private float minX = -400;
	private float maxX = 400.0f;
	private float minY = -400;
	private float maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Frame frame;

	private int x = 0;
	private int y = 0;

	private Modo modo = Modo.DESENHO;

	private boolean poligonoAberto;

	private Mundo mundo;

	public Main(Frame f) {
		this.frame = f;
	}

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1f, 1f, 1f, 0f);
		mundo = Mundo.getInstance();
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glu.gluOrtho2D(0, 400, 400, 0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (poligonoAberto) {
			mundo.setPrimitiva(GL.GL_LINE_STRIP);
		} else {
			mundo.setPrimitiva(GL.GL_LINE_LOOP);
		}

		mundo.getObjetos().forEach(o -> o.desenha(gl));
		mundo.desenhaBBox(gl);
		
		gl.glFlush();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
	}

	private void move(float minX, float maxX, float minY, float maxY) {

		if (!validaBounds(minX, maxX, minY, maxY))
			return;

		this.minX += minX;
		this.maxX += maxX;
		this.minY += minY;
		this.maxY += maxY;
	}

	private boolean validaBounds(float minX, float maxX, float minY, float maxY) {
		return !(this.minX + minX > -100) && !(this.minY + minY > -100) && !(this.minX + minX < -700)
				&& !(this.minX + minX < -700) && !(this.maxX + maxX < 100) && !(this.maxY + maxY < 100)
				&& !(this.maxX + maxX > 700) && !(this.maxY + maxY > 700);
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 120) {
			System.out.println(mundo.getObjetos());
		}

		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			mundo.adicionarObjeto();
			break;
		case KeyEvent.VK_I:
			move(50f, -50f, 50f, -50f);
			break;
		case KeyEvent.VK_O:
			move(-50f, 50f, -50f, 50f);
			break;
		case KeyEvent.VK_E:
			move(50f, 50f, 0f, 0f);
			break;
		case KeyEvent.VK_D:
			if (modo.equals(Modo.DESENHO)) {
				move(-50f, -50f, 0f, 0f);
			} else {
				mundo.deleteCurrentPoint();
			}
			break;
		case KeyEvent.VK_C:
			move(0f, 0f, -50f, -50f);
			break;
		case KeyEvent.VK_B:
			move(0f, 0f, 50f, 50f);
			break;
		case KeyEvent.VK_P:
			
			poligonoAberto = !poligonoAberto;

			break;
		case KeyEvent.VK_T:
			mundo.mudaCor();
			break;
		case KeyEvent.VK_M:
			inverteModo();

			if (modo.equals(Modo.DESENHO)) {
				frame.setCursor(new java.awt.Cursor(Cursor.CURSOR_POINTING_HAND));
			} else {
				frame.setCursor(new java.awt.Cursor(Cursor.CURSOR_DEFAULT));
			}
			break;
		// Transla��o
		case KeyEvent.VK_RIGHT:
			mundo.translacaoXYZ(2.0, 0.0, 0.0);
			break;
		case KeyEvent.VK_LEFT:
			mundo.translacaoXYZ(-2.0, 0.0, 0.0);
			break;
		case KeyEvent.VK_UP:
			mundo.translacaoXYZ(0.0, -2.0, 0.0);
			break;
		case KeyEvent.VK_DOWN:
			mundo.translacaoXYZ(0.0, 2.0, 0.0);
			break;
		// Escala
		case KeyEvent.VK_PAGE_UP:
			mundo.escalaXYZ(2.0, 2.0);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			mundo.escalaXYZ(0.5, 0.5);
			break;
		case KeyEvent.VK_1:
			mundo.escalaXYZPtoFixo(0.5, new Ponto4D(-15.0, -15.0, 0.0, 0.0));
			break;
		case KeyEvent.VK_2:
			mundo.escalaXYZPtoFixo(2.0, new Ponto4D(-15.0, -15.0, 0.0, 0.0));
			break;
		case KeyEvent.VK_3:
			mundo.rotacaoZPtoFixo(10.0, new Ponto4D(-15.0, -15.0, 0.0, 0.0));
			break;
		case KeyEvent.VK_DELETE:
			mundo.deleteCurrent();
		}

		glDrawable.display();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		mundo.setupClosestPoint(e.getX(), e.getY());

		if (modo.equals(Modo.SELECAO)) {

			x = e.getX();
			y = e.getY();

			mundo.dragClosestPoint(e.getX(), e.getY());
			glDrawable.display();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mundo.dragCurrent(e.getX(), e.getY());
		glDrawable.display();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (modo.equals(Modo.SELECAO)) {
			mundo.setupClosestPoint(e.getX(), e.getY());
			mundo.changeSelection(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (modo.equals(Modo.DESENHO)) {
			x = arg0.getX();
			y = arg0.getY();
			mundo.adicionarPonto(x, y);
			glDrawable.display();
		} 
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	private void inverteModo() {
		if (modo.equals(Modo.DESENHO)) {
			modo = Modo.SELECAO;
		} else {
			modo = Modo.DESENHO;
		}
	}
}