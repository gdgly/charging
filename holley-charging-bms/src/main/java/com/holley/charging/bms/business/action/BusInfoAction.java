package com.holley.charging.bms.business.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.Page;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/**
 * 运营商信息查询
 * 
 * @author zdd
 */
public class BusInfoAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private Page              page;

    /**
     * 运营商信息列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        return SUCCESS;
    }

    /**
     * 运营商详细信息
     * 
     * @return
     */
    public String detailInit() {
        String infoid = getParameter("infoid");
        if (StringUtil.isNotNumber(infoid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        Integer infoId = Integer.valueOf(infoid);
        // 查询运营商信息
        BusBussinessInfo record = userService.selectBussinessByPrimaryKey(infoId);
        // 查询该企业的帐号列表
        List<BusUser> userList = userService.selectBusinessUserByInfoid(infoId);
        this.getRequest().setAttribute("busInfo", record);
        this.getRequest().setAttribute("userList", userList);
        return SUCCESS;
    }

    /**
     * 查询运营商信息列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = this.getParameter("keyword");
        String province = this.getParameter("province");
        String city = this.getParameter("city");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(province, city)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isDigits(province) && Integer.parseInt(province) > 0) {
            params.put("province", Integer.valueOf(province));
        }
        if (StringUtil.isDigits(city) && Integer.parseInt(city) > 0) {
            params.put("city", Integer.valueOf(city));
        }
        if (isExportExcel()) {
            List<BusBussinessInfo> busInfoList = userService.selectBusinessInfoByPage(params);
            String[] headsName = { "运营商信息编码", "企业名称", "联系电话", "所属省", "所属市", "详细地址", "主要业务应用", "主要业务", "企业规模", "营业年限", "企业银行开户名", "企业对公账户", "开户行名称", "营业执照", "法人代表身份证", "办理人身份证" };
            String[] properiesName = { "id", "busName", "tel", "provinceDesc", "cityDesc", "address", "busDomain", "domain", "scale", "limitYear", "accRealName", "bankAccount",
                                       "bankName", "licenceImg", "corporateImg", "transatorImg" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(busInfoList, properiesName, headsName, BusBussinessInfo.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusBussinessInfo> busInfoList = userService.selectBusinessInfoByPage(params);
            page.setRoot(busInfoList);
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
