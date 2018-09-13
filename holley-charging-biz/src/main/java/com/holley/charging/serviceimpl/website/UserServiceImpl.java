package com.holley.charging.serviceimpl.website;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.common.util.AccountLogUtil;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusBussinessInfoMapper;
import com.holley.charging.dao.bus.BusBussinessRealMapper;
import com.holley.charging.dao.bus.BusCardRechargeMapper;
import com.holley.charging.dao.bus.BusChargeCardMapper;
import com.holley.charging.dao.bus.BusGroupInfoMapper;
import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.dao.bus.BusRebateMapper;
import com.holley.charging.dao.bus.BusSuggestionMapper;
import com.holley.charging.dao.bus.BusUserInfoMapper;
import com.holley.charging.dao.bus.BusUserMapper;
import com.holley.charging.dao.bus.BusUserRealMapper;
import com.holley.charging.dao.bus.BusUserRebateMapper;
import com.holley.charging.dao.bus.BusUserTokenMapper;
import com.holley.charging.dao.dcs.DcsChargerecordMapper;
import com.holley.charging.model.app.UserRealAndAccount;
import com.holley.charging.model.bms.UserRealIntro;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealExample;
import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.bus.BusChargeCard;
import com.holley.charging.model.bus.BusChargeCardExample;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;
import com.holley.charging.model.bus.BusSuggestion;
import com.holley.charging.model.bus.BusSuggestionKey;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserReal;
import com.holley.charging.model.bus.BusUserRealExample;
import com.holley.charging.model.bus.BusUserRealKey;
import com.holley.charging.model.bus.BusUserRebate;
import com.holley.charging.model.bus.BusUserRebateExample;
import com.holley.charging.model.bus.BusUserToken;
import com.holley.charging.model.dcs.DcsChargerecord;
import com.holley.charging.model.def.SubAccountModel;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.IsActiveEnum;
import com.holley.common.constants.RoleEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.AccountStatusEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.SerializeCoderUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.dao.sys.SysAccountroleMapper;
import com.holley.platform.dao.sys.SysNoticeMapper;
import com.holley.platform.model.sys.SysAccountroleExample;
import com.holley.platform.model.sys.SysAccountroleKey;
import com.holley.platform.model.sys.SysNotice;
import com.holley.platform.model.sys.SysNoticeMessage;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.ImageUtil;

public class UserServiceImpl implements UserService {

    private BusUserMapper          busUserMapper;
    private BusBussinessInfoMapper busBussinessInfoMapper;
    private BusUserInfoMapper      busUserInfoMapper;
    private BusUserRealMapper      busUserRealMapper;
    private BusAccountMapper       busAccountMapper;
    private BusBussinessRealMapper busBussinessRealMapper;
    private BusSuggestionMapper    busSuggestionMapper;
    private SysNoticeMapper        sysNoticeMapper;
    private BusUserTokenMapper     busUserTokenMapper;
    private SysAccountroleMapper   sysAccountroleMapper;
    private BusGroupInfoMapper     busGroupInfoMapper;
    private BusChargeCardMapper    busChargeCardMapper;
    private BusCardRechargeMapper  busCardRechargeMapper;
    private BusPaymentMapper       busPaymentMapper;
    private DcsChargerecordMapper  dcsChargerecordMapper;
    private BusRebateMapper        busRebateMapper;
    private BusUserRebateMapper    busUserRebateMapper;

