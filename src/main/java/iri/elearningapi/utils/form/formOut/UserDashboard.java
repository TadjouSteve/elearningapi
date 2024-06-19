package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.List;

import iri.elearningapi.model.courModel.Module;

public class UserDashboard {

	private int	courLu				= 0;
	private int	QCMValide			= 0;
	private int	moduleAccessible	= 0;

	private int		moduleActif		= 0;
	private int		moduleTotal		= 0;
	private int		chapitreTotal	= 0;
	private int		qcmValide		= 0;
	private int		qcmTotal		= 0;
	private float	tauxReuissite	= 0;
	private int		questionPose	= 0;

	List<Module> modules = new ArrayList<>();

	public int getCourLu() {
		return courLu;
	}

	public void setCourLu(int courLu) {
		this.courLu = courLu;
	}

	public int getQCMValide() {
		return QCMValide;
	}

	public void setQCMValide(int qCMValide) {
		QCMValide = qCMValide;
		qcmValide = qCMValide;
	}

	public int getModuleAccessible() {
		return moduleAccessible;
	}

	public void setModuleAccessible(int moduleAccessible) {
		this.moduleAccessible = moduleAccessible;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public void addModule(Module module) {

		if (modules == null) {
			modules = new ArrayList<>();
		}

		if (modules.contains(module)) {
			return;
		}
		modules.add(module);
	}

	public int getModuleActif() {
		return moduleActif;
	}

	public void setModuleActif(int moduleActif) {
		this.moduleActif = moduleActif;
	}

	public int getModuleTotal() {
		return moduleTotal;
	}

	public void setModuleTotal(int moduleTotal) {
		this.moduleTotal = moduleTotal;
	}

	public int getChapitreTotal() {
		return chapitreTotal;
	}

	public void setChapitreTotal(int chapitreTotal) {
		this.chapitreTotal = chapitreTotal;
	}

	public int getQcmValide() {
		return qcmValide;
	}

	public void setQcmValide(int qcmValide) {
		this.qcmValide = qcmValide;
		QCMValide = qcmValide;
	}

	public float getTauxReuissite() {
		return tauxReuissite;
	}

	public void setTauxReuissite(float tauxReuissite) {
		this.tauxReuissite = tauxReuissite;
	}

	public int getQuestionPose() {
		return questionPose;
	}

	public void setQuestionPose(int questionPose) {
		this.questionPose = questionPose;
	}

	public int getQcmTotal() {
		return qcmTotal;
	}

	public void setQcmTotal(int qcmTotal) {
		this.qcmTotal = qcmTotal;
	}

}
