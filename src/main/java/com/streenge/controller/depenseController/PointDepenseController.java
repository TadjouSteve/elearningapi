package com.streenge.controller.depenseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.streenge.model.depense.PointDepense;
import com.streenge.service.depenseService.PointDepenseService;
import com.streenge.service.utils.URLStreenge;
import com.streenge.service.utils.formInt.FormFilter;
import com.streenge.service.utils.formOut.FormTable;

@RestController
@CrossOrigin
public class PointDepenseController {
	@Autowired
	PointDepenseService pointDepenseService;
	
	@GetMapping("/pointdepense/{idPointDepense}")
	public PointDepense getPointDepense(@PathVariable("idPointDepense") int idPointDepense) {
		return pointDepenseService.getPointDepense(idPointDepense);
	}

	@GetMapping("/pointdepenses/{userLevel}")
	public FormTable getAllPointDepense(@PathVariable("userLevel") int userLevel) {
		
		return pointDepenseService.getListPointDepeense(null, userLevel);
	}
	
	@GetMapping("/pointdepenses/{userLevel}/{filter}")
	public FormTable getAllPointDepense(@PathVariable("filter") String filter,@PathVariable("userLevel") int userLevel) {
		return pointDepenseService.getListPointDepeense(filter, userLevel);
	}
	
	
	@PostMapping("/pointdepense")
	public URLStreenge createPointDepense(@RequestBody PointDepense pointdepense) {
		pointdepense = pointDepenseService.createOrUpdatePointDepense(pointdepense, true);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/pointdepense/" + pointdepense.getId());
		urlStreenge.setData(pointdepense);
		return urlStreenge;
	} 
	
	@PutMapping("/pointdepense")
	public URLStreenge updatePointDepense(@RequestBody PointDepense pointdepense) {
		pointdepense = pointDepenseService.createOrUpdatePointDepense(pointdepense, false);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/pointdepense/" + pointdepense.getId());
		urlStreenge.setData(pointdepense);
		return urlStreenge;
	}
	
	@DeleteMapping("/pointdepense/{idPointDepense}")
	public URLStreenge deletePointDepense(@PathVariable("idPointDepense") int idPointDepense,@RequestBody FormFilter formFilter) {
		pointDepenseService.deletePointDepense(formFilter,idPointDepense);
		URLStreenge urlStreenge = new URLStreenge();
		urlStreenge.setUrl("/pointdepenses/");
		urlStreenge.setData("OK is delete");
		return urlStreenge;
	}
	
	
}

