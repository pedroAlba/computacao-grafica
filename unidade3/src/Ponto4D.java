public final class Ponto4D {
	private double x; 
	private double y;
	private double z;
	private double w;

	public Ponto4D() {
		this(0, 0, 0, 1);
	}
	
	public Ponto4D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Ponto4D(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
		this.w = 0;
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
	
	@Override
	public String toString() {
		return String.format("\nX: %s Y: %s", x, y);
	}
}
