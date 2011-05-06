package com.conzebit.myplan.core.call;

public class CallStat {

	private String text;
	
	private String value;
	
	public CallStat(String text, String value) {
		this.text = text;
		this.value = value;
	}
	
	public String getConcept() {
		return this.text;
	}
	
	public String getValue() {
		return this.value;
	}
}