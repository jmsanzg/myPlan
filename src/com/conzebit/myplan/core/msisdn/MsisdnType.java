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


public enum MsisdnType {
	ES_JAZZTEL ("Jazztel", true),
    ES_MOVISTAR ("Movistar", true),
    ES_PEPEPHONE ("PepePhone", true),
    ES_ORANGE ("Orange", true),
    ES_SIMYO ("Simyo", true),
    ES_VODAFONE ("Vodafone", true),
    ES_YOIGO ("Yoigo", true),
    UNKNOWN ("Móvil Desconocido", true),
    ES_LAND_LINE ("Telefono Fijo", false),
    ES_LAND_LINE_SPECIAL ("Especial (90x)", false),
    ES_SPECIAL ("Especial (091, 112)", false),
    ES_SPECIAL_ZER0 ("Coste 0", false),
    ES_INTERNATIONAL ("Internacional", false);
    
    private String name = null;
    
    private boolean mobile = false;
    
    private static String[] names;
    static {
    	names = new String[MsisdnType.values().length];
    	for (int i = 0; i < names.length; i++) {
    		names[i] = MsisdnType.values()[i].getName();
    	}
    }
    
    MsisdnType(String name, boolean mobile) {
    	this.name = name;
    	this.mobile = mobile;
    }

    public static MsisdnType fromString(String value) {
    	if (value != null) {
        	for (MsisdnType type : MsisdnType.values()) {
        		if (value.equalsIgnoreCase(type.getName())) {
        			return type;
        		}
        	}
    	}
    	return UNKNOWN;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public static String[] getNames() {
    	return names;
    }
    
    public boolean isMobile() {
    	return this.mobile;
    }
    
    public String toString() {
    	return this.name;
    }
}
