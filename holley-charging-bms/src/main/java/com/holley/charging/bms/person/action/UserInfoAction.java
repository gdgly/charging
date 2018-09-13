package com.holley.charging.bms.person.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.Page;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/**
 * 个人信息查询
 * 
 * @author zdd
 */
public class UserInfoAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private Page              page;

    /**
     * 个人信息列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        return SUCCESS;
    }

    public String detailInit() {
        String infoid = getParameter("infoid");
        if (StringUtil.isNotNumber(infoid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusUserInfo record = userService.selectUserInfoDetail(Integer.valueOf(infoid));
        this.getRequest().setAttribute("busUserInfo", record);
        return SUCCESS;
    }

    /**
     * 查询个人信息列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = this.getParameter("keyword");
        String province = this.getParameter("province");
        String city = this.getParameter("city");
        String realinfo = this.getParameter("realinfo");

        // String pageindex = this.getParameter("pageindex");
        // String pagelimit = this.getParameter("pagelimit");
        int pageindex = getParamInt("pageindex");
        int pagelimit = getParamInt("pagelimit");
        // if (StringUtil.isNotNumber(province, city)) {
        // this.success = false;
        // this.message = "参数格式不正确.";
        // return SUCCESS;
        // }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(realinfo)) {
            params.put("realinfo", StringUtil.trim(realinfo));
        }
        if (StringUtil.isDigits(province) && Integer.parseInt(province) > 0) {
            params.put("province", Integer.valueOf(province));
        }
        if (StringUtil.isDigits(city) && Integer.parseInt(city) > 0) {
            params.put("city", Integer.valueOf(city));
        }
        if (isExportExcel()) {
            List<BusUserInfo> userInfoList = userService.selectUserInfoByPage(params);
            String[] headsName = { "个人信息编码", "用户昵称", "手机号码", "身份证号", "真实姓名", "身份证件", "性别", "出身日期", "个性签名", "所属省", "所属市", "车牌号", "车架号", "车品牌", "车型号", "行驶证" };
            String[] properiesName = { "id", "username", "phone", "cardNo", "realName", "front", "sexDesc", "birthdayDesc", "sign", "provinceDesc", "cityDesc", "plateNo", "vin",
                    "brandDesc", "modelDesc", "pic" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(userInfoList, properiesName, headsName, BusUserInfo.class);
            return null;
        } else {
            // if (StringUtil.isNotNumber(pageindex, pagelimit)) {
            // this.success = false;
            // this.message = "参数格式不正确.";
            // return SUCCESS;
            // }
            Page page = this.returnPage(pageindex, pagelimit);
            params.put(Globals.PAGE, page);
            List<BusUserInfo> userInfoList = userService.selectUserInfoByPage(params);
            page.setRoot(userInfoList);
            this.page = page;
            return SUCCESS;
        }

    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Page getPage() {
        return page;
    }

}
