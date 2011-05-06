package com.conzebit.myplan.core.msisdn;


public interface IMsisdnTypeStore {

	public MsisdnType getMsisdnType(String msisdn);
	
	public void setMsisdnType(String msisdn, MsisdnType msisdnType);
	
}
