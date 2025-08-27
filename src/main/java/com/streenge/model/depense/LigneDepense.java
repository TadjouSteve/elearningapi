package com.streenge.model.depense;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ligne_depense database table.
 * 
 */
@Entity
@Table(name="ligne_depense")
@NamedQuery(name="LigneDepense.findAll", query="SELECT l FROM LigneDepense l")
public class LigneDepense implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length=150)
	private String designation;

	private float montant;

	@Column(length=255)
	private String note;

	//bi-directional many-to-one association to PointDepense
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_point_depense")
	private PointDepense pointDepense;

	//bi-directional many-to-one association to FicheDepense
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_fiche_depense")
	private FicheDepense ficheDepense;

	public LigneDepense() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public float getMontant() {
		return this.montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public PointDepense getPointDepense() {
		return this.pointDepense;
	}

	public void setPointDepense(PointDepense pointDepense) {
		this.pointDepense = pointDepense;
	}

	public FicheDepense getFicheDepense() {
		return this.ficheDepense;
	}

	public void setFicheDepense(FicheDepense ficheDepense) {
		this.ficheDepense = ficheDepense;
	}

}