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
 * along with myPlan.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.core.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.eroskimovil.particulares.ESEroskiMovilContigo;
import com.conzebit.myplan.ext.es.eroskimovil.particulares.ESEroskiMovilConekta;
import com.conzebit.myplan.ext.es.eroskimovil.particulares.ESEroskiMovilContratoSin;
import com.conzebit.myplan.ext.es.eroskimovil.particulares.ESEroskiMovilHablaA6;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelJazzmovil5;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelTarifaPlana100;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelTarifaPlana200;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelTarifaPlana300;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelTarifaPlana400;
import com.conzebit.myplan.ext.es.jazztel.particulares.ESJazztelTarifaPlana500;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifa3;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifa5;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifa8;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaTotal;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaSIN300;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaSIN500;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaSIN1GB;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaMovilToday;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaLight300;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaLight500;
import com.conzebit.myplan.ext.es.masmovil.particulares.ESMasMovilTarifaLight1GB;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistar6;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistar8;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarContratoMoviles;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarContratoSimple;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarContratoTiempoLibre;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarMiGente;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarPlanazoMovilesMovistar;
import com.conzebit.myplan.ext.es.movistar.particulares.ESMovistarPlanazoTiempoLibre;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla15;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla25;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla35;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla45;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHabla75;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHablaOcio;
import com.conzebit.myplan.ext.es.movistar.particulares.habla.ESMovistarHablaOcio15;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeArdilla12;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeArdilla15;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeArdilla6;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeArdilla8;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeArdilla9;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeBasico6;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin20;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin30;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin32;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin40;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin42;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin59;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeDelfin79;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeLeon24;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeLeon25;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeLeon30;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeLeon32;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeLeon49;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangeMiniBasico;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangePanda15;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangePanda20;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangePanda22;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangePanda26;
import com.conzebit.myplan.ext.es.orange.particulares.ESOrangePinguino;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhone6;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhone7;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhoneLoboCordero;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhoneMovilonia9;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhoneMoviloniaVIP;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhonePulpo;
import com.conzebit.myplan.ext.es.pepephone.particulares.ESPepePhoneRatoncitoElefante;
import com.conzebit.myplan.ext.es.simyo.particulares.ESSimyo0y8centimos;
import com.conzebit.myplan.ext.es.simyo.particulares.ESSimyo3centimos;
import com.conzebit.myplan.ext.es.simyo.particulares.ESSimyo5centimos;
import com.conzebit.myplan.ext.es.vodafone.empresas.ESVodafoneConectaVozInternet;
import com.conzebit.myplan.ext.es.vodafone.empresas.ESVodafoneSencillo;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneAire90x124h;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneAire90x1ATodos;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneAireSimple;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneContrato1;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneDiminuto8;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneMini24h;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneMiniTodos;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafonePlana24h;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafonePlanaTodos;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneSuperPlanaMini;
import com.conzebit.myplan.ext.es.vodafone.particulares.ESVodafoneSuperTarifaPlana;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneAtL;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneAtM;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneAtS;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneAtXL;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneAtXS;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneL;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneM;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneS;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneXL;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneXS;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneXS6;
import com.conzebit.myplan.ext.es.vodafone.particulares.tallas.ESVodafoneXS8;
import com.conzebit.myplan.ext.es.yoigo.particulares.ESYoigoLaDel0;
import com.conzebit.myplan.ext.es.yoigo.particulares.ESYoigoLaDel4;
import com.conzebit.myplan.ext.es.yoigo.particulares.ESYoigoLaDel6;
import com.conzebit.myplan.ext.es.yoigo.particulares.ESYoigoLaDel8;


public class PlanService {

	private ArrayList<AbstractPlan> plans;
	
	private ArrayList<Chargeable> data;
	
	private HashMap<String, PlanOperator> processedData;
	
	private static PlanService planService = null;
	
