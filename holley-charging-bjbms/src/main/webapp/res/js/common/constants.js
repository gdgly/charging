// -----------时间常量定义----------
dataTimeFormateT = 'Y-m-dTH:i:s';
dataTimeFormate = 'Y-m-d H:i:s';
timeformat = "H:i:s";
timeHI =  "H:i";
dataTimeFormateWithZero ='Y-m-d 00:00:00';
dataFormate = 'Y-m-d';
monthFormat = 'Y-m';
yearFormat = 'Y';
CR_MODEL = 2;//创锐
//ohers------------------
All = "all";

TREE_DEFAULT_LIMIT = 20; //树默认每页显示记录数

//查询类型
SEARCH_SELF='-1';	//自定义
SEARCH_MIN = '0';
SEARCH_HOUR = '1';
SEARCH_DATE = "2";   //日
SEARCH_MONTH = "3";  //月
SEARCH_YEAR="4";
SEARCH_BETWEEN = "10"; //任意时段
SEARCH_SINGGLE = "11";
	
LEFT_TREE_HEIGHT_GAP = 56; //左边树中tab+toolbar的高度
LEFT_TREE_PAGINGBAR_HEIGHT = 20;//分页工具条高度

ROWSPLIT = "@"; // 分隔符
COLUMNSPLIT = "#"; 

PAGE_LIMIT = 20;//分页条数

PASSWORD_SECRETKEY = "charging";//密码加密密钥

//-----------用户类型----------
PLATFORM = 1;//平台
ENTERPRISE = 2;//企业
PERSION = 3;//个人
COMPANY = 9;//子公司

//-----------新闻动态类型----------
TRENDS = 1;
NEWS = 2;

//-----------菜单样式----------
HEADER_HEIGHT = 50;
FOOTER_HEIGHT = 40;

//-----------地图打点样式----------
LABEL_OFFSET_X1=10;
LABEL_OFFSET_X2=7;
LABEL_OFFSET_X3=3;
LABEL_OFFSET_Y=4;
LABEL_STYLE = {
	 color : "white",
	 backgroundColor:"transparent",
	 borderColor:"transparent",
	 fontSize : "12px",
	 height : "20px",
	 lineHeight : "20px",
	 fontFamily:"微软雅黑"
 }

//-----------操作类型----------
REQUEST_TYPE_ADD = 1;
REQUEST_TYPE_EDIT = 2;

ADMIN_ROLE_ID = 1;//系统最高管理员

UPLOADIMG_MAX_SIZE = 4;//上传图片最大大小4M

var REGBOX = {
		regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,//邮箱
		regName : /^[a-z0-9_-]{3,16}$/,//用户名
//		regMobile : /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/,//手机
		regMobile : /^(1)+\d{10}$/,//手机
		regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
		//身份证正则表达式(15位) 
		regCard15 : /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/,
		//身份证正则表达式(18位) 
		regCard18 : /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/,
		regNum:/^[0-9]*$/,
		//中文
		regCn:/^[\u4e00-\u9fa5]+$/,
		regMoney:/^(([1-9]{1}\d*)|([0]{1}))(\.(\d){0,2})?$/
}

MAX_EXPORT = 5000;//excel最大导出条数


