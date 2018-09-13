<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="text-center">
	<ul class="pagination">
	   <s:if test="#request.pagebean.showFirstPage == true">
	   	<li><a href="<s:property value="#request.pagebean.firstPageUrl()"/>">首页</a></li>
	   </s:if>
	   <s:if test="#request.pagebean.previewEnabled == false">
	   		<li class="disabled"><a href="javascript:void(0)">上一页</a></li>
	   </s:if>
	   <s:else> 
	   		<li><a href="<s:property value="#request.pagebean.previewUrl()"/>">上一页</a></li>
	   </s:else>	   
	   <s:iterator value="#request.pagebean.pageinList" var="item">
			<li  <s:if test="#request.page == #item">class="active"</s:if>>
				<a href="<s:property value="#request.pagebean.url"/><s:property value="#request.pagebean.urlSufx()"/>page=${item}">${item}</a>
			</li>
		</s:iterator>
		<s:if test="#request.pagebean.nextEnabled == false">
	   		<li class="disabled"><a href="javascript:void(0)">下一页</a></li>
	   </s:if>
	   <s:else> 
	   		<li><a href="<s:property value="#request.pagebean.nextUrl()"/>">下一页</a></li>
	   </s:else>
		<s:if test="#request.pagebean.showLastPage == true">
	   	<li><a href="<s:property value="#request.pagebean.lastPageUrl()"/>">未页</a></li>
	   </s:if>
	</ul>
</div>
