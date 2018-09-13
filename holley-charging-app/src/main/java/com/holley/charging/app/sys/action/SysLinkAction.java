package com.holley.charging.app.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.platform.model.sys.SysCarBrand;
import com.holley.platform.util.CacheSysHolder;

public class SysLinkAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private ResultBean        resultBean       = new ResultBean();

    /**
     * 车品牌
     * 
     * @return 所有车品牌
     * @throws Exception
     */
    public String carbrand() throws Exception {
        List<SysCarBrand> brandlist = CacheSysHolder.getCarBrandList();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brandlist", brandlist);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 车型号
     * 
     * @return 车品牌id下的车型list
     * @throws Exception
     */
    public String carmodel() throws Exception {
        String brandid = this.getAttribute("brandid");
        if (!NumberUtils.isNumber(brandid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        List<SysCarBrand> modellist = CacheSysHolder.getCarModelListByPid(Integer.valueOf(brandid));
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("modellist", modellist);
        resultBean.setData(data);
        return SUCCESS;

    }

    public ResultBean getResultBean() {
        return resultBean;
    }

}
