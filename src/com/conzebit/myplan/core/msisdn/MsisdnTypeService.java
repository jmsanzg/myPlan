/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
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