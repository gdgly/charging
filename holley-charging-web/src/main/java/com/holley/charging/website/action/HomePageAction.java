package com.holley.charging.website.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.pob.PobNews;
import com.holley.charging.service.website.PobObjectService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.NewsStatusEnum;
import com.holley.common.constants.charge.NewsTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;

public class HomePageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private PobObjectService  pobObjectService;

    private List<PobNews>     trendsList;
    private List<PobNews>     newsList;

    public String init() {
        this.getRequest().setAttribute(Globals.CURRENTMODULE, "homepage");
        return SUCCESS;
    }

    public String queryNewsList() {
        int pageIndex = 1;
        int pageLimit = 4;
        Page page = this.returnPage(pageIndex, pageLimit);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", NewsTypeEnum.TRENDS.getShortValue());
        params.put("status", NewsStatusEnum.OPEN.getShortValue());
        params.put(Globals.PAGE, page);
        List<PobNews> dataList = pobObjectService.selectNewsByPage(params);
        for (PobNews record : dataList) {
            if (record.getPublishdate() != null) {
                record.setPublishdatestr(DateUtil.DateToShortStr(record.getPublishdate()));
            }
        }
        this.trendsList = dataList;

        pageLimit = 6;
        params.put("type", NewsTypeEnum.NEWS.getShortValue());
        dataList = pobObjectService.selectNewsByPage(params);
        for (PobNews record : dataList) {
            if (record.getPublishdate() != null) {
                record.setPublishdatestr(DateUtil.DateToShortStr(record.getPublishdate()));
            }
        }
        this.newsList = dataList;
        this.success = true;
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<PobNews> getTrendsList() {
        return trendsList;
    }

    public List<PobNews> getNewsList() {
        return newsList;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

}
