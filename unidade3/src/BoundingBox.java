import javax.media.opengl.GL;

/**
 * Tem como objetivo representar uma BoundingBox.
 * Contêm 6 valores de "bordas", tanto postivo como positivo
 */
public final class BoundingBox {
	private double menorX;
	private double menorY;
	private double menorZ;
	private double maiorX;
	private double maiorY;
	private double maiorZ;
	private Ponto4D centro = new Ponto4D();


	public BoundingBox() {
		this(0, 0, 0, 0, 0, 0);
	}
	
	public BoundingBox(double smallerX, double smallerY, double smallerZ, double greaterX, double greaterY, double greaterZ) {
		this.menorX = smallerX;
		this.menorY = smallerY;
		this.menorZ = smallerZ;
		this.maiorX = greaterX;
		this.maiorY = greaterY;
		this.maiorZ = greaterZ;
	}
	
	public void atribuirBoundingBox(double smallerX, double smallerY, double smallerZ, double greaterX, double greaterY, double greaterZ) {
		this.menorX = smallerX;
		this.menorY = smallerY;
		this.menorZ = smallerZ;
		this.maiorX = greaterX;
		this.maiorY = greaterY;
		this.maiorZ = greaterZ;
		processarCentroBBox();
	}
		
	public void atualizarBBox(Ponto4D point) {
	    atualizarBBox(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Atribui os novos valores a BBox, fazendo algumas validações de limite máximo / minimo
	 * @param x novo valor de x
	 * @param y novo valor de y
	 * @param z novo valor de z
	 */
	public void atualizarBBox(double x, double y, double z) {
	    if (x < menorX)
	        menorX = x;
	    else {
	        if (x > maiorX) maiorX = x;
	    }
	    if (y < menorY)
	        menorY = y;
	    else {
	        if (y > maiorY) maiorY = y;
	    }
	    if (z < menorZ)
	        menorZ = z;
	    else {
	        if (z > maiorZ) maiorZ = z;
	    }
	}
	
	/**
	 * Calcula o centro, utilizando maior + menor / 2
	 */
	public void processarCentroBBox() {
	    centro.setX((maiorX + menorX)/2);
	    centro.setY((maiorY + menorY)/2);
	    centro.setZ((maiorZ + menorZ)/2);
	}

	/**
	 * Faz o desenho da BBox, com base nas propriedades já definidas na classe
	 * @param gl
	 */
	public void desenharOpenGLBBox(GL gl) {
		gl.glColor3f(1.0f, 0.0f, 0.0f);

		gl.glBegin (GL.GL_LINE_LOOP);
			gl.glVertex3d (menorX, maiorY, menorZ);
			gl.glVertex3d (maiorX, maiorY, menorZ);
			gl.glVertex3d (maiorX, menorY, menorZ);
			gl.glVertex3d (menorX, menorY, menorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (menorX, menorY, menorZ);
	    	gl.glVertex3d (menorX, menorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, menorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (maiorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, menorY, maiorZ);
	    	gl.glVertex3d (maiorX, menorY, maiorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (maiorX, menorY, menorZ);
	    	gl.glVertex3d (maiorX, maiorY, menorZ);
	    	gl.glVertex3d (maiorX, maiorY, maiorZ);
	    	gl.glVertex3d (maiorX, menorY, maiorZ);
    	gl.glEnd();
	}

	public double obterMenorX() {
		return menorX;
	}

	public double obterMenorY() {
		return menorY;
	}

	public double obterMenorZ() {
		return menorZ;
	}

	public double obterMaiorX() {
		return maiorX;
	}

	public double obterMaiorY() {
		return maiorY;
	}

	public double obterMaiorZ() {
		return maiorZ;
	}
	
	public Ponto4D obterCentro() {
		return centro;
	}

}