	private PlanService() {
		this.plans = new ArrayList<AbstractPlan>();

		this.plans.add(new ESEroskiMovilContigo());
		this.plans.add(new ESEroskiMovilConekta());
		this.plans.add(new ESEroskiMovilContratoSin());
		this.plans.add(new ESEroskiMovilHablaA6());

		this.plans.add(new ESJazztelJazzmovil5());
		this.plans.add(new ESJazztelTarifaPlana100());
		this.plans.add(new ESJazztelTarifaPlana200());
		this.plans.add(new ESJazztelTarifaPlana300());
		this.plans.add(new ESJazztelTarifaPlana400());
		this.plans.add(new ESJazztelTarifaPlana500());
		
		this.plans.add(new ESMasMovilTarifa3());
		this.plans.add(new ESMasMovilTarifa5());
		this.plans.add(new ESMasMovilTarifa8());
		this.plans.add(new ESMasMovilTarifaTotal());
		this.plans.add(new ESMasMovilTarifaSIN300());
		this.plans.add(new ESMasMovilTarifaSIN500());
		this.plans.add(new ESMasMovilTarifaSIN1GB());
		this.plans.add(new ESMasMovilTarifaMovilToday());
		this.plans.add(new ESMasMovilTarifaLight300());
		this.plans.add(new ESMasMovilTarifaLight500());
		this.plans.add(new ESMasMovilTarifaLight1GB());
		
		this.plans.add(new ESMovistarContratoMoviles());
		this.plans.add(new ESMovistarContratoSimple());
		this.plans.add(new ESMovistarContratoTiempoLibre());
		this.plans.add(new ESMovistarPlanazoMovilesMovistar());
		this.plans.add(new ESMovistarPlanazoTiempoLibre());
		this.plans.add(new ESMovistar8());
		this.plans.add(new ESMovistar6());
		this.plans.add(new ESMovistarMiGente());
		this.plans.add(new ESMovistarHabla());
		this.plans.add(new ESMovistarHabla15());
		this.plans.add(new ESMovistarHabla25());
		this.plans.add(new ESMovistarHabla35());
		this.plans.add(new ESMovistarHabla45());
		this.plans.add(new ESMovistarHabla75());
		this.plans.add(new ESMovistarHablaOcio());
		this.plans.add(new ESMovistarHablaOcio15());
		
		this.plans.add(new ESOrangeBasico6());
		this.plans.add(new ESOrangeArdilla6());
		this.plans.add(new ESOrangeArdilla8());
		this.plans.add(new ESOrangeArdilla9());
		this.plans.add(new ESOrangeArdilla12());
		this.plans.add(new ESOrangeArdilla15());
		this.plans.add(new ESOrangeDelfin20());
		this.plans.add(new ESOrangeDelfin30());
		this.plans.add(new ESOrangeDelfin32());
		this.plans.add(new ESOrangeDelfin40());
		this.plans.add(new ESOrangeDelfin42());
		this.plans.add(new ESOrangeDelfin59());
		this.plans.add(new ESOrangeDelfin79());
		this.plans.add(new ESOrangeLeon24());
		this.plans.add(new ESOrangeLeon25());
		this.plans.add(new ESOrangeLeon30());
		this.plans.add(new ESOrangeLeon32());
		this.plans.add(new ESOrangeLeon49());
		this.plans.add(new ESOrangeMiniBasico());
		this.plans.add(new ESOrangePanda15());
		this.plans.add(new ESOrangePanda20());
		this.plans.add(new ESOrangePanda22());
		this.plans.add(new ESOrangePanda26());
		this.plans.add(new ESOrangePinguino());

		this.plans.add(new ESPepePhone6());
		this.plans.add(new ESPepePhone7());
		this.plans.add(new ESPepePhoneMovilonia9());
		this.plans.add(new ESPepePhoneMoviloniaVIP());
		this.plans.add(new ESPepePhonePulpo());
		this.plans.add(new ESPepePhoneRatoncitoElefante());
		this.plans.add(new ESPepePhoneLoboCordero());

		this.plans.add(new ESSimyo0y8centimos());
		this.plans.add(new ESSimyo3centimos());
		this.plans.add(new ESSimyo5centimos());

		this.plans.add(new ESVodafoneAire90x124h());
		this.plans.add(new ESVodafoneAire90x1ATodos());
		this.plans.add(new ESVodafoneAireSimple());
		this.plans.add(new ESVodafoneContrato1());
		this.plans.add(new ESVodafoneDiminuto8());
		this.plans.add(new ESVodafoneMini24h());
		this.plans.add(new ESVodafoneMiniTodos());
		this.plans.add(new ESVodafonePlana24h());
		this.plans.add(new ESVodafonePlanaTodos());
		this.plans.add(new ESVodafoneSuperPlanaMini());
		this.plans.add(new ESVodafoneSuperTarifaPlana());
		this.plans.add(new ESVodafoneL());
		this.plans.add(new ESVodafoneM());
		this.plans.add(new ESVodafoneS());
		this.plans.add(new ESVodafoneXL());
		this.plans.add(new ESVodafoneXS());
		this.plans.add(new ESVodafoneAtL());
		this.plans.add(new ESVodafoneAtM());
		this.plans.add(new ESVodafoneAtS());
		this.plans.add(new ESVodafoneAtXL());
		this.plans.add(new ESVodafoneAtXS());
		this.plans.add(new ESVodafoneXS6());
		this.plans.add(new ESVodafoneXS8());
		
		this.plans.add(new ESVodafoneConectaVozInternet());
		this.plans.add(new ESVodafoneSencillo());

		this.plans.add(new ESYoigoLaDel0());
		this.plans.add(new ESYoigoLaDel4());
		this.plans.add(new ESYoigoLaDel6());
		this.plans.add(new ESYoigoLaDel8());

		this.process(new ArrayList<Chargeable>());
	}
	
