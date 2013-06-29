package com.autotrack.webmanager.control;

import java.io.Serializable;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Controller;

@Controller
public class ControllerVeiculo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -389270324531259788L;
	private MapModel simpleModel;
	
	public ControllerVeiculo() {
		simpleModel = new DefaultMapModel();  
        
        //Shared coordinates  
        LatLng coord1 = new LatLng(-3.1046, -59.9845);  
         
        //Basic marker  
        simpleModel.addOverlay(new Marker(coord1, "Palio - JW#-74$%"));   
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}
	
	

}
