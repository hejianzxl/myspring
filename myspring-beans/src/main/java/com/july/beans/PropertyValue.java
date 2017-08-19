package com.july.beans;

public class PropertyValue {
	private String	name;
	private Object	value;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public String toString() {
		return "PropertyValue: name='" + this.name + "'; value=[" + this.value + "]";
	}

	public boolean equals(Object other) {
		if (!(other instanceof PropertyValue))
			return false;
		PropertyValue pvOther = (PropertyValue) other;
		return (this == other) || ((this.name == pvOther.name) && (this.value == pvOther.value));
	}
}
