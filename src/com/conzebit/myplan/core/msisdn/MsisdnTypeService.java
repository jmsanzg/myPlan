package com.conzebit.myplan.core.msisdn;

import java.util.HashMap;

import com.conzebit.myplan.ext.es.ESMsisdnTypeValidator;
import com.conzebit.util.MsisdnUtil;


public class MsisdnTypeService {

	private static MsisdnTypeService msisdnTypeService = null;
	
	private IMsisdnTypeStore msisdnTypeStore = null;
	
	private HashMap<String, IMsisdnTypeValidator> validators = new HashMap<String, IMsisdnTypeValidator>();
	
	private MsisdnTypeService(IMsisdnTypeStore msisdnTypeStore) {
		this.msisdnTypeStore = msisdnTypeStore;
		this.addValidator(new ESMsisdnTypeValidator());
	}
	
	public static MsisdnTypeService getInstance() {
		return getInstance(null);
	}

	public static MsisdnTypeService getInstance(IMsisdnTypeStore msisdnTypeStore) {
		if (msisdnTypeService == null) {
			msisdnTypeService = new MsisdnTypeService(msisdnTypeStore);
		}
		return msisdnTypeService;
	}
	
	public void setMsisdnType(String msisdn, MsisdnType msisdnType) {
		msisdnTypeStore.setMsisdnType(msisdn, msisdnType);
	}
	
	private void addValidator(IMsisdnTypeValidator validator) {
		this.validators.put(validator.getCountryCode(), validator);
	}
	
	public MsisdnType getMsisdnType(String msisdn, String userCountry) {
		MsisdnType ret = msisdnTypeStore.getMsisdnType(msisdn);
		if (ret == null) {
			String countryCode = MsisdnUtil.getCountryCodeFromMsisdn(msisdn, userCountry);
			IMsisdnTypeValidator validator = this.validators.get(countryCode);
			ret = validator.getMsisdnType(msisdn);
			msisdnTypeStore.setMsisdnType(msisdn, ret);
		}
		
		return ret;
	}
}