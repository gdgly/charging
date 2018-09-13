/**
 * 未审核充电桩列表
 */
var searchUnValidPilesByAjaxUrl="device_searchUnValidPilesByAjax.action";
var param={};
var str;
/**
 * 初始化搜索条件
 */
function initInput(){
	$("#searchPileName").val("");
}
$(function(){
	var tbody=$("#unValidPileTable").find("tbody");
	function initPileBody(item){
		$.post(searchUnValidPilesByAjaxUrl,param,function(data){
			$("#pileCurrentPage").val(data.page.startRow/(data.page.endRow-data.page.startRow)+1);
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

			$("#pileTotalProperty").text(data.page.totalProperty);
			if($("#pileCurrentPage").val() <= 1){
				$("#pileUpPage").attr("disabled","disabled");
			}
			else{
				$("#pileUpPage").removeAttr("disabled");
			}
			var pileTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
			if($("#pileCurrentPage").val() >= pileTotalPage){
				$("#pileDownPage").attr("disabled","disabled");
			}
			else{
				$("#pileDownPage").removeAttr("disabled");
			}
		});
	};
	initPileBody(tbody);
	initInput();
	//各类按钮事件绑定
	$("#pileUpPage").on("click",function(){
		param={};
		param.searchPileName = $("#searchpileName").val();
		param.pileCurrentPage=parseInt($("#pileCurrentPage").val())-1;
		initPileBody(tbody);
	});
	$("#pileDownPage").on("click",function(){
		param={};
		param.searchPileName = $("#searchpileName").val();
		param.pileCurrentPage=parseInt($("#pileCurrentPage").val())+1;
		initPileBody(tbody);
	});


	$("#pileSearchBtn").on("click",function(){
		param={};
		param.searchPileName = $("#searchpileName").val();
		initPileBody(tbody);
	});
})

