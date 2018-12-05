package object;

import javax.media.opengl.GL;

public class Foguete extends OBJModel{
    
    private float translacao = 0, movimento = 0.1f, posX, rotacao = 0, origem = -10.0f, angulo = 0;
    private boolean bateu;
    private float saida;
    private float speed;
    private float speedOriginal;
    
    public Foguete(GL gl, float saida, int speed) {
        super("data/12217_rocket_v1_l1", 1, gl, true);
                this.posX = 0;
                this.saida = saida;
        this.speed = speed;
        this.speedOriginal = speed;
    }
    
    public void increaseSpeed(){ 
    	speed++;
    }
    
    public void resetSpeed() {
    	speed = speedOriginal;
    }
    
    float f = -20;
        
    public void desenha(){
        gl.glPushMatrix();
        if(isBateu()){
            gl.glTranslatef(posX, 2.0f, getZ());
            gl.glRotatef(altAngulo(), 2.0f, 0.0f, 0.0f);
        } else {
            gl.glTranslatef(f,1,saida);
            f+=speed * 0.1;
            if(f > 20) {
            	f = -20;
            }
            rotacao = 90; 
            gl.glRotatef(rotacao, 0, 1.0f, 0.0f);
        }
        
        gl.glCallList(modelDispList);
        gl.glPopMatrix();
    }
    
    private float altAngulo(){
        angulo += 90;
        if(angulo > 540){
            angulo = 0;
            setBateu(false);
        }
        return angulo;
    }
    
    public float getX(){
        return posX;
    }
    
    public float getZ(){
        return origem + translacao;
    }
    
    /**
     * Tratas as "bordas" do movimento
     */
    public void movimentar(){
        if(translacao > 0 && translacao > 18){
            translacao=0;
        } else if(translacao < 0 && translacao < -18){
            translacao=0;
        } else {
            translacao+=movimento;
        }
    }
    
    public void ponto(){
        System.out.println(origem + translacao);
    }

    public boolean isBateu() {
        return bateu;
    }

    public void setBateu(boolean bateu) {
        this.bateu = bateu;
    }
    
    public float getF() {
		return f;
	}
    
    public float getMovimento() {
		return movimento;
	}
    
    public float getSaida() {
		return saida;
	}
}