	public static PlanService getInstance() {
		if (planService == null) {
			planService = new PlanService();
		}
		return planService;
	}
	
	public void process(ArrayList<Chargeable> data) {
		this.data = data;
		process();
	}
	
	public PlanSummary process(ArrayList<Chargeable> data, String operator, String planName) {
		for (AbstractPlan plan : this.plans) {
			if (plan.getOperator().equals(operator) &&
			    plan.getPlanName().equals(planName)) {
				return plan.process(data);
			}
		}
		return null;
	}
	
	public void updateMisdnTypeAndProcess(String msisdn, MsisdnType msisdnType) {
		// TODO if Contact is the same instance then remove this loop
		for (Chargeable chargeable : this.data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				if (msisdn.equals(call.getContact().getMsisdn())) {
					call.getContact().setMsisdnType(msisdnType);
				}
			}
		}
		process();
	}
	
	private void process() {
		this.processedData = new HashMap<String, PlanOperator>();
		
		for (AbstractPlan plan : this.plans) {
			String operator = plan.getOperator();
			if (!this.processedData.containsKey(operator)) {
				this.processedData.put(operator, new PlanOperator(operator));
			}
			PlanOperator planOperator = this.processedData.get(operator);
		    PlanSummary planSummary = plan.process(this.data);
		    planOperator.addPlanSummary(planSummary);
		}
	}

	public ArrayList<PlanOperator> getOperators() {
		Collection<PlanOperator> ret = this.processedData.values();
		ArrayList<PlanOperator> ret2 = new ArrayList<PlanOperator>(ret);
		Collections.sort(ret2);
		return ret2;
	}

	public String[] getOperatorsAsStringArray() {
		ArrayList<PlanOperator> alpc = this.getOperators();
		String []ret = new String[alpc.size()];
		for (int i = 0; i < alpc.size(); i++) {
			ret[i] = alpc.get(i).getName();
		}
		return ret;
	}
	
	public ArrayList<PlanSummary> getPlanSummaries(String operator, Comparator<PlanSummary> comparator) {
		ArrayList<PlanSummary> ret2 = null;
		if (operator != null) {
			Collection<PlanSummary> ret = this.processedData.get(operator).getPlanSummaryList();
			ret2 = new ArrayList<PlanSummary>(ret);
		} else {
			ret2 = new ArrayList<PlanSummary>();
			for (PlanOperator planOperator : getOperators()) {
				ret2.addAll(planOperator.getPlanSummaryList());
			}
		}
		
		if (comparator == null) {
			Collections.sort(ret2);
		} else {
			Collections.sort(ret2, comparator);
		}
		
		return ret2;
	}

	public PlanConfig getPlanConfig(String operator, String planName) {
		for (AbstractPlan plan : this.plans) {
			if (plan.getOperator().equals(operator) && plan.getPlanName().equals(planName)) {
				return plan.getPlanConfig();
			}
		}
		return null;
	}
	
	public PlanSummary getPlanSummary(String operator, String planName) {
		return this.processedData.get(operator).getPlanSummary(planName);
	}
	
	
	public ArrayList<PlanChargeable> getPlanCalls(String operator, String planName) {
		return this.getPlanSummary(operator, planName).getPlanCalls();
	}
	
	public ArrayList<AbstractPlan> getPlans() {
		return this.plans;
	}
	
	public String[] getPlansAsStringArray() {
		String []ret = new String[this.plans.size()];
		for (int i = 0; i < plans.size(); i++) {
			ret[i] = plans.get(i).getPlanName();
		}
		return ret;
	}

	public String[] getPlansAsStringArray(String operator) {
		ArrayList<String> aux = new ArrayList<String>();
		for (AbstractPlan plan : this.plans) {
			if (operator.equals(plan.getOperator())) {
				aux.add(plan.getPlanName());
			}
		}
		Collections.sort(aux);
		String []ret = new String[aux.size()];
		for (int i = 0; i < aux.size(); i++) {
			ret[i] = aux.get(i);
		}
		return ret;
	}

	public void setUserConfig(HashMap<String, Object> allConfig) {
	    for (AbstractPlan plan : this.plans) {
	    	PlanConfig planConfig = plan.getPlanConfig();
	    	if (planConfig != null) {
	    		planConfig.setUserConfig(allConfig);
	    	}
	    }
	    
    }
}