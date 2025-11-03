package joguinho.mover.bloco;

import java.io.Serializable;
import java.util.List;

/**
 * @author vitin
 */
public class EstadoJogo implements Serializable {
    
    public final List<GlobalJogo> componentes; 
    public final String pontosBranco;
    public final String pontosVermelho;

    public EstadoJogo(List<GlobalJogo> componentes, String pb, String pv) {
        this.componentes = componentes;
        this.pontosBranco = pb;
        this.pontosVermelho = pv;
    }
}