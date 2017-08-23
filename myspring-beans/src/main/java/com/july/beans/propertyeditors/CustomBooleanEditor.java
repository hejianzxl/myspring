package com.july.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

public class CustomBooleanEditor extends PropertyEditorSupport{
	private boolean allowEmpty;

	/**
	 * Create a new instance.
	 * The allowEmpty parameter states if an empty String should
	 * be allowed for parsing, i.e. get interpreted as null value.
	 * Else, an IllegalArgumentException gets thrown in that case.
	 * @param allowEmpty if empty strings should be allowed
	 */
	public CustomBooleanEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && text.trim().equals("")) {
			setValue(null);
		}
		else if (text.equalsIgnoreCase("true")) {
			setValue(Boolean.TRUE);
		}
		else if (text.equalsIgnoreCase("false")) {
			setValue(Boolean.FALSE);
		}
		else
			throw new IllegalArgumentException("Invalid Boolean value [" + text + "]");
	}
}
