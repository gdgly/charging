$(document).ready(function(){
	queryDeviceList();
});

function queryDeviceList(){
	$.ajax({
        type: "POST",
        url: 'device/chargedevice_queryDeviceList.action',
        data: {tm:new Date().getTime()},
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
            	var deviceList = data.deviceList;
            	createContentDiv(deviceList);
            }else{
            	
            }
        }
    });
}

function createContentDiv(list){
	var html = '';
	$.each(list, function(i, item){
		html += '<div>';
		html += '<img alt="" src="'+item.picture+'" style="width: 100%;height: 100%;">';
		html += '</div>';
	});
	$('#content').html(html);
}

