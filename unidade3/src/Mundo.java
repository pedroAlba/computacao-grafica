
public class Mundo {

	private static Mundo instance = new Mundo();

	private Mundo() {
		this.camera = new Camera();
	}
	
	private Camera camera;
	
	public Camera getCamera() {
		return camera;
	}
	
	public static Mundo getInstance() {
		if (instance == null)
			instance = new Mundo();
		return instance;
	}
}
