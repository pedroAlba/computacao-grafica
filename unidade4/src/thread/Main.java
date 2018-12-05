package thread;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.TextureData;

import object.Aviao;
import object.Foguete;

public class Main implements GLEventListener, KeyListener {

	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;

	private List<Foguete> foguetes = new ArrayList<>();
	private Aviao aviao;
	private boolean terminou = true;

	private int idTexture[], width, height;
	private ByteBuffer buffer[];

	private int level = 0;

	public void init(GLAutoDrawable drawable) {

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(0, 0, 0, 0);
		float posLight[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLight, 0);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);

		adicionaFoguetes();
		
		aviao = new Aviao(1.8f, gl, true, 1000.0f);
		aviao.moveInicio();

		new Movimento(this).start();
		new Desenhar(this).start();
		
		carregaTexturas();

		scheduleLevelUp();
	}
	
	/**
	 * Cria um executor que aumenta de nivel a cada 10 segundos
	 */
	private void scheduleLevelUp() {
		Runnable helloRunnable = new Runnable() {
			public void run() {
				level++;
				foguetes.forEach(Foguete::increaseSpeed);
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 10, TimeUnit.SECONDS);
	}

	private void carregaTexturas() {
		// Comandos de inicializacao para textura
		idTexture = new int[3]; // lista de identificadores de textura
		gl.glGenTextures(1, idTexture, 2); // Gera identificador de textura
		// magnificacao e minificacao
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		buffer = new ByteBuffer[2]; // buffer da imagem
		loadImage(0, "Cenario180.jpeg"); // carrega as texturas
	}

	public void loadImage(int ind, String fileName) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) { System.out.println(e);}


		width = image.getWidth();
		height = image.getHeight();
		
		// Cria textureData e carrega buffer
		TextureData td = new TextureData(0, 0, false, image);
		buffer[ind] = (ByteBuffer) td.getBuffer();
	}

	private void adicionaFoguetes() {
		foguetes.add(new Foguete(gl,  -2, getRandomBetweenRange()));
		foguetes.add(new Foguete(gl,  -1, getRandomBetweenRange()));
		foguetes.add(new Foguete(gl,  0, getRandomBetweenRange()));
		foguetes.add(new Foguete(gl,  1, getRandomBetweenRange()));
		foguetes.add(new Foguete( gl, 2, getRandomBetweenRange()));
	}

	private int getRandomBetweenRange() {
		Random r = new Random();
		return r.nextInt(5) + 1;
	}
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
		glu.gluPerspective(90, width / height, 0.1, 100); // projecao Perpectiva 1 pto fuga 3D
	}

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(5, 5, 0, 0, 0, 0, 0, 1, 0);
		gl.glEnable(GL.GL_LIGHTING);

		gl.glEnable(GL.GL_LIGHTING);
		gl.glPushMatrix();
		aviao.desenha();
		gl.glPopMatrix();
		gl.glDisable(GL.GL_LIGHTING);

		desenhaTextura();

		if (terminou) {
			if (foguetes.isEmpty())
				adicionaFoguetes();
			for (Foguete fog : foguetes) {
				fog.desenha();
			}
		}

		aviao.desenha();

		gl.glRasterPos3f(0, 4.0f, 0.5f);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "LEVEL " + level);

		gl.glFlush();
	}

	private void desenhaTextura() {
		
		gl.glPushMatrix();
		//Habilita textura
		gl.glEnable(GL.GL_TEXTURE_2D);
		//Faz o bind ao id escolhido
		gl.glBindTexture(GL.GL_TEXTURE_2D, idTexture[0]);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, width, height, 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, buffer[0]);
		
		desenhaCenario(0, 0, 0);

		gl.glDisable(GL.GL_TEXTURE_2D); 
		gl.glPopMatrix();
	}

	public void desenhaCenario(float posX, float posY, float posZ) {
		gl.glPushMatrix();
		gl.glTranslatef(5.0f, -10.0f, -40.0f);
		gl.glScalef(50.0f, 1.0f, 75.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_QUADS);

		//Qual a face
		gl.glNormal3f(0.0f, 1.0f, 0.0f);

		// coordenadas do quadrado de 1 face
		
		//x y projecao textura
		gl.glTexCoord2f(0.0f, 1.0f);		gl.glVertex3f(-1.0f, -5.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 0.0f);		gl.glVertex3f(-1.0f, -5.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);		gl.glVertex3f(1.0f, -5.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);		gl.glVertex3f(1.0f, -5.0f, -1.0f);

		gl.glEnd();
		gl.glPopMatrix();
	}

	public void keyPressed(KeyEvent e) {}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(1);
			break;
		case KeyEvent.VK_RIGHT:
			if (!aviao.isAtropelado()) {
				aviao.movimentoRight();
			}
			break;
		case KeyEvent.VK_LEFT:
			if (!aviao.isAtropelado()) {
				aviao.movimentoLeft();
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {}
	

	public GL getGl() {
		return gl;
	}

	public void setGl(GL gl) {
		this.gl = gl;
	}

	public GLU getGlu() {
		return glu;
	}

	public void setGlu(GLU glu) {
		this.glu = glu;
	}

	public GLAutoDrawable getGlDrawable() {
		return glDrawable;
	}

	public void setGlDrawable(GLAutoDrawable glDrawable) {
		this.glDrawable = glDrawable;
	}

	public List<Foguete> getFoguetes() {
		return foguetes;
	}

	public void setFoguetes(List<Foguete> foguetes) {
		this.foguetes = foguetes;
	}

	public Aviao getAviao() {
		return aviao;
	}

	public void setAviao(Aviao aviao) {
		this.aviao = aviao;
	}

	public boolean isTerminou() {
		return terminou;
	}

	public void setTerminou(boolean terminou) {
		this.terminou = terminou;
	}

	public void setLevel(int i) {
		this.level = i;
		this.foguetes.forEach(Foguete::resetSpeed);
	}
}
