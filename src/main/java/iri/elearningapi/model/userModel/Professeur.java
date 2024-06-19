package iri.elearningapi.model.userModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import iri.elearningapi.model.Message;
import iri.elearningapi.model.courModel.ProfesseurModule;
import iri.elearningapi.model.courModel.Reponse;
import lombok.Data;
 
/*
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
*/


/**
 * The persistent class for the professeur database table.
 * 
 */

@Data
@Entity
@NamedQuery(name="Professeur.findAll", query="SELECT p FROM Professeur p")
public class Professeur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String email;

	private String etat;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_inscription")
	private Date dateInscription;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_connexion")
	private Date lastConnexion;

	private String matricule;

	@Lob
	private String nom;

	@Lob
	private String password;

	@Lob
	private String prenom;

	@Lob
	private String profession;

	@Lob
	private String telephone;

	//bi-directional many-to-one association to Message
	@JsonIgnore
	@OneToMany(mappedBy="professeur")
	private List<Message> messages;

	//bi-directional many-to-one association to ProfesseurModule
	@JsonIgnore
	@OneToMany(mappedBy="professeur")
	private List<ProfesseurModule> professeurModules;

	//bi-directional many-to-one association to Reponse
	@JsonIgnore
	@OneToMany(mappedBy="professeur")
	private List<Reponse> reponses;

	public Professeur() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Date getLastConnexion() {
		return this.lastConnexion;
	}

	public void setLastConnexion(Date lastConnexion) {
		this.lastConnexion = lastConnexion;
	}

	public String getMatricule() {
		return this.matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setProfesseur(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setProfesseur(null);

		return message;
	}

	public List<ProfesseurModule> getProfesseurModules() {
		return this.professeurModules;
	}

	public void setProfesseurModules(List<ProfesseurModule> professeurModules) {
		this.professeurModules = professeurModules;
	}

	public ProfesseurModule addProfesseurModule(ProfesseurModule professeurModule) {
		getProfesseurModules().add(professeurModule);
		professeurModule.setProfesseur(this);

		return professeurModule;
	}

	public ProfesseurModule removeProfesseurModule(ProfesseurModule professeurModule) {
		getProfesseurModules().remove(professeurModule);
		professeurModule.setProfesseur(null);

		return professeurModule;
	}

	public List<Reponse> getReponses() {
		return this.reponses;
	}

	public void setReponses(List<Reponse> reponses) {
		this.reponses = reponses;
	}

	public Reponse addRepons(Reponse repons) {
		getReponses().add(repons);
		repons.setProfesseur(this);

		return repons;
	}

	public Reponse removeRepons(Reponse repons) {
		getReponses().remove(repons);
		repons.setProfesseur(null);

		return repons;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

}
