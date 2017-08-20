package com.july.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MutablePropertyValues implements PropertyValues{
	
	private List	propertyValuesList;
	
	public MutablePropertyValues() {
		propertyValuesList = new ArrayList(10);
	}
	
	public MutablePropertyValues(PropertyValues other) {
		PropertyValue[] pvs = other.getPropertyValues();
		propertyValuesList = new ArrayList(pvs.length);
		for (int i = 0; i < pvs.length; i++)
			addPropertyValue(new PropertyValue(pvs[i].getName(), pvs[i].getValue()));
	}
	
	public MutablePropertyValues(Map map) {
		Set keys = map.keySet();
		propertyValuesList = new ArrayList(keys.size());
		Iterator itr = keys.iterator(); 
		while (itr.hasNext()) {
			String key = (String) itr.next();
			addPropertyValue(new PropertyValue(key, map.get(key)));
		}
	}
	
	public void addPropertyValue(PropertyValue pv) {
		propertyValuesList.add(pv);
	}

	public PropertyValue[] getPropertyValues() {
		return (com.july.beans.PropertyValue[]) propertyValuesList.toArray(new PropertyValue[0]);
	}

	public boolean contains(String propertyName) {
		return getPropertyValue(propertyName) != null;
	}

	public PropertyValue getPropertyValue(String propertyName) {
		for (int i = 0; i < propertyValuesList.size(); i++) {
			PropertyValue pv = (PropertyValue) propertyValuesList.get(i);
			if (pv.getName().equals(propertyName))
				return pv;
		}
		return null;
	}
	
	public void setPropertyValueAt(PropertyValue pv, int i) {
		propertyValuesList.set(i, pv);
	}

	public PropertyValues changesSince(PropertyValues old) {
		MutablePropertyValues changes = new MutablePropertyValues();
		if (old == this)
			return changes;
			
		for (int i = 0; i < this.propertyValuesList.size(); i++) {
			PropertyValue newPv = (PropertyValue) this.propertyValuesList.get(i);
			// If there wasn't an old one, add it
			PropertyValue pvOld = old.getPropertyValue(newPv.getName());
			if (pvOld == null) {
				//System.out.println("No old pv for " + newPv.getName());
				changes.addPropertyValue(newPv);
			}
			else if (!pvOld.equals(newPv)) {
				// It's changed
				//System.out.println("pv changed for " + newPv.getName());
				changes.addPropertyValue(newPv);
			}
		}
		return changes;
	}

}