    @Override
    public boolean insertUser(BusUser user, UserTypeEnum type, RoleTypeEnum roleType, Integer roleid, BusGroupInfo groupInfo) {

        if (type == UserTypeEnum.ENTERPRISE && roleType == RoleTypeEnum.ENTERPRISE) { // 运营商信息
            BusBussinessInfo busInfo = new BusBussinessInfo();
            busInfo.setAddTime(new Date());
            busBussinessInfoMapper.insert(busInfo);
            user.setInfoId(busInfo.getId());
        } else if (type == UserTypeEnum.PERSON || type == UserTypeEnum.COMPANY) {
            BusUserInfo userInfo = new BusUserInfo();
            busUserInfoMapper.insert(userInfo);
            user.setInfoId(userInfo.getId());
        } else if (type == UserTypeEnum.GROUP) {
            busGroupInfoMapper.insertSelective(groupInfo);
            user.setInfoId(groupInfo.getId());
        } else if (type == UserTypeEnum.PLATFORM) {

        } else if (type == UserTypeEnum.ENTERPRISE && roleType == RoleTypeEnum.ENTERPRISE_SUB) {

        } else {
            return false;
        }

        busUserMapper.insertSelective(user);

        if (type != UserTypeEnum.PLATFORM) {
            BusAccount account = new BusAccount();
            account.setUserId(user.getId());
            account.setStatus(AccountStatusEnum.ACTIVATED.getShortValue());
            account.setUpdateTime(new Date());
            busAccountMapper.insertSelective(account);
        }

        // 插入权限
        SysAccountroleKey accountRole = new SysAccountroleKey();
        accountRole.setUserid(user.getId());
        if (type == UserTypeEnum.ENTERPRISE) {
            if (roleType == RoleTypeEnum.ENTERPRISE_SUB) {
                accountRole.setRoleid(RoleEnum.ENTERPRISE_SUB.getValue());
            } else {
                accountRole.setRoleid(RoleEnum.ENTERPRISE.getValue());
            }
        } else if (type == UserTypeEnum.PERSON && user.getGroupId() == null) {
            accountRole.setRoleid(RoleEnum.PERSON.getValue());
        } else if (type == UserTypeEnum.PERSON && user.getGroupId() > 0) {
            accountRole.setRoleid(RoleEnum.GROUP_SUB.getValue());
        } else if (type == UserTypeEnum.GROUP) {
            accountRole.setRoleid(RoleEnum.GROUP.getValue());
        } else {
            accountRole.setRoleid(roleid);
        }
        RoleUtil.insertAccountRole(accountRole);
        return true;
    }

    public List<BusUser> selectUserByExample(BusUserExample emp) {
        return busUserMapper.selectByExample(emp);
    }

