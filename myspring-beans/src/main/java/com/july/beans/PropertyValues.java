package com.july.beans;

public abstract interface PropertyValues {
	public abstract PropertyValue[] getPropertyValues();

	public abstract boolean contains(String paramString);

	public abstract PropertyValue getPropertyValue(String paramString);

	public abstract PropertyValues changesSince(PropertyValues paramPropertyValues);
}
