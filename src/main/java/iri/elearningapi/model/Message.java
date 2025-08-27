package iri.elearningapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iri.elearningapi.model.userModel.Admin;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.model.userModel.Professeur;

/*
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
*/


/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String etat;

	@Lob
	private String texte;

	//bi-directional many-to-one association to Admin
	@ManyToOne
	@JoinColumn(name="id_admin")
	private Admin admin;

	//bi-directional many-to-one association to Etudiant
	@ManyToOne
	@JoinColumn(name="id_Etudiant")
	private Etudiant etudiant;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="id_Message_parent")
	private Message message;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="message")
	private List<Message> messages;

	//bi-directional many-to-one association to Professeur
	@ManyToOne
	@JoinColumn(name="id_Professeur")
	private Professeur professeur;

	public Message() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getTexte() {
		return this.texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setMessage(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setMessage(null);

		return message;
	}

	public Professeur getProfesseur() {
		return this.professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

}