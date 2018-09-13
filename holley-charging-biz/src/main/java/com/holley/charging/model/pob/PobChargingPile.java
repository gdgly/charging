package com.holley.charging.model.pob;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargeInterfaceTypeEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileInfoStatusEnum;
import com.holley.common.constants.charge.UseToTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.util.DateUtil;
import com.holley.platform.util.CacheSysHolder;

public class PobChargingPile implements Serializable {

    private static final long serialVersionUID = -283234841837665525L;

    private Integer           id;

    private String            pileCode;

    private String            pileName;

    private Integer           stationId;

    private Short             status;

    private Short             pileType;

    private Integer           pileModel;

    private Short             intfType;

    private Short             chaWay;

    private Short             comType;

    private String            comAddr;

    private String            comSubAddr;

    private Short             isApp;

    private Short             isTimeCha;

    private Short             isRationCha;

    private Short             isMoneyCha;

    private Short             isControl;

    private Short             isChaLoad;

    private Date              buildTime;

    private String            doc;

    private Short             isFee;

    private Integer           feeRule;

    private BigDecimal        chargeFee;

    private BigDecimal        parkFee;

    private BigDecimal        serviceFee;

    private String            payWay;

    private String            address;

    private Short             isLock;

    private String            lockCode;

    private String            softVersion;

    private String            hardVersion;

    private Date              updateTime;
    private Short             pileToType;
    // 充电桩型号信息
    private BusPileModel      pileModelInfo;

    // 相关费用信息
    private String            feeRuleName;
    private BigDecimal        jianFee;
    private BigDecimal        fengFee;
    private BigDecimal        pingFee;
    private BigDecimal        guFee;

    private String            feeRuleDesc;
    private String            feeRuleDetail;
    private String            chargeFeeDesc;
    private String            stationName;
    private Short             busType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode == null ? null : pileCode.trim();
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName == null ? null : pileName.trim();
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getPileType() {
        return pileType;
    }

    public void setPileType(Short pileType) {
        this.pileType = pileType;
    }

    public Integer getPileModel() {
        return pileModel;
    }

    public void setPileModel(Integer pileModel) {
        this.pileModel = pileModel;
    }

    public Short getIntfType() {
        return intfType;
    }

    public void setIntfType(Short intfType) {
        this.intfType = intfType;
    }

    public Short getChaWay() {
        return chaWay;
    }

    public void setChaWay(Short chaWay) {
        this.chaWay = chaWay;
    }

    public Short getComType() {
        return comType;
    }

    public void setComType(Short comType) {
        this.comType = comType;
    }

    public String getComAddr() {
        return comAddr;
    }

    public void setComAddr(String comAddr) {
        this.comAddr = comAddr == null ? null : comAddr.trim();
    }

    public String getComSubAddr() {
        return comSubAddr;
    }

    public void setComSubAddr(String comSubAddr) {
        this.comSubAddr = comSubAddr == null ? null : comSubAddr.trim();
    }

    public Short getIsApp() {
        return isApp;
    }

    public void setIsApp(Short isApp) {
        this.isApp = isApp;
    }

    public Short getIsTimeCha() {
        return isTimeCha;
    }

    public void setIsTimeCha(Short isTimeCha) {
        this.isTimeCha = isTimeCha;
    }

    public Short getIsRationCha() {
        return isRationCha;
    }

    public void setIsRationCha(Short isRationCha) {
        this.isRationCha = isRationCha;
    }

    public Short getIsMoneyCha() {
        return isMoneyCha;
    }

    public void setIsMoneyCha(Short isMoneyCha) {
        this.isMoneyCha = isMoneyCha;
    }

    public Short getIsControl() {
        return isControl;
    }

    public void setIsControl(Short isControl) {
        this.isControl = isControl;
    }

    public Short getIsChaLoad() {
        return isChaLoad;
    }

