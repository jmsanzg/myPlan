package com.conzebit.myplan.core.msisdn;


public interface IMsisdnTypeValidator {
	
	public String getCountryCode();
	
	public MsisdnType getMsisdnType(String msisdn);
}
