package com.holley.charging.dcs.database;

import java.util.Date;
import java.util.List;

import com.holley.charging.dcs.dao.model.Account;
import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.dao.model.User;

public interface IDataBaseService {

    public void initDataBase();

    public List<PobPile> loadPobPiles();

    public List<PileFeeModel> loadPileFeeModel(Integer pileID);

    public boolean saveData(Object data);

    public User getUserByPhone(String phoneNum);

    public User getUserByCpuCardID(String cardNum);

    public List<PobPile> reLoadPobPiles(Date freshTime);

    public List<PileFeeModel> reLoadPileFeeModel(Date freshTime);

    public Account getAccountByCardID(String cardID);

    // ADD BY SC 2018-03-26
    public void reLoadFeeModel();

}
