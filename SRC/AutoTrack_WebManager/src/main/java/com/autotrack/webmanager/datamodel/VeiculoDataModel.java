package com.autotrack.webmanager.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.autotrack.webmanager.model.Veiculo;

public class VeiculoDataModel extends ListDataModel<Veiculo> implements
		SelectableDataModel<Veiculo> {

	public VeiculoDataModel() {
	}

	public VeiculoDataModel(List<Veiculo> veiculos) {
		super(veiculos);
	}

	@Override
	public Veiculo getRowData(String rowKey) {		
		List<Veiculo> veiculos = (List<Veiculo>) getWrappedData();

		int wrappedRowKey = new Integer(rowKey);
		for (Veiculo car : veiculos) {
			if (car.getId()==wrappedRowKey)
				return car;
		}
		return null;
	}

	@Override
	public Object getRowKey(Veiculo veiculo) {
		return veiculo.getId();
	}

}
