/**
 * 设备列表
 */


$(function(){
	var searchUnValidStationsUrl="device_searchUnValidStationsByAjax.action";
	var searchUnValidPilesByAjaxUrl="device_searchUnValidPilesByAjax.action";
	var param={};
	var str;
	var item;
	/**
	 * 初始化搜索条件
	 */
	function initInput(){
		$("#datetime").find("input").val("");
	}
	//初始化充电桩
	function initPileBody(){
		param={};
		param.currentPage=$("#currentPage2").val();
		item= $("#unValidPileTable").find("tbody");
		$.post(searchUnValidPilesByAjaxUrl,param,function(data){
			item.empty();
			$(data.page.root).each(function(index,value){
				var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
				var chaWay = parseInt(value.chaWay);//1.交流2.直流
				var pileType = parseInt(value.pileType);  //1.快充2.慢充3.超速
				var requestType = parseInt(value.requestType);//1.点桩新增2.单个装新增4.桩修改
				var validStatus = parseInt(value.validStatus);//1.待审核2.审核中3.审核通过4.审核失败
				str = "<tr>"+"<td>"+value.pileName+"</td>";

				if(pileType == 1){
					str=str+"<td>"+"快充"+"</td>";
				}
				else if(pileType == 2){
					str=str+"<td>"+"慢充"+"</td>";
				}
				else if(pileType == 3){
					str=str+"<td>"+"超速"+"</td>";
				}
				else{
					str=str+"<td>"+"未知"+"</td>";

				}

				if(chaWay == 1){
					str=str+"<td>"+"交流"+"</td>";
				}
				else if(chaWay == 2){
					str=str+"<td>"+"直流"+"</td>";
				}
				else{
					str=str+"<td>"+"未知"+"</td>";
				}
				if(requestType == 1){
					str=str+"<td>"+"新增桩"+"</td>";
				}
				else if(requestType == 2){
					str=str+"<td>"+"新增单个增桩"+"</td>";
				}
				else if(requestType == 4){
					str=str+"<td>"+"修改单个增桩"+"</td>";
				}
				else{
					str=str+"<td>"+"未知"+"</td>";
				}
				if(validStatus == 1){
					str=str+"<td>"+"待审核"+"</td>";

				}
				else if(validStatus == 2){
					str=str+"<td>"+"审核中"+"</td>";
				}
				else if(validStatus == 4){
					str=str+"<td>"+"审核失败"+"</td>";
				}
				else if(validStatus == 7){
					str=str+"<td>"+"用户取消"+"</td>";
				}
				else{
					str=str+"<td>"+"未知"+"</td>";
				}
				if(validStatus == 1&&requestType == 1){
					str=str+"<td>"
					+"<a href='deviceManager/editNewPile.action?type=1&newPileId="+value.id
					+"'>修改"
					+"</a>"
					+"</td>";
				}
				else if(validStatus == 1&&requestType == 2){
					str=str	+"<td>"
					+"<a href='deviceManager/editNewPile.action?type=2&newPileId="+value.id
					+"'>修改"
					+"</a>"
					+"</td>";
				}
				else{
					str=str+"<td>"+"</td>";
				}
				str=str+"</tr>";
				item.append(str);//封装table

			});
			var stationTotalPage2 = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
			setPagingToolbar2Params(data.page.totalProperty,stationTotalPage2,$("#currentPage2").val());
		});
	};
	//初始化充电点
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		item=$("#unValidStationTable").find("tbody");
		$.post(searchUnValidStationsUrl,param,function(data){
			item.empty();
			$(data.page.root).each(function(index,value){
				var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
				str = "<tr>"
					+"<td>"+value.stationName+"</td>"
					+"<td title='"+value.address+"'>"
					+mySubString(value.address,15)
					+"</td>";
				var requestType = parseInt(value.requestType);
				var validStatus = parseInt(value.validStatus);
				if(validStatus == 1){
					str=str +"<td style='color:green;'>"+"待审核"+"</td>";
				}
				else if(validStatus == 2){
					str=str +"<td style='color:red;'>"+"审核中"+"</td>";
				}
				else if(validStatus == 4){
					str=str +"<td style='color:#ccc;'>"+"审核失败"+"</td>";
				}
				else if(validStatus == 7){
					str=str +"<td>"+"用户取消"+"</td>";
				}
				if(requestType == 1){
					str=str +"<td>"+"新建点"+"</td>";
					if(validStatus == 1){
						str = str 
						+"<td>"
						+"<a href='deviceManager/editNewStation.action?newStationId=" 
						+value.id
						+"'>修改/</a>"
						+"<a href='deviceManager/addNewStationPile.action?newStationId="
						+value.id
						+"'>新增</a>"
						+"</td>";
					}
					else{
						str = str  +"<td></td>";
					}

				}
				else if(requestType == 3){
					str=str +"<td>"+"修改点"+"</td>"
					+"<td></td>";

				}

				str = str
				+"</tr>"; 
				item.append(str);//封装table

			});
			var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
			setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());

		});
	};

	initPagingToolbar2(initPileBody);
	initPagingToolbar(initStationBody);
	initStationBody();
	initOneBootStrapDate2($("#datetime"));
	initInput();
	stationAndPileBtn(initStationBody,initPileBody,initInput);

})