    public void setIsChaLoad(Short isChaLoad) {
        this.isChaLoad = isChaLoad;
    }

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc == null ? null : doc.trim();
    }

    public Short getIsFee() {
        return isFee;
    }

    public void setIsFee(Short isFee) {
        this.isFee = isFee;
    }

    public Integer getFeeRule() {
        return feeRule;
    }

    public void setFeeRule(Integer feeRule) {
        this.feeRule = feeRule;
    }

    public BigDecimal getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(BigDecimal chargeFee) {
        this.chargeFee = chargeFee;
    }

    public BigDecimal getParkFee() {
        return parkFee;
    }

    public void setParkFee(BigDecimal parkFee) {
        this.parkFee = parkFee;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay == null ? null : payWay.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Short getIsLock() {
        return isLock;
    }

    public void setIsLock(Short isLock) {
        this.isLock = isLock;
    }

    public String getLockCode() {
        return lockCode;
    }

    public void setLockCode(String lockCode) {
        this.lockCode = lockCode == null ? null : lockCode.trim();
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion == null ? null : softVersion.trim();
    }

    public String getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion == null ? null : hardVersion.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BusPileModel getPileModelInfo() {
        return pileModelInfo;
    }

    public void setPileModelInfo(BusPileModel pileModelInfo) {
        this.pileModelInfo = pileModelInfo;
    }

    public String getFeeRuleName() {
        return feeRuleName;
    }

    public void setFeeRuleName(String feeRuleName) {
        this.feeRuleName = feeRuleName;
    }

    public BigDecimal getJianFee() {
        return jianFee;
    }

    public void setJianFee(BigDecimal jianFee) {
        this.jianFee = jianFee;
    }

    public BigDecimal getFengFee() {
        return fengFee;
    }

    public void setFengFee(BigDecimal fengFee) {
        this.fengFee = fengFee;
    }

    public BigDecimal getPingFee() {
        return pingFee;
    }

    public void setPingFee(BigDecimal pingFee) {
        this.pingFee = pingFee;
    }

    public BigDecimal getGuFee() {
        return guFee;
    }

    public void setGuFee(BigDecimal guFee) {
        this.guFee = guFee;
    }

    // -------------------------desc--------------------------------

    public String getPileTypeDesc() {
        if (pileType == null) return "未知";
        String desc = ChargePowerTypeEnum.getText(pileType.intValue());
        return desc == null ? "未知" : desc;
    }

    public String getStatusDesc() {
        if (status == null) return "未知";
        String desc = PileInfoStatusEnum.getText(status.intValue());
        return desc == null ? "未知" : desc;
    }

    public String getChaWayDesc() {
        if (chaWay == null) return "未知";
        String desc = ChargeCurrentTypeEnum.getText(chaWay.intValue());
        return desc == null ? "未知" : desc;
    }

    public String getIsAppDesc() {
        if (isApp == null) return "未知";
        String desc = WhetherEnum.getText(isApp.intValue());
        return desc == null ? "未知" : desc;
    }

    public String getIsControlDesc() {
        if (isControl == null) return "";
        String desc = WhetherEnum.getText(isControl.intValue());
        return desc == null ? "" : desc;
    }

    public String getIsChaLoadDesc() {
        if (isChaLoad == null) return "";
        String desc = WhetherEnum.getText(isChaLoad.intValue());
        return desc == null ? "" : desc;
    }

    public String getPayWayDesc() {
        String result = "";
        if (payWay != null) {
            String[] ways = payWay.split(",");
            for (int i = 0; i < ways.length; i++) {
                result = result + CacheSysHolder.getSysLinkName(LinkTypeEnum.PAY_WAY.getValue(), ways[i]) + ",";
            }
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String getChaModelDesc() {
        String result = "自动,";
        if (isMoneyCha != null && WhetherEnum.YES.getShortValue().equals(isMoneyCha)) {
            result = result + "定金额,";
        }
        if (isRationCha != null && WhetherEnum.YES.getShortValue().equals(isRationCha)) {
            result = result + "定电量,";
        }
        if (isTimeCha != null && WhetherEnum.YES.getShortValue().equals(isTimeCha)) {
            result = result + "定时间";
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String getComTypeDesc() {
        String result = "";
        if (comType != null) {
            result = CacheSysHolder.getSysLinkName(LinkTypeEnum.COM_TYPE.getValue(), String.valueOf(comType));
        }
        return result;
    }

    public String getPileModelDesc() {
        if (pileModel == null) return "";
        BusPileModel record = CacheChargeHolder.selectPileModelById(pileModel);
        return record == null ? "" : record.getBrand();
    }

    public String getBuildTimeStr() {
        if (buildTime == null) return "";
        return DateUtil.DateToLongStr(buildTime);
    }

    public String getUpdateTimeStr() {
        if (updateTime == null) return "";
        return DateUtil.DateToLongStr(updateTime);
    }

    public String getIsFeeDesc() {
        String result = "未知";
        if (isFee != null) {
            switch (isFee) {
                case 1:
                    result = WhetherEnum.YES.getText();
                    break;
                case 2:
                    result = WhetherEnum.NO.getText();
                    break;
                default:
                    result = "未知";
            }
        }
        return result;
    }

    public String getIntfTypeDesc() {
        if (intfType == null) return "";
        String desc = ChargeInterfaceTypeEnum.getText(intfType.intValue());
        return desc == null ? "" : desc;
    }

    public String getFeeRuleDesc() {
        return feeRuleDesc;
    }

    public void setFeeRuleDesc(String feeRuleDesc) {
        this.feeRuleDesc = feeRuleDesc;
    }

    public String getFeeRuleDetail() {
        return feeRuleDetail;
    }

    public void setFeeRuleDetail(String feeRuleDetail) {
        this.feeRuleDetail = feeRuleDetail;
    }

    public String getChargeFeeDesc() {
        return chargeFeeDesc;
    }

    public void setChargeFeeDesc(String chargeFeeDesc) {
        this.chargeFeeDesc = chargeFeeDesc;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Short getBusType() {
        return busType;
    }

    public void setBusType(Short busType) {
        this.busType = busType;
    }

    public Short getPileToType() {
        return pileToType;
    }

    public void setPileToType(Short pileToType) {
        this.pileToType = pileToType;
    }

    public String getPileToTypeDesc() {
        return UseToTypeEnum.getText(pileToType);
    }
}
