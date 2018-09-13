package com.holley.charging.server.job;

import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRebate;

public interface CalcJobService {

    void updateAccountForAccountBj(BusPayment pay, BusRebate rebate);

}
