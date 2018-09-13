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
import com.holley.common.util.StringUtil;

public class NewsAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private PobObjectService  pobObjectService;

    private Page              page;

    public String init() {
        String type = this.getParameter("type");
        if (StringUtil.isEmpty(type)) {
            this.getRequest().setAttribute("newstype", NewsTypeEnum.NEWS.getValue());
        } else {
            this.getRequest().setAttribute("newstype", type);
        }
        this.getRequest().setAttribute(Globals.CURRENTMODULE, "news");

        return SUCCESS;
    }

    public String queryList() {
        String newsType = this.getParameter("newsType");
        String pageIndex = this.getParameter("pageIndex");
        String pageLimit = this.getParameter("pageLimit");
        if (StringUtil.isEmpty(newsType) || StringUtil.isEmpty(pageIndex) || StringUtil.isEmpty(pageLimit)) {
            return SUCCESS;
        }
        Page page = this.returnPage(Integer.valueOf(pageIndex), Integer.valueOf(pageLimit));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", Short.valueOf(newsType));
        params.put("status", NewsStatusEnum.OPEN.getShortValue());
        params.put(Globals.PAGE, page);
        List<PobNews> dataList = pobObjectService.selectNewsByPage(params);
        for (PobNews record : dataList) {
            if (record.getPublishdate() != null) {
                record.setPublishdatestr(DateUtil.DateToShortStr(record.getPublishdate()));
            }
        }
        page.setRoot(dataList);
        this.page = page;
        return SUCCESS;
    }

    public String queryDetail() {
        String id = this.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            return SUCCESS;
        }
        PobNews pobNews = pobObjectService.selectNewsByPrimaryKey(Integer.valueOf(id));
        pobNews.setTypedesc(NewsTypeEnum.getText(pobNews.getType()));
        if (pobNews.getPublishdate() != null) {
            pobNews.setPublishdatestr(DateUtil.DateToShortStr(pobNews.getPublishdate()));
        }

        if (pobNews.getVisitcount() == null) {
            pobNews.setVisitcount(1);
        } else {
            pobNews.setVisitcount(pobNews.getVisitcount() + 1);
        }

        PobNews record = new PobNews();
        record.setId(Integer.valueOf(id));
        record.setVisitcount(pobNews.getVisitcount());
        pobObjectService.updateNewsByPrimaryKeySelective(record);
        this.getRequest().setAttribute("news", pobNews);
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

    public Page getPage() {
        return page;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

}
