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

/**
 * Classe destinada a fazer a intera��o com o usu�rio, delegando os eventos de mouse / teclado para as respectivas classes
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

	private int x = 0;
	private int y = 0;
	
	private ObjetoGrafico selecionado;
	
	private Estado estado;
	
	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espa�o de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1f, 1f, 1f, 0f);
		
		selecionado = Mundo.getInstance().getObjetos().get(0);
		
		estado = Estado.DESENHO;
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glu.gluOrtho2D(0, 400, 400, 0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		//SRU();

		Mundo.getInstance().getObjetos().forEach(o -> o.desenha(gl));
		gl.glFlush();
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("width" + width);
		System.out.println("height" + height);
		System.out.println(" --- reshape ---");
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
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
		
		switch (e.getKeyChar()) {
		case ' ':
			selecionado.setPrimitiva(GL.GL_LINE_LOOP);
			selecionado = Mundo.getInstance().adicionarObjeto();
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

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(selecionado != null)
			selecionado.drag(e.getX(), e.getY());
		glDrawable.display();
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
		Mundo.getInstance().adicionarPonto(x, y);		
		glDrawable.display();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}