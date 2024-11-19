package iri.elearningapi.utils.elearningFunction;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.repository.userRepository.EtudiantRepository;

@Service
public class ScheduledTasks {
	@Autowired
	private EtudiantRepository etudiantRepository;
	
	
	@Scheduled(cron = "0 0 * * * ?")// Exécute toutes les heures
	public void controlSendQRO() {
		Date toDay=new Date();
		System.out.println("Tâche exécutée toutes les heures");
        for (Etudiant etudiant : etudiantRepository.findAll()) {
        	String emailBody="";
			for (EtudiantChapitre etudiantChapitre : etudiant.getEtudiantChapitres()) {
				if (Methode.calculateDaysDifference(etudiantChapitre.getDateDebut(), toDay)==3) {
					
				}
			}
		}
    }
}
