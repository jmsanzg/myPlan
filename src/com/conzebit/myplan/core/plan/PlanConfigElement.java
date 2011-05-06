package com.conzebit.myplan.core.plan;

public class PlanConfigElement<T> {

	private String id;
	private T value;
	private T minValue;
	private T maxValue;
	private Class<T> clazz;
	
	public PlanConfigElement(String id, T value, T minValue, T maxValue, Class<T> clazz) {
		this.id = id;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.clazz = clazz;
	}
	
	public String getId() {
		return this.id;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}

	public T getMinValue() {
		return this.minValue;
	}
	
	public T getMaxValue() {
		return this.maxValue;
	}
	
	public String getTName() {
		return this.clazz.getName();
	}
}