package com.autotrack.webmanager.util;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import com.autotrack.webmanager.service.Menuable;




public class SelectOneMenuConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String identifier) throws ConverterException {

		if (identifier == null || identifier.trim().length() == 0)
			return null;

		try {
			return findObject(Long.parseLong(identifier), getSelectItems(context, component));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {

		try {
			if (object instanceof Menuable) {
				Menuable menuable = (Menuable) object;
				return menuable != null ? String.valueOf(menuable.getIdentifier()) : "";
			} else
				return "";
		} catch (Exception e) {
			return "";
		}
	}

	private Object findObject(long identifier, List<SelectItem> items) {

		for (SelectItem selectItem : items) {
			if (identifier == ((Menuable) selectItem.getValue()).getIdentifier())
				return selectItem.getValue();
		}

		return null;
	}

	@SuppressWarnings({ "unchecked" })
	private List<SelectItem> getSelectItems(FacesContext context, UIComponent component) {

		List<UIComponent> children = component.getChildren();

		for (UIComponent componente : children)
			if (componente.getClass().equals(UISelectItems.class))
				return (List<SelectItem>) componente.getValueExpression("value").getValue(context.getELContext());

		return null;
	}
}
