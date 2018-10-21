import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.media.opengl.GL;

/**
 * Classe que representa um ObjetoGrafico, armazena informações de todos os seus pontos, sua primitiva, boundingBox e cor.
 *
 */
public class ObjetoGrafico {

	GL gl;
	
	private List<Ponto4D> pontos = new ArrayList<>();
	
	private int primitiva;
	
	private BoundingBox bbox;
	
	private int r, g, b, currentColor;
	
	void desenha(GL gl) {
		desenhaLinhas(gl);
	}

	private Transformacao4D matrizObjeto = new Transformacao4D();
	/// Matrizes temporarias que sempre sao inicializadas com matriz Identidade entao podem ser "static".
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();		
//	private static Transformacao4D matrizTmpRotacaoZ = new Transformacao4D();
	private static Transformacao4D matrizGlobal = new Transformacao4D();

	public ObjetoGrafico(int primitiva) {
		this.primitiva = primitiva;
		this.bbox = new BoundingBox();
	}
	
	public ObjetoGrafico() {
		this.primitiva = GL.GL_LINE_STRIP;
		this.bbox = new BoundingBox();
	}
	/**
	 * Itera sobre todos os pontos, e inicia um desenho na tela, com base na primitiva definida na variavel {@link primitiva}
	 * @param gl
	 */
	private void desenhaLinhas(GL gl) {
		
		gl.glLineWidth(2.0f);
		
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
			gl.glBegin(primitiva);
			gl.glColor3f(r,g,b);		
			for (Ponto4D p : pontos) {
				gl.glVertex2d(p.getX(), p.getY());
			}
			
			gl.glEnd();
		gl.glPopMatrix();
	}

	public void desenhaBBox(GL gl) {
		if  (!pontos.isEmpty()) {
			bbox.desenharOpenGLBBox(gl);
		}
	}
	
	void atualizaBBox() {
		
		pontos.stream().findFirst().ifPresent(p -> bbox.atribuirBoundingBox(p));
		
		for (Ponto4D p : pontos) {
			bbox.atualizarBBox(p);			
		}
	}
	
	void adicionarPonto(double x, double y, double z, double w) {
		if(pontos.isEmpty()) {
			pontos.add(new Ponto4D(x, y, z, w));	
			bbox.atribuirBoundingBox(x, y, z, x, y, z);
		}
		pontos.add(new Ponto4D(x, y, z, w));
	}
	
	public List<Ponto4D> getPontos() {
		return pontos;
	}
	
	@Override
	public String toString() {
		return "Objeto Gráfico: " + pontos.toString() + "\n";
	}

	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	public void drag(int movtoX, int movtoY) {
		if(pontos.isEmpty())
			return;
		
		Ponto4D p = pontos.get(pontos.size() - 1);
		p.setX(movtoX);
		p.setY(movtoY);
	}

	public void mudaCor() {
		switch (currentColor) {
		case 0:
			r = 255;
			g = 0;
			b = 0;
			break;
		case 1:
			r = 0;
			g = 255;
			b = 0;
			break;
		case 2:
			r = 0;
			g = 0;
			b = 255;
			break;

		default:
			break;
		}
		
		currentColor++;
		if(currentColor > 2) {
			currentColor = 0;
		}
	}

	public Ponto4D searchClosest(int x, int y) {
		Map<Double, Ponto4D> distancias = new TreeMap();
		for (Ponto4D ponto4d : pontos) {
			distancias.put(Math.pow((x - ponto4d.getX()),2) + Math.pow((y - ponto4d.getY()),2), ponto4d);
		}
		return distancias.values().stream().findFirst().get();
	}
	
	public void atribuirGL(GL gl) {
		this.gl = gl;
	}

	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx,ty,tz);
		matrizObjeto = matrizTranslate.transformMatrix(matrizObjeto);		
	}
	
	public void escalaXYZ(double Sx,double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();		
		matrizScale.atribuirEscala(Sx,Sy,1.0);
		matrizObjeto = matrizScale.transformMatrix(matrizObjeto);
	}

	public void atribuirIdentidade() {
		matrizObjeto.atribuirIdentidade();
	}

	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.getX(),ptoFixo.getY(),ptoFixo.getZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirEscala(escala, escala, 1.0);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.getX(),ptoFixo.getY(),ptoFixo.getZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
	}
	
	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.getX(),ptoFixo.getY(),ptoFixo.getZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.getX(),ptoFixo.getY(),ptoFixo.getZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
	}

	public void exibeMatriz() {
		matrizObjeto.exibeMatriz();
	}

	
}
