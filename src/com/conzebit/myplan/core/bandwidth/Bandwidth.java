package com.conzebit.myplan.core.bandwidth;

import com.conzebit.myplan.core.Chargeable;

public class Bandwidth extends Chargeable {

	@Override
    public int getChargeableType() {
	    return CHARGEABLE_TYPE_BANDWIDTH;
    }

}
