/// \file Ponto4D.java
/// \brief Classe que define ponto no espaco 3D
/// \version $Revision: 1.7 $

/// \class Ponto4D
/// \brief Pontos e vetores usando coordenadas homogeneas
///
/// A classe Ponto4D fornece uma forma unificada de representar objetos com pontos e vetores, facilitando as operacoes entre estes "dois" tipos de entidade,
/// juntamente com a integracao com a classe Transformacao4D. O ponto 4D homogeneo eh representado por um vector ( x , y , z, w ).
/// A coordenada W eh 0 para vetores e 1 para pontos normalizados.
public final class Ponto4D {
	private double x; /// valor X.
	private double y; /// valor Y.
	private double z; /// valor Z.
	private double w; /// valor W.

	 /// Cria o ponto (0,0,0,1).
	public Ponto4D() {
		this(0, 0, 0, 1);
	}
	
	 /// Cria o ponto (0,0,0,1).
	public Ponto4D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Ponto4D inverterSinal(Ponto4D pto) {
		pto.setX(pto.getX()*-1);
		pto.setY(pto.getY()*-1);
		pto.setZ(pto.getZ()*-1);
		return pto;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}
	
}