    public BusUser selectUserByPrimaryKey(Integer id) {
        return busUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BusUser> selectBusUserByMap(Map<String, Object> param) {
        return this.busUserMapper.selectBusUserByMap(param);
    }

    public BusUser selectUserByCache(Integer userid) {
        if (userid == null || userid == 0) return null;
        byte[] data = ChargingCacheUtil.getUser(userid.toString());
        BusUser busUser;
        if (data == null) {
            busUser = busUserMapper.selectByPrimaryKey(userid);
            if (busUser != null) {
                ChargingCacheUtil.setUser(userid.toString(), SerializeCoderUtil.serializeObj(busUser));
            }
        } else {
            busUser = (BusUser) SerializeCoderUtil.deserializeObj(data);
        }
        return busUser;
    }

    @Override
    public List<BusUser> selectUserByPage(Map<String, Object> params) {
        return busUserMapper.selectUserByPage(params);
    }

    @Override
    public BusUser selectUserByInfo(Map<String, Object> params) {
        return busUserMapper.selectUserByInfo(params);
    }

    @Override
    public List<BusUser> selectBusinessUserByInfoid(Integer infoid) {
        return busUserMapper.selectBusinessUserByInfoid(infoid);
    }

    @Override
    public int updateUserAndRole(BusUser busUser, int rebateId) {
        int row = 0;
        // 更新账号信息
        row += busUserMapper.updateByPrimaryKeySelective(busUser);

        // 更新用户角色信息
        SysAccountroleKey accountrole = sysAccountroleMapper.selectAccountRoleByUserid(busUser.getId());
        if (!accountrole.getRoleid().equals(busUser.getRoleid())) {
            SysAccountroleExample emp = new SysAccountroleExample();
            SysAccountroleExample.Criteria cr = emp.createCriteria();
            cr.andUseridEqualTo(busUser.getId());
            sysAccountroleMapper.deleteByExample(emp);

            SysAccountroleKey record = new SysAccountroleKey();
            record.setUserid(busUser.getId());
            record.setRoleid(busUser.getRoleid());
            row += sysAccountroleMapper.insert(record);
        }
        BusUserRebateExample userRebateEmp = new BusUserRebateExample();
        BusUserRebateExample.Criteria userRebateCr = userRebateEmp.createCriteria();
        if (rebateId == 0) {
            userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
            userRebateCr.andUserIdEqualTo(busUser.getId());
            int countActive = busUserRebateMapper.countByExample(userRebateEmp);
            if (countActive > 0) {
                BusUserRebate userRebateTemp = new BusUserRebate();
                userRebateTemp.setStatus(IsActiveEnum.UNACTIVE.getShortValue());
                userRebateEmp.clear();
                userRebateCr = userRebateEmp.createCriteria();
                userRebateCr.andUserIdEqualTo(busUser.getId());
                userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
                busUserRebateMapper.updateByExampleSelective(userRebateTemp, userRebateEmp);
            }
        } else if (rebateId > 0) {
            userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
            userRebateCr.andUserIdEqualTo(busUser.getId());
            userRebateCr.andRebateIdEqualTo(rebateId);
            int countActive = busUserRebateMapper.countByExample(userRebateEmp);
            if (countActive == 0) {
                BusUserRebate userRebateTemp = new BusUserRebate();
                userRebateTemp.setStatus(IsActiveEnum.UNACTIVE.getShortValue());
                userRebateEmp.clear();
                userRebateCr = userRebateEmp.createCriteria();
                userRebateCr.andUserIdEqualTo(busUser.getId());
                userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
                busUserRebateMapper.updateByExampleSelective(userRebateTemp, userRebateEmp);
                BusUserRebate newUserRebate = new BusUserRebate();
                newUserRebate.setAddTime(new Date());
                newUserRebate.setRebateId(rebateId);
                newUserRebate.setStatus(IsActiveEnum.ACTIVE.getShortValue());
                newUserRebate.setUserId(busUser.getId());
                busUserRebateMapper.insert(newUserRebate);
            }
        }
        return row;
    }

    public UserRealAndAccount selectUserRealAndAccount(Integer id) {
        return busUserMapper.selectUserRealAndAccount(id);
    }

    @Override
    public int insertUserReal(BusUserReal record) {
        return busUserRealMapper.insert(record);
    }

    @Override
    public List<BusUserReal> selectUserRealByPage(Map<String, Object> params) {
        return busUserRealMapper.selectUserRealByPage(params);
    }

    @Override
    public BusUserReal selectUserRealByPK(BusUserRealKey key) {
        return busUserRealMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<BusUserReal> selectUserRealByExample(BusUserRealExample example) {
        return busUserRealMapper.selectByExample(example);
    }

    @Override
    public String updateUserRealAndInfo(BusUserReal record, RealVerifyStatusEnum verifystatus) throws Exception {
        String msg = "success";
        if (RealVerifyStatusEnum.PASSED == verifystatus) {
            // 更新用户信息表的信息
            BusUser user = busUserMapper.selectByPrimaryKey(record.getUserId());
            if (user == null) {
                return "用户不存在";
            }
            if (record.getFront() != null) {
                String datapath = RoleUtil.selectRuleByPrimaryKey(RoleUtil.DATA_PATH);
                String imgPath = record.getFront();
                imgPath = imgPath.replaceAll(Globals.IMG_DATA_FILE + "/", "");
                File file = new File(datapath + "/" + imgPath);
                Map<String, Object> imgMap = ImageUtil.uploadImg(file, user.getInfoId(), ImgTypeEnum.USER_CARD_FRONT, datapath);
                msg = (String) imgMap.get("msg");
                if (msg.equals("success")) {
                    BusUserInfo info = new BusUserInfo();
                    info.setId(user.getInfoId());
                    info.setCardNo(record.getCardNum());
                    info.setRealName(record.getRealName());
                    info.setFront((String) imgMap.get("url"));
                    busUserInfoMapper.updateByPrimaryKeySelective(info);
                }
            }
            // 更新用户表的实名状态
            BusUser bususer = new BusUser();
            bususer.setId(user.getId());
            bususer.setRealStatus(CertificateStatusEnum.PASSED.getShortValue());
            busUserMapper.updateByPrimaryKeySelective(bususer);
            ChargingCacheUtil.removeUser(user.getId().toString());
        }

        // 更新个人实名记录表的状态
        BusUserReal userReal = new BusUserReal();
        userReal.setAddTime(record.getAddTime());
        userReal.setUserId(record.getUserId());
        userReal.setStatus(verifystatus.getShortValue());
        userReal.setRemark(record.getRemark());
        busUserRealMapper.updateByPrimaryKeySelective(userReal);
        return msg;
    }

    @Override
    public BusUserReal selectLatestUserRealByUserid(Integer userid) {
        return busUserRealMapper.selectLatestUserRealByUserid(userid);
    }

    public int updateUserByPKSelective(BusUser busUser) {
        return busUserMapper.updateByPrimaryKeySelective(busUser);
    }

    public BusBussinessInfo selectBussinessByPrimaryKey(Integer infoId) {
        return busBussinessInfoMapper.selectByPrimaryKey(infoId);
    }

    @Override
    public List<BusBussinessInfo> selectBusinessInfoByPage(Map<String, Object> params) {
        return busBussinessInfoMapper.selectBusinessInfoByPage(params);
    }

    public int updateBussinessByPrimaryKeySelective(BusBussinessInfo info) {
        return busBussinessInfoMapper.updateByPrimaryKeySelective(info);
    }

    public int insertUserInfo(BusUserInfo info) {
        return busUserInfoMapper.insertSelective(info);
    }

    public BusUserInfo selectUserInfoByPrimaryKey(Integer infoid) {
        return busUserInfoMapper.selectByPrimaryKey(infoid);
    }

    @Override
    public List<BusUserInfo> selectUserInfoByPage(Map<String, Object> params) {
        return busUserInfoMapper.selectUserInfoByPage(params);
    }

    @Override
    public BusUserInfo selectUserInfoDetail(Integer id) {
        return busUserInfoMapper.selectUserInfoDetail(id);
    }

    @Override
    public UserRealIntro selectUserRealIntro(Map<String, Object> params) {
        return busUserInfoMapper.selectUserRealIntro(params);
    }

    public int updateUserInfoByPrimaryKeySelective(BusUserInfo record) {
        return busUserInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public BusUser selectSimpleUserInfoByPrimaryKey(Integer userid) {
        return busUserMapper.selectSimpleByPrimaryKey(userid);
    }

    @Override
    public BusBussinessReal selectBusBussinessRealByExample(BusBussinessRealExample example) {
        List<BusBussinessReal> list = busBussinessRealMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int insertBusBussinessRealSelective(BusBussinessReal BusBussinessReal) {
        return busBussinessRealMapper.insertSelective(BusBussinessReal);
    }

    @Override
    public int updateBusBussinessRealByPrimaryKeySelective(BusBussinessReal busBussinessReal) {
        return busBussinessRealMapper.updateByPrimaryKeySelective(busBussinessReal);
    }

    @Override
    public void updateBusBussinessRealAndInfoAndUser(BusUser busUser, BusBussinessInfo busBussinessInfo, BusBussinessReal busBussinessReal, String dataPath) throws Exception {
        // busUserMapper.updateByPrimaryKeySelective(busUser);
        String licenceImg = busBussinessReal.getLicenceImg();
        licenceImg = dataPath + licenceImg.replaceFirst("data/", "");
        String corporateImg = busBussinessReal.getCorporateImg();
        corporateImg = dataPath + corporateImg.replaceFirst("data/", "");
        String transatorImg = busBussinessReal.getTransatorImg();
        transatorImg = dataPath + transatorImg.replaceFirst("data/", "");
        Map<String, Object> map1 = ImageUtil.uploadImg(new File(licenceImg), busBussinessInfo.getId(), ImgTypeEnum.BUSSINESS_LIC, dataPath);
        Map<String, Object> map2 = ImageUtil.uploadImg(new File(corporateImg), busBussinessInfo.getId(), ImgTypeEnum.BUSSINESS_COR, dataPath);
        Map<String, Object> map3 = ImageUtil.uploadImg(new File(transatorImg), busBussinessInfo.getId(), ImgTypeEnum.BUSSINESS_TRAN, dataPath);
        if ("success".equals(map1.get("msg")) && "success".equals(map2.get("msg")) && "success".equals(map3.get("msg"))) {
            busBussinessInfo.setLicenceImg(map1.get("url").toString());
            busBussinessInfo.setCorporateImg(map2.get("url").toString());
            busBussinessInfo.setTransatorImg(map3.get("url").toString());
            BusUser newBusUser = new BusUser();
            newBusUser.setRealStatus(busUser.getRealStatus());
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andInfoIdEqualTo(busUser.getInfoId());
            busUserMapper.updateByExampleSelective(newBusUser, emp);
            busBussinessRealMapper.updateByPrimaryKeySelective(busBussinessReal);
            busBussinessInfoMapper.updateByPrimaryKeySelective(busBussinessInfo);
        }
    }

    @Override
    public List<SubAccountModel> selectSubAccounts(Map<String, Object> param) {
        return busUserMapper.selectSubAccounts(param);
    }

    @Override
    public Map<String, Object> updateHeadImgAndUserName(WebUser webUser, String username, File headImg, String dataPath) throws Exception {
        BusUser busUser = new BusUser();
        busUser.setId(webUser.getUserId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");

        if (headImg != null) {
            map = ImageUtil.uploadImg(headImg, webUser.getUserId(), ImgTypeEnum.HEAD_IMG, dataPath);
            if ("success".equals(map.get("msg"))) {
                busUser.setHeadImg(map.get("url").toString());
            }

        }
        if (!StringUtil.isEmpty(username) && "success".equals(map.get("msg"))) {
            if (!username.equals(webUser.getUserName())) {
                BusUserExample emp = new BusUserExample();
                BusUserExample.Criteria cr = emp.createCriteria();
                cr.andUsernameEqualTo(username);
                List<BusUser> list = this.busUserMapper.selectByExample(emp);
                if (list != null && list.size() > 0) {
                    map.put("msg", "用户名：'" + username + "'已被注册！！");
                } else {
                    map.put("username", username);
                    busUser.setUsername(username);
                }

            }
        }
        if ("success".equals(map.get("msg")) && (!StringUtil.isEmpty(busUser.getUsername()) || !StringUtil.isEmpty(busUser.getHeadImg()))) {
            this.busUserMapper.updateByPrimaryKeySelective(busUser);
        }

        return map;
    }

    @Override
    public List<SysNoticeMessage> selectSysNoticeByPage(Map<String, Object> param) {
        return sysNoticeMapper.selectSysNoticeByPage(param);
    }

    @Override
    public String insertAndUpdateBusBussinessReal(BusBussinessReal busBussinessReal, File licenceImg, File corporateImg, File transatorImg, String dataPath) throws Exception {
        Map<String, Object> map1 = ImageUtil.uploadImg(licenceImg, busBussinessReal.getAddTime(), ImgTypeEnum.BUSSINESS_TEMP_LIC, dataPath);
        Map<String, Object> map2 = ImageUtil.uploadImg(corporateImg, busBussinessReal.getAddTime(), ImgTypeEnum.BUSSINESS_TEMP_COR, dataPath);
        Map<String, Object> map3 = ImageUtil.uploadImg(transatorImg, busBussinessReal.getAddTime(), ImgTypeEnum.BUSSINESS_TEMP_TRAN, dataPath);
        String msg = "success";
        if ("success".equals(map1.get("msg")) && "success".equals(map2.get("msg")) && "success".equals(map3.get("msg"))) {
            busBussinessReal.setLicenceImg(map1.get("url").toString());
            busBussinessReal.setCorporateImg(map2.get("url").toString());
            busBussinessReal.setTransatorImg(map3.get("url").toString());
            this.busBussinessRealMapper.insertSelective(busBussinessReal);
            Globals.VALID_COUNT_MAP.remove(busBussinessReal.getBusInfoId());// 删除之前的认证信息
        } else {
            msg = "提交失败！！";
        }
        return msg;
    }

    @Override
    public int updateSysNoticeByPrimaryKeySelective(SysNotice sysNotice) {
        return sysNoticeMapper.updateByPrimaryKeySelective(sysNotice);
    }

    @Override
    public SysNoticeMessage selectSysNoticeMessage(Map<String, Object> param) {
        return sysNoticeMapper.selectSysNoticeMessage(param);
    }

    @Override
    public SysNotice selectSysNoticeByPrimaryKey(Integer id) {
        return sysNoticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSuggestion(BusSuggestion record) {
        return busSuggestionMapper.insert(record);
    }

    @Override
    public int updateSuggestionByPKSelective(BusSuggestion record) {
        return busSuggestionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public BusSuggestion selectSuggestionByPK(BusSuggestionKey key) {
        return busSuggestionMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<BusSuggestion> selectSuggestionByPage(Map<String, Object> params) {
        return busSuggestionMapper.selectSuggestionByPage(params);
    }

    @Override
    public int insertUserToken(BusUserToken record) {
        return busUserTokenMapper.insert(record);
    }

    @Override
    public BusUserToken selectUserTokenByPK(Integer id) {
        return busUserTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateUserTokenByPK(BusUserToken record) {
        return busUserTokenMapper.updateByPrimaryKey(record);
    }

    @Override
    public BusUser selectBusUserByPrimaryKey(Integer id) {
        return busUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateBusUserByExampleSelective(BusUser record, BusUserExample example) {
        return busUserMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int countBusUserByExample(BusUserExample example) {
        return busUserMapper.countByExample(example);
    }

    @Override
    public BusGroupInfo selectBusGroupInfoByPrimaryKey(Integer id) {
        return busGroupInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BusUserInfo> selectUserInfoForCardByPage(Map<String, Object> params) {
        return busUserInfoMapper.selectUserInfoForCardByPage(params);
    }

    @Override
    public List<BusChargeCard> selectChargeCardByExample(BusChargeCardExample example) {
        return busChargeCardMapper.selectByExample(example);
    }

    @Override
    public int insertCardUser(BusUser user, BusUserInfo userInfo) {
        busUserInfoMapper.insert(userInfo);
        user.setInfoId(userInfo.getId());
        String pwd = SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD);
        user.setUsername(System.currentTimeMillis() + "");
        user.setPassword(pwd);
        user.setPayPassword(pwd);
        user.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
        user.setEmailStatus(CertificateStatusEnum.PASSED.getShortValue());
        user.setUserType(UserTypeEnum.PERSON.getShortValue());
        user.setRegistTime(new Date());
        user.setHeadImg(Globals.DEFAULT_HEAD_URL);
        user.setRealStatus(CertificateStatusEnum.PASSED.getShortValue());
        busUserMapper.insertSelective(user);
        // 插入账户
        BusAccount account = new BusAccount();
        account.setUserId(user.getId());
        account.setStatus(AccountStatusEnum.ACTIVATED.getShortValue());
        account.setUpdateTime(new Date());
        busAccountMapper.insertSelective(account);

        // 插入权限
        SysAccountroleKey accountRole = new SysAccountroleKey();
        accountRole.setUserid(user.getId());
        accountRole.setRoleid(RoleEnum.PERSON.getValue());
        RoleUtil.insertAccountRole(accountRole);
        // 插入优惠
        if (userInfo.getRebateId() > 0) {
            BusUserRebate newRebate = new BusUserRebate();
            newRebate.setAddTime(new Date());
            newRebate.setRebateId(userInfo.getRebateId());
            newRebate.setUserId(user.getId());
            newRebate.setStatus(IsActiveEnum.ACTIVE.getShortValue());
            this.busUserRebateMapper.insert(newRebate);
        }
        return userInfo.getId();
    }

    @Override
    public int insertChargeCardSelective(BusChargeCard record) {
        busChargeCardMapper.insertSelective(record);
        return record.getId();
    }

    @Override
    public int countChargeCardByExample(BusChargeCardExample example) {
        return busChargeCardMapper.countByExample(example);
    }

    @Override
    public int insertCardRechargeSelective(BusCardRecharge record) {
        return busCardRechargeMapper.insertSelective(record);
    }

    @Override
    public int updateChargeCardByExampleSelective(BusChargeCard record, BusChargeCardExample example) {
        return busChargeCardMapper.updateByExampleSelective(record, example);
    }

    @Override
    public List<BusCardRecharge> selectCardRechargeByPage(Map<String, Object> params) {
        return busCardRechargeMapper.selectCardRechargeByPage(params);
    }

    @Override
    public void insertAndUpdateCardRecharge(BusCardRecharge cardRecharge, BusChargeCard chargeCard) {
        busCardRechargeMapper.insertSelective(cardRecharge);
        // 插入日志
        AccountLogUtil.insertAccountLogForChargeCard(chargeCard, cardRecharge.getId(), AccountLogTypeEnum.CARD_RECHARGE, cardRecharge.getMoney(), null);
        chargeCard.setUserId(null);
        chargeCard.setFreezeMoney(null);
        busChargeCardMapper.updateByPrimaryKeySelective(chargeCard);
    }

    @Override
    public int insertAndUpdateBadCardCharge(List<String> chargerecordList) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void updateCleanBadRecord(DcsChargerecord newRecord, BusPayment newPay, BusChargeCard newChargeCard, BigDecimal operateMoney) {
        busPaymentMapper.updateByPrimaryKeySelective(newPay);
        dcsChargerecordMapper.updateByPrimaryKeySelective(newRecord);
        // 插入日志
        AccountLogUtil.insertAccountLogForChargeCard(newChargeCard, newPay.getId(), AccountLogTypeEnum.CARD_CUT_MONEY, operateMoney, null);
        newChargeCard.setUserId(null);
        newChargeCard.setFreezeMoney(null);
        busChargeCardMapper.updateByPrimaryKeySelective(newChargeCard);
    }

    // set
    public void setBusUserMapper(BusUserMapper busUserMapper) {
        this.busUserMapper = busUserMapper;
    }

    public void setBusBussinessInfoMapper(BusBussinessInfoMapper busBussinessInfoMapper) {
        this.busBussinessInfoMapper = busBussinessInfoMapper;
    }

    public void setBusUserInfoMapper(BusUserInfoMapper busUserInfoMapper) {
        this.busUserInfoMapper = busUserInfoMapper;
    }

    public void setBusAccountMapper(BusAccountMapper busAccountMapper) {
        this.busAccountMapper = busAccountMapper;
    }

    public void setBusBussinessRealMapper(BusBussinessRealMapper busBussinessRealMapper) {
        this.busBussinessRealMapper = busBussinessRealMapper;
    }

    public void setSysNoticeMapper(SysNoticeMapper sysNoticeMapper) {
        this.sysNoticeMapper = sysNoticeMapper;
    }

    public void setBusUserRealMapper(BusUserRealMapper busUserRealMapper) {
        this.busUserRealMapper = busUserRealMapper;
    }

    public void setBusSuggestionMapper(BusSuggestionMapper busSuggestionMapper) {
        this.busSuggestionMapper = busSuggestionMapper;
    }

    public void setBusUserTokenMapper(BusUserTokenMapper busUserTokenMapper) {
        this.busUserTokenMapper = busUserTokenMapper;
    }

    public void setSysAccountroleMapper(SysAccountroleMapper sysAccountroleMapper) {
        this.sysAccountroleMapper = sysAccountroleMapper;
    }

    public void setBusGroupInfoMapper(BusGroupInfoMapper busGroupInfoMapper) {
        this.busGroupInfoMapper = busGroupInfoMapper;
    }

    public void setBusChargeCardMapper(BusChargeCardMapper busChargeCardMapper) {
        this.busChargeCardMapper = busChargeCardMapper;
    }

    public void setBusCardRechargeMapper(BusCardRechargeMapper busCardRechargeMapper) {
        this.busCardRechargeMapper = busCardRechargeMapper;
    }

    public void setBusPaymentMapper(BusPaymentMapper busPaymentMapper) {
        this.busPaymentMapper = busPaymentMapper;
    }

    public void setDcsChargerecordMapper(DcsChargerecordMapper dcsChargerecordMapper) {
        this.dcsChargerecordMapper = dcsChargerecordMapper;
    }

    public void setBusRebateMapper(BusRebateMapper busRebateMapper) {
        this.busRebateMapper = busRebateMapper;
    }

    public void setBusUserRebateMapper(BusUserRebateMapper busUserRebateMapper) {
        this.busUserRebateMapper = busUserRebateMapper;
    }

    @Override
    public int insertRebate(BusRebate record) {
        return busRebateMapper.insert(record);
    }

    @Override
    public int updateRebateByPrimaryKeySelective(BusRebate record) {
        return busRebateMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<BusRebate> selectRebateByPage(Map<String, Object> params) {
        return busRebateMapper.selectRebateByPage(params);
    }

    @Override
    public BusRebate selectRebateByPrimaryKey(Integer id) {
        return busRebateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countUserRebateByExample(BusUserRebateExample example) {
        return busUserRebateMapper.countByExample(example);
    }

    @Override
    public int deleteRebateByPrimaryKey(Integer id) {
        return busRebateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BusRebate> selectRebateByExample(BusRebateExample example) {
        return busRebateMapper.selectByExample(example);
    }

    @Override
    public List<BusUserRebate> selectUserRebateByExample(BusUserRebateExample example) {
        return busUserRebateMapper.selectByExample(example);
    }

}
