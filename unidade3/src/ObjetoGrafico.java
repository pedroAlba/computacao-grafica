import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import javax.media.opengl.GL;

/**
 * Classe que representa um ObjetoGrafico, armazena informações de todos os seus pontos, sua primitiva, boundingBox e cor.
 *
 */
public class ObjetoGrafico {

	private final String OID = UUID.randomUUID().toString();
	
	private List<Ponto4D> pontos = new ArrayList<>();
	private List<ObjetoGrafico> filhos = new ArrayList<ObjetoGrafico>();
	
	private int primitiva;	
	private BoundingBox bbox;
	
	private int r, g, b, currentColor;
	
	private Transformacao4D matrizObjeto = new Transformacao4D();

	/**
	 * Variável de controle para saber se o objeto sofre alguma transformacao
	 */
	private boolean transformou = false;
	
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();		
	private static Transformacao4D matrizGlobal = new Transformacao4D();

	
	public ObjetoGrafico(int primitiva) {
		this.primitiva = primitiva;
		this.bbox = new BoundingBox();
		filhos = new ArrayList<>();
	}

	void desenha(GL gl) {
		desenhaLinhas(gl);
	}

	public ObjetoGrafico() {
		this.primitiva = GL.GL_LINE_STRIP;
		this.bbox = new BoundingBox();
		filhos = new ArrayList<>();
	}
	/**
	 * Itera sobre todos os pontos, e inicia um desenho na tela, com base na primitiva definida na variavel {@link primitiva}
	 * @param gl
	 */
	private void desenhaLinhas(GL gl) {
		gl.glLineWidth(2.0f);
		gl.glPushMatrix();		
		{
			gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
			gl.glBegin(primitiva);
			{
				gl.glColor3f(r,g,b);		
				for (Ponto4D p : pontos) {
					gl.glVertex2d(p.getX(), p.getY());
				}	
			}
			gl.glEnd();
	
		}
		getFilhos().forEach(o -> o.desenhaLinhas(gl));
		gl.glPopMatrix();
	}

	public void desenhaBBox(GL gl) {
		if  (!pontos.isEmpty() && !transformou) {
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

	/**
	 * Procura o ponto mais próximo com base nas coordenadas passadas por parametro
	 * @param x
	 * @param y
	 * @return
	 */
	public Ponto4D searchClosest(int x, int y) {
		Map<Double, Ponto4D> distancias = new TreeMap<>();
		for (Ponto4D ponto4d : pontos) {
			distancias.put(Math.pow((x - ponto4d.getX()),2) + Math.pow((y - ponto4d.getY()),2), ponto4d);
		}
		return distancias.values().stream().findFirst().get();
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		transformou = true;
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx,ty,tz);
		matrizObjeto = matrizTranslate.transformMatrix(matrizObjeto);		
	}

	public void escalaXYZ(double Sx,double Sy) {
		transformou = true;
		Transformacao4D matrizScale = new Transformacao4D();		
		matrizScale.atribuirEscala(Sx,Sy,1.0);
		matrizObjeto = matrizScale.transformMatrix(matrizObjeto);
	}

	public void atribuirIdentidade() {
		matrizObjeto.atribuirIdentidade();
	}

	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		transformou = true;
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
		transformou = true;
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

	public boolean isInside(int x, int y) {
		
		if(transformou) return false;
		
		return insideBBox(x, y) ? scanLine(x,y) : false;
	}

	/**
	 * Verifica se as cordenadas X e Y estão dentro da Bbox
	 * @param x eixo X
	 * @param y eixo Y
	 * @return {@code true} se o ponto está dentro da BBox
	 */
	private boolean insideBBox(int x, int y) {
		return (x >= bbox.obterMenorX() && x <= bbox.obterMaiorX())
				&&
			   (y >= bbox.obterMenorY() && y <= bbox.obterMaiorY());
	}
	
	/**
	 * Verifica se as coordenadas X e Y estão dentro do poligono
	 * @param x eixo X
	 * @param y eixo Y
	 * @return {@code true} se o ponto está dentro do poligono
	 */
	private boolean scanLine(int x, int y)
	{
		int nCruzamentos = 0;
		
		for (int i = 0; i < pontos.size(); i++)
		{
			//pega o proximo com base em i
			int iB = (i + 1) % pontos.size();
			
			Ponto4D pontoA = pontos.get(i);
			Ponto4D pontoB = pontos.get(iB);
			
			if (pontoA.getY() != pontoB.getY())
			{
				Ponto4D interseccao = interseccao(pontoA, pontoB, y); 
				if (interseccao != null)
				{
					if (interseccao.getX() >= x && 
						interseccao.getY() >= Math.min(pontoA.getY(), pontoB.getY()) && 
						interseccao.getY() <= Math.max(pontoA.getY(), pontoB.getY()))
					{
						nCruzamentos++;
					}
				}
			}
		}
		//se for impar, true
		return !((nCruzamentos % 2) == 0);
	}
	
	/**
	 * Cálculo da Equação da Reta - Inclinação e interseção 
	 * @param p1
	 * @param p2
	 * @param y
	 * @return
	 */
	private Ponto4D interseccao(Ponto4D p1, Ponto4D p2, double y)
	{		
		double ti = (y - p1.getY()) / (p2.getY() - p1.getY());
		if (ti >= 0.0 && ti <= 1.0)
		{
			double x = p1.getX() + ((p2.getX() - p1.getX()) * ti);
			return new Ponto4D(x, y, 0, 0);
		}
		return null;
	}

	public void deletaBBox() {
		bbox.deletaBBox();
	}
	
	public boolean isTransformado() {
		return transformou;
	}
	
	public void adicionaFilho(ObjetoGrafico f) {
		System.out.printf("Adicionando filho %s ao objeto %s\n", f.getOID(), this.getOID());
		filhos.add(f);
	}
	
	public List<ObjetoGrafico> getFilhos() {
		return this.filhos;
	}
	
	/**
	 * Busca todos os filhos do objeto atual, com base nos filhos
	 * @param og
	 * @return
	 */
	public List<ObjetoGrafico> retornaDescendentes(ObjetoGrafico og){
		List<ObjetoGrafico> retorno = new ArrayList<>();
		for (ObjetoGrafico o : og.getFilhos()) {
			retorno.add(o);
			retorno.addAll(retornaDescendentes(o));
		}
		return retorno;
	}

	public String getOID() {
		return OID;
	}

	/**
	 * Procura, na lista de filhos, um objeto com o mesmo OID. Caso encontre, deleta
	 * Chamado de forma recursiva em todos os filhos.
	 * @param oid2
	 */
	public void procuraEDeleta(String oid2) {
		
		Optional<ObjetoGrafico> found = filhos.stream().filter(f -> f.getOID().equals(oid2)).findAny();
		
		if(found.isPresent()) {
			filhos.remove(found.get());
		}
		
		filhos.forEach(filho -> filho.procuraEDeleta(oid2));
	}
	

	@Override
	public String toString() {
		return OID;
	}
}
