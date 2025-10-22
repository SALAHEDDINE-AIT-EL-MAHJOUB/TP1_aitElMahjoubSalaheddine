package ma.emsi.aitelmahjoub.tp1_aitelmahjoub_salaheddine.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.emsi.aitelmahjoub.tp1_aitelmahjoub_salaheddine.LLM.JsonUtilPourGemini;
import ma.emsi.aitelmahjoub.tp1_aitelmahjoub_salaheddine.LLM.LlmInteraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Backing bean pour la page JSF index.xhtml.
 * Portée view pour conserver l'état de la conversation qui dure pendant plusieurs requêtes HTTP.
 */
@Named
@ViewScoped

public class Bb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private JsonUtilPourGemini jsonUtil;

    /**
     * Rôle "système" que l'on attribuera plus tard à un LLM.
     * Valeur par défaut que l'utilisateur peut modifier.
     * Possible d'écrire un nouveau rôle dans la liste déroulante.
     */
    private String roleSysteme;

    /**
     * Quand le rôle est choisi par l'utilisateur dans la liste déroulante,
     * il n'est plus possible de le modifier (voir code de la page JSF), sauf si on veut un nouveau chat.
     */
    private boolean roleSystemeChangeable = true;

    /**
     * Liste de tous les rôles de l'API prédéfinis.
     */
    private List<SelectItem> listeRolesSysteme;

    /**
     * Dernière question posée par l'utilisateur.
     */
    private String question;

    /**
     * Dernière réponse de l'API OpenAI.
     */
    private String reponse;

    /**
     * Texte JSON de la requête envoyée (exposé à la page).
     */
    private String texteRequeteJson;

    /**
     * Texte JSON de la réponse reçue (exposé à la page).
     */
    private String texteReponseJson;




    /**
     * La conversation depuis le début.
     */
    private StringBuilder conversation = new StringBuilder();





    /**
     * Contexte JSF. Utilisé pour qu'un message d'erreur s'affiche dans le formulaire.
     */
    @Inject
    private FacesContext facesContext;

    /**
     * Constructeur public obligatoire pour CDI si vous ajoutez d'autres constructeurs.
     */
    public Bb() {
    }

    public String getRoleSysteme() {
        return roleSysteme;
    }

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public boolean isRoleSystemeChangeable() {
        return roleSystemeChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    /**
     * setter indispensable pour le textarea.
     *
     * @param reponse la réponse à la question.
     */
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
    /*
    * getter et setter pour texteRequeteJson
    */
    public String getTexteRequeteJson() {

        return texteRequeteJson;
    }

    public void setTexteRequeteJson(String texteRequeteJson) {
        this.texteRequeteJson = texteRequeteJson;
    }
    /*
     * getter et setter pour texteReponseJson
     */
    public String getTexteReponseJson() {
        return texteReponseJson;
    }

    public void setTexteReponseJson(String texteReponseJson) {
        this.texteReponseJson = texteReponseJson;
    }
    /*
     * getter et setter pour isDebug
     */
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    /**
     * Flag pour activer/désactiver le mode debug (affichage des JSON).
     */
    private boolean debug = false;

    public String getConversation() {
        return conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    /**
     * Envoie la question au serveur. En attendant une vraie API LLM,
     * on réalise un traitement factice pour tester le fonctionnement.
     *
     * @return null pour rester sur la même page (même vue).
     */
    public String envoyer() {
        if (question == null || question.isBlank()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Texte question vide", "Il manque le texte de la question");
            facesContext.addMessage(null, message);
            return null;
        }
        try {
            // passer le rôle système au util JSON (évite null)
            jsonUtil.setSystemRole(this.roleSysteme);

            //  Appel à l’utilitaire JSON + API Gemini
            LlmInteraction interaction = jsonUtil.envoyerRequete(question);

            //  Met à jour les champs affichés dans la page
            this.reponse = interaction.getReponseTexte();
            this.texteRequeteJson = interaction.getTexteRequeteJson();
            this.texteReponseJson = interaction.getTexteReponseJson();

            // Si la conversation n’a pas encore commencé, ajoute le rôle système
            if (this.conversation.isEmpty()) {
                if (this.roleSysteme != null) {
                    this.reponse = roleSysteme.toUpperCase(Locale.FRENCH) + "\n" + this.reponse;
                }
                this.roleSystemeChangeable = false;
            }

            // Affiche la conversation
            afficherConversation();

        } catch (Exception e) {
            //  Gestion d’erreur : problème de connexion ou de requête
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Problème de connexion avec l'API du LLM",
                    "Problème de connexion avec l'API du LLM : " + e.getMessage()
            );
            facesContext.addMessage(null, message);
        }

        return null;

    }

    /**
     * Pour un nouveau chat : retourne "index" pour forcer la création d'une nouvelle instance du bean (nouvelle vue).
     *
     * @return "index"
     */
    public String nouveauChat() {
        return "index";
    }

    /**
     * Basculer le mode debug.
     */
    public void toggleDebug() {
        this.setDebug(!isDebug());
    }


    /**
     * Concatène la question / réponse à la conversation.
     */
    private void afficherConversation() {
        this.conversation.append("== User:\n").append(question).append("\n== Serveur:\n").append(reponse).append("\n");
    }

    /**
     * Getter exposé pour la page JSF afin d'obtenir la liste des rôles.
     *
     * @return liste de SelectItem (valeur, libellé)
     */
    public List<SelectItem> getRolesSysteme() {
        if (this.listeRolesSysteme == null) {
            this.listeRolesSysteme = new ArrayList<>();

            String role = """
                    You are a helpful assistant. You help the user to find the information they need.
                    If the user type a question, you answer it.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Assistant"));

            role = """
                    You are an interpreter. You translate from English to French and from French to English.
                    If the user type a French text, you translate it into English.
                    If the user type an English text, you translate it into French.
                    If the text contains only one to three words, give some examples of usage of these words in English.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Traducteur Anglais-Français"));

            role = """
                    Your are a travel guide. If the user type the name of a country or of a town,
                    you tell them what are the main places to visit in the country or the town
                    are you tell them the average price of a meal.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Guide touristique"));
        }
        return this.listeRolesSysteme;
    }

}