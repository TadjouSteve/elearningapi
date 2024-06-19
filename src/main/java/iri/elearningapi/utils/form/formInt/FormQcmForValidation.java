package iri.elearningapi.utils.form.formInt;

import java.util.ArrayList;
import java.util.List;

public class FormQcmForValidation {

	private int				id				= 0;
	private List<Integer>	propositions	= new ArrayList<>();

	private int	qcmValide	= 0;
	private int	totalQcm	= 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<Integer> propositions) {
		this.propositions = propositions;
	}

	public int getQcmValide() {
		return qcmValide;
	}

	public void setQcmValide(int qcmValide) {
		this.qcmValide = qcmValide;
	}

	public int getTotalQcm() {
		return totalQcm;
	}

	public void setTotalQcm(int totalQcm) {
		this.totalQcm = totalQcm;
	}

}
