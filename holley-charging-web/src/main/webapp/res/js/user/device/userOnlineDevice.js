/**
 * 设备监控
 */

	function showMap(obj){
		stationId = $(obj).attr("stationId");
		if(stationId && stationId > 0){
			href("userDevice/initUserDeviceMap.action?stationId="+stationId);
		}
	}
	function mouseIn(obj){
		$(obj).parent().parent().attr("onclick","");
		$(obj).css('color','#0a94f2');
	}
	function mouseOut(obj){
		$(obj).parent().parent().attr("onclick","showInfo(this)");
		$(obj).css('color','#858585');
	}
	function showInfo(obj){
		pileName = $(obj).find("[id=pilename]").text();
		pileCode = $(obj).find("[id=pilecode]").text();
		pileType = $(obj).find("[id=piletype]").text();
		statusDesc = $(obj).find("[id=status]").text();
		status =  $(obj).find("[id=status]").attr("status");
		comType = $(obj).find("[id=comtype]").text();
		comAddr = $(obj).find("[id=comaddr]").text();
		chaWay = $(obj).find("[id=chaway]").text();
		pileModel = $(obj).find("[id=pilemodel]").text();
		outV = $(obj).find("[id=outv]").text();
		outA = $(obj).find("[id=outi]").text();
		updateTime = $(obj).find("[id=updatetime]").text();
		address = $(obj).find("[id=address]").text();
		$("#model"+"PileName").text(pileName);
		$("#model"+"PileCode").text(pileCode);
		$("#model"+"PileType").text(pileType);
		if(status == 1){
			$("#model"+"Status").css("color","#1ED538");
		}else if(status == 2){
			$("#model"+"Status").css("color","#3EA2CC");
		}else if(status == 3){
			$("#model"+"Status").css("color","#FF8D18");
		}else if(status == 4){
			$("#model"+"Status").css("color","#6E6E6E");
		}else{
			$("#model"+"Status").css("color","#6E6E6E");
		}
		$("#model"+"Status").text(statusDesc);
		$("#model"+"ComType").text(comType);
		$("#model"+"ComAddr").text(comAddr);
		$("#model"+"ChaWay").text(chaWay);
		$("#model"+"PileModel").text(pileModel);
		$("#model"+"OutV").text(outV);
		$("#model"+"OutA").text(outA);
		$("#model"+"UpdateTime").text(updateTime);
		$("#model"+"Address").text(address);
	 $("#onLinePileListDiv").addClass("hide");
	$("#onLinePileInfoDiv").removeClass("hide"); 
	}

	$(function(){
		$("#comeBackOnLinePileListBtn").on("click",function(){
			$("#onLinePileInfoDiv").addClass("hide");
			$("#onLinePileListDiv").removeClass("hide");
		});
		$("#comeBackMonitorMapBtn").on("click",function(){
			href("userDevice/initUserDeviceMap.action");
		});
		$("#flushStatusBtn").on("click",function(){
			reload();
		});
	});
	
	
