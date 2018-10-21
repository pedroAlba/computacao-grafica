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
 * Classe destinada a fazer a interação com o usuário, delegando os eventos de mouse / teclado para as respectivas classes
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
	
	private boolean ehDesenho = true;
	
	private boolean poligonoAberto;
	
	public Main(Frame f) {
		this.frame = f;
	}
	
	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1f, 1f, 1f, 0f);
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glu.gluOrtho2D(0, 400, 400, 0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		if(poligonoAberto) {
			Mundo.getInstance().setPrimitiva(GL.GL_LINE_STRIP);
		} else {
			Mundo.getInstance().setPrimitiva(GL.GL_LINE_LOOP);
		}
			
		Mundo.getInstance().getObjetos().forEach(o -> o.desenha(gl));
		Mundo.getInstance().desenhaBBox(gl);
		gl.glFlush();
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
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
		
		if(e.getKeyCode() == 120) {
			System.out.println(Mundo.getInstance().getObjetos());
		}
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			Mundo.getInstance().adicionarObjeto();
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
			if(ehDesenho) { 
				move(-50f, -50f, 0f, 0f);	
			}else {
				Mundo.getInstance().deleteCurrentPoint();
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
			Mundo.getInstance().mudaCor();
			break;
		case KeyEvent.VK_M:
			ehDesenho = !ehDesenho;
			if(ehDesenho) {
				frame.setCursor(new java.awt.Cursor(Cursor.CURSOR_POINTING_HAND));
			}else {
				frame.setCursor(new java.awt.Cursor(Cursor.CURSOR_DEFAULT));
			}
			break;
		//Translação
		case KeyEvent.VK_RIGHT:
			Mundo.getInstance().translacaoXYZ(2.0,0.0,0.0);
			break;
		case KeyEvent.VK_LEFT:
			Mundo.getInstance().translacaoXYZ(-2.0,0.0,0.0);
			break;
		case KeyEvent.VK_UP:
			Mundo.getInstance().translacaoXYZ(0.0,-2.0,0.0);
			break;
		case KeyEvent.VK_DOWN:
			Mundo.getInstance().translacaoXYZ(0.0,2.0,0.0);
			break;
		//Escala
		case KeyEvent.VK_PAGE_UP:
			Mundo.getInstance().escalaXYZ(2.0,2.0);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			Mundo.getInstance().escalaXYZ(0.5,0.5);
			break;
		case KeyEvent.VK_1:
			Mundo.getInstance().escalaXYZPtoFixo(0.5, new Ponto4D(-15.0,-15.0,0.0,0.0));
			break;
			
		case KeyEvent.VK_2:
			Mundo.getInstance().escalaXYZPtoFixo(2.0, new Ponto4D(-15.0,-15.0,0.0,0.0));
			break;
			
		case KeyEvent.VK_3:
			Mundo.getInstance().rotacaoZPtoFixo(10.0, new Ponto4D(-15.0,-15.0,0.0,0.0));
			break;
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
		
		Mundo.getInstance().setupClosestPoint(e.getX(), e.getY());
		
		if(! ehDesenho) {
			
			x = e.getX();
			y = e.getY();
			
			Mundo.getInstance().dragClosestPoint(e.getX(), e.getY());
			glDrawable.display();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Mundo.getInstance().dragCurrent(e.getX(), e.getY());
		glDrawable.display();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Mundo.getInstance().setupClosestPoint(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(ehDesenho) {
			x = arg0.getX();
			y = arg0.getY();		
			Mundo.getInstance().adicionarPonto(x, y);		
			glDrawable.display();	
		}else {
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}