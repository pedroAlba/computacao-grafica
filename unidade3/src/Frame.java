import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Classe principal, que contem o frame para a aplicação gráfica
 *
 */
public class Frame extends JFrame{

	private static final long serialVersionUID = 1L;
	private Main renderer = new Main(this);
	
	public Frame() {	
		// Cria o frame.
		super("CG-N2_BemVindo");   
		setBounds(300,250,416,439);  // 400 + 22 da borda do titulo da janela
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8); 

		GLCanvas canvas = new GLCanvas(glCaps);
		add(canvas,BorderLayout.CENTER);
		canvas.addGLEventListener(renderer);        
		canvas.addKeyListener(renderer);
		canvas.addMouseListener(renderer);
		canvas.addMouseMotionListener(renderer);	
		canvas.requestFocus();			
	}		
	
	public static void main(String[] args) {
		new Frame().setVisible(true);
	}
}
