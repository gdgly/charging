<%@ page contentType="text/html;charset=UTF-8"  language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar" style="height: 220px;">
    <!-- Sidebar user panel -->
      <%-- <div class="user-panel">
        <div class="pull-left image">
          <img id="userHeadImg2" style="height: 21%;" src="${imgUrl}${webUser.headImg}" class="img-circle" alt="User Image">
        </div> --%>
      <%-- <div class="pull-left info">
          <p>${webUser.userName}</p>
          <a href="javascript:;"><i class="fa fa-circle text-success"></i> Online</a>
        </div> --%>
    <!-- </div> -->

      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu">
      <s:if test="#request.webUser.roleType == 5">
      <li class="header">集团主菜单</li>
      </s:if>
      <s:else>
      <li class="header">个人主菜单</li>
      </s:else>
     
     <s:iterator id="topMenu" value="#request.topMenu">
    
      <li class="treeview">
          <a href="javascript:;">
          <s:if test="#topMenu.moduleid == '31000000'">
              <i class="fa fa-user"></i>
          </s:if>
           <s:elseif test="#topMenu.moduleid == '51000000'">
             <i class="fa fa-cog"></i>
           </s:elseif>
	      <s:elseif test="#topMenu.moduleid == '32000000' || #topMenu.moduleid == '52000000'">
	       <i class="fa fa-dollar"></i>
	      </s:elseif>
	      <s:elseif test="#topMenu.moduleid == '33000000'">
	       <i class="fa fa-laptop"></i>
	      </s:elseif>
	      <s:elseif test="#topMenu.moduleid == '34000000'">
	     	<i class="fa fa-pie-chart"></i>
	      </s:elseif>
	      <s:elseif test="#topMenu.moduleid == '53000000'">
	     	<i class="fa fa-user-plus"></i>
	      </s:elseif>
            <span> <s:property value="#topMenu.modulename"/></span>
            <i class="fa fa-angle-left pull-right"></i>
          </a>
           <ul class="treeview-menu">
          <s:iterator id="subMenu" value="#request.subMenu">
          <s:if test="#topMenu.moduleid == #subMenu.parentmoduleid">
          <li><a id=<s:property value="#subMenu.note"/> href="javascript:;" link=<s:property value="#subMenu.url"/>><i class="fa fa-circle-o"></i> <s:property value="#subMenu.modulename"/></a></li>
          </s:if>
          </s:iterator>
          </ul>
      </li>
     </s:iterator>
 <!--        <li class="treeview">
          <a href="javascript:;">
            <i class="fa fa-user"></i>
            <span>个人中心</span>
            <i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
            <li><a id="userInfo" href="javascript:;" link="userAdmin/initUserInfo.action"><i class="fa fa-circle-o"></i> 个人资料</a></li>
            <li><a href="javascript:;" link="userAdmin/initUserCar.action"><i class="fa fa-circle-o"></i> 我的爱车</a></li>
            <li><a href="javascript:;" link="userAdmin/initUserFavorite.action"><i class="fa fa-circle-o"></i> 我的收藏</a></li>
            <li><a href="javascript:;" link="userAdmin/initUserMsg.action"><i class="fa fa-circle-o"></i> 我的消息</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-dollar"></i>
            <span>账户中心</span>
            <i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
            <li><a id="userAccount" href="javascript:;" link="userAccount/initUserAccount.action"><i class="fa fa-circle-o"></i> 我的钱包</a></li>
            <li><a href="javascript:;" link="userAccount/initUserAppointment.action"><i class="fa fa-circle-o"></i> 我的预约</a></li>
            <li><a href="javascript:;" link="userAccount/initUserCharge.action"><i class="fa fa-circle-o"></i> 我的充电</a></li>
            <li><a id="userBill" href="javascript:;" link="userAccount/initUserBill.action"><i class="fa fa-circle-o"></i> 我的账单</a></li>
            <li><a href="javascript:;" link="userAccount/initUserInvoice.action"><i class="fa fa-circle-o"></i> 我的发票</a></li>
          </ul>
        </li>

       <li class="header">个人运营主菜单</li>
          <li class="treeview">
          <a href="#">
            <i class="fa fa-laptop"></i>
            <span>设备管理</span>
            <i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
            <li><a href="javascript:;" link="userDevice/initUserDevice.action"><i class="fa fa-circle-o"></i> 我的设备</a></li>
            <li><a href="javascript:;" link="userDevice/initUserDeviceMap.action"><i class="fa fa-circle-o"></i> 设备监视</a></li>
            <li><a href="javascript:;" link="userDevice/initUserDeviceAlarm.action"><i class="fa fa-circle-o"></i> 设备告警</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-pie-chart"></i> <span>交易中心</span>
            <i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
            <li><a href="javascript:;" link="userTransaction/initUserDeviceAppointment.action"><i class="fa fa-circle-o"></i> 收益统计</a></li>
            <li><a id="userDevciceBill" href="javascript:;" link="userTransaction/initUserDeviceBill.action"><i class="fa fa-circle-o"></i> 收益账单</a></li>
            <li><a href="javascript:;" link="userTransaction/initUserProfit.action"><i class="fa fa-circle-o"></i> 收益分析</a></li>
          </ul>
        </li> -->
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
