package object;

import javax.media.opengl.GL;

public class Aviao extends OBJModel {
    
    private float x, z, y;
    private boolean atropelado;
    private float angulo = 0;
    
    public Aviao(float sz, GL gl, boolean showDetails, float posX) {
        super("data/f-16", sz, gl, showDetails);
        x = posX;
    }
    
    public void moveInicio(){
        x = 4;
        z = 0; 
        y = 0;
    }
    
    public void desenha(){
    	
        gl.glPushMatrix();
        gl.glTranslatef(x, getY(), z);
        
        if(isAtropelado()){
            gl.glRotatef(getAtpAngulo(), 2.0f, 2.0f, 2.0f);
        } else {
            gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
        }
        gl.glCallList(modelDispList);
        gl.glPopMatrix();
    }
    
    public void movimentoLeft(){
    	if(z <= 1) {
    		z += 1;	
    	}
    }
    
    public void movimentoRight(){
    	if(z > -2) {
    		z -= 1;	
    	}
    }
    
    public void incAngulo(boolean y){
        angulo += 10.0f;
        if(y){
            setY(getY() + 0.1f);
        } else {
            setY(getY() - 0.1f);
        }
        if(x < 3.0){
            x += 0.05f;
        }
    }
    
    /**
     * Verifica se o foguete atingiu o aivao
     * @param x
     * @param zRocket
     * @return
     */
    public boolean tentaAtropelar(float x, float zRocket){
    	if(z == zRocket && (x >= 4 && x <= 6)) {
    		 setAtropelado(true);
             return true;
    	}
    	return false;
    }
    
    public boolean isAtropelado() {
        return atropelado;
    }

    public void setAtropelado(boolean atropelado) {
        this.atropelado = atropelado;
    }

    public float getAtpAngulo() {
        return angulo;
    }

    public void setAtpAngulo(float atpAngulo) {
        this.angulo = atpAngulo;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public void setX(float x) {
        this.x = x;
    }
    
    public float getX() {
        return x;
    }
}
