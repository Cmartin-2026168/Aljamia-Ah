package cc.cmartin.system.model;

public enum Script {
    ESPAÑOL, 
    CASTELLANO, 
    ARABE, 
    HEBREO; 

    public boolean esVarianteLatina() {
        return this == ESPAÑOL || this == CASTELLANO;
        
        
        
        
    }
}
