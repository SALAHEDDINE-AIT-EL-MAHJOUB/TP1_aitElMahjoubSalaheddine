package ma.emsi.aitelmahjoub.tp1_aitelmahjoub_salaheddine.LLM;

import java.io.Serializable;

/**
 * Simple DTO représentant une interaction entre la requête envoyée (texte JSON),
 * la réponse brute reçue (JSON) et le texte extrait de la réponse.
 */
public class LlmInteraction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String texteRequeteJson;
    private String texteReponseJson;
    private String reponseTexte;

    // Constructeur par défaut nécessaire pour certains frameworks / sérialisation
    public LlmInteraction() {
    }

    public LlmInteraction(String texteRequeteJson, String texteReponseJson, String reponseTexte) {
        this.texteRequeteJson = texteRequeteJson;
        this.texteReponseJson = texteReponseJson;
        this.reponseTexte = reponseTexte;
    }

    public String getTexteRequeteJson() {
        return texteRequeteJson;
    }

    public void setTexteRequeteJson(String texteRequeteJson) {
        this.texteRequeteJson = texteRequeteJson;
    }

    public String getTexteReponseJson() {
        return texteReponseJson;
    }

    public void setTexteReponseJson(String texteReponseJson) {
        this.texteReponseJson = texteReponseJson;
    }

    public String getReponseTexte() {
        return reponseTexte;
    }

    public void setReponseTexte(String reponseTexte) {
        this.reponseTexte = reponseTexte;
    }

    @Override
    public String toString() {
        return "LlmInteraction{" +
                "texteRequeteJson=" + (texteRequeteJson == null ? "null" : "[json]") +
                ", texteReponseJson=" + (texteReponseJson == null ? "null" : "[json]") +
                ", reponseTexte='" + reponseTexte + '\'' +
                '}';
    }
}
