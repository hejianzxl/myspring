package com.july.beans;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.july.beans.propertyeditors.CustomBooleanEditor;
import com.july.beans.propertyeditors.CustomNumberEditor;


/**
 * 加载属性编辑器
 * @author hejian
 *
 */
public class BeanPropertyEditor {
	private static final Log	logger			= LogFactory.getLog(BeanPropertyEditor.class);

	public static final Map<Class<?>, PropertyEditor>	defaultEditors	= new ConcurrentHashMap<Class<?>, PropertyEditor>();
	
	String NESTED_PROPERTY_SEPARATOR = ".";

	static {
		defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, Boolean.FALSE));
		defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, Boolean.FALSE));
		defaultEditors.put(float.class, new CustomNumberEditor(Float.class, Boolean.FALSE));
		defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, Boolean.FALSE));
		defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, Boolean.FALSE));
		defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, Boolean.FALSE));
		defaultEditors.put(short.class, new CustomNumberEditor(Short.class, Boolean.FALSE));
		defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, Boolean.FALSE));
		defaultEditors.put(long.class, new CustomNumberEditor(Long.class, Boolean.FALSE));
		defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, Boolean.FALSE));
		defaultEditors.put(double.class, new CustomNumberEditor(Double.class, Boolean.FALSE));
		defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, Boolean.FALSE));
		defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, Boolean.FALSE));
		defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, Boolean.FALSE));

		defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
		defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

		/*this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
		this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
		this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
		this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
		this.defaultEditors.put(SortedMap.class, new CustomMapEditor(SortedMap.class));*/

		/*
		 * if (this.configValueEditorsActive) { StringArrayPropertyEditor sae =
		 * new StringArrayPropertyEditor();
		 * this.defaultEditors.put(String[].class, sae);
		 * this.defaultEditors.put(short[].class, sae);
		 * this.defaultEditors.put(int[].class, sae);
		 * this.defaultEditors.put(long[].class, sae); }
		 */
	}

	private Map customEditors;

	public void registerCustomEditor(Class requiredType, String propertyPath, PropertyEditor propertyEditor) {
		doRegisterCustomEditor(requiredType, propertyPath, propertyEditor);
	}

	public void doRegisterCustomEditor(Class requiredType, String propertyName, PropertyEditor propertyEditor) {
		if (this.customEditors == null) {
			this.customEditors = new HashMap();
		}
		if (propertyName != null) {
			/*
			 * // consistency check PropertyDescriptor descriptor =
			 * getPropertyDescriptor(propertyName); if (requiredType != null &&
			 * !descriptor.getPropertyType().isAssignableFrom(requiredType)) {
			 * throw new
			 * IllegalArgumentException("Types do not match: required=" +
			 * requiredType.getName() + ", found=" +
			 * descriptor.getPropertyType()); }
			 */
			this.customEditors.put(propertyName, propertyEditor);
		} else {
			if (requiredType == null) {
				throw new IllegalArgumentException("No propertyName and no requiredType specified");
			}
			this.customEditors.put(requiredType, propertyEditor);
		}
	}

	public PropertyEditor doFindCustomEditor(Class<?> requiredType, String propertyName) {
		if (this.defaultEditors == null) {
			return null;
		}
		if (propertyName != null) {
			PropertyEditor editor = (PropertyEditor) this.defaultEditors.get(getFinalPath(propertyName));
			if (editor != null) {
				return editor;
			}
		}
		return (PropertyEditor) this.defaultEditors.get(requiredType);
	}
	
	private String getFinalPath(String nestedPath) {
		return nestedPath.substring(nestedPath.lastIndexOf(NESTED_PROPERTY_SEPARATOR) + 1);
	}
}
