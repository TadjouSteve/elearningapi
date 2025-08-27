package com.streenge.controller.contactController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.contact.Client;
import com.streenge.service.contactService.ClientService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormCreateContact;
import com.streenge.service.utils.formOut.FormClient;
import com.streenge.service.utils.formOut.FormTable;

@CrossOrigin
@RestController
public class ClientController {
	@Autowired
	private ClientService clientService;

	/** 
	 * Create - Add a new client
	 * 
	 * @param cleint An object client
	 * @return The client object saved
	 */
	@PostMapping("/client")
	public URLStreenge createFournisseur(@RequestBody FormCreateContact form) {
		URLStreenge urlStreenge=new URLStreenge();
		Client client=clientService.createClient(form);
				
		if (client!=null) {
			urlStreenge.setData(client.getNom());
			urlStreenge.setUrl("/client/"+client.getIdClient());
		}
		return urlStreenge;
	}
	
	@PutMapping("/client")
	public URLStreenge alterFournisseur(@RequestBody FormCreateContact form) {
		URLStreenge urlStreenge=new URLStreenge();
		Client client=clientService.alterClient(form);
				
		if (client!=null) {
			urlStreenge.setData(client.getNom());
			urlStreenge.setUrl("/client/"+client.getIdClient());
		}
		return urlStreenge;
	}
	
	@GetMapping("/clients")
	public FormTable getClients(){
		return clientService.getAllClient(null);
	}
	
	@GetMapping("/clients/{filter}")
	public FormTable getClientsFilter(@PathVariable("filter") String filter){
		return clientService.getAllClient(filter);
	}
	
	
	@GetMapping("/client/{id}") 
	public FormClient geFormContact(@PathVariable("id") String id) {
		return clientService.getClient(id);
	}

}
