package ma.emsi.aitelmahjoub.tp1_aitelmahjoub_salaheddine.LLM;


import java.io.Serializable;

public record LlmInteraction(String texteRequeteJson, String texteReponseJson, String reponseTexte) implements Serializable {
    private static final long serialVersionUID = 1L;

    
    public String getTexteRequeteJson() { return texteRequeteJson; }
    public String getTexteReponseJson() { return texteReponseJson; }
    public String getReponseTexte() { return reponseTexte; }
}
