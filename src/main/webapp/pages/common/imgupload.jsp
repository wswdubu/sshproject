<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" src="<%=basePath%>pages/uploadify/scripts/jquery.uploadify.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>pages/uploadify/css/uploadify.css" />
<link href="<%=basePath%>pages/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="<%=basePath%>pages/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="<%=basePath%>pages/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<script type="text/javascript">
<!--
	//加载附件
	function loadAttach(){
		var offdocAnnexeStr=$("#offdocAnnexes").val();
		var annexes=offdocAnnexeStr.split("<%=SystemSymbol.COMMA%>");
		var offdocAnnexes="";//公文附件名字集合
		if(annexes!=''){
			for(var i=0;i<annexes.length;i++){
				var annexeNames=annexes[i].split("<%=SystemSymbol.ATTACHNAMESEP%>");
				var attaches=$("#attaches");
	            var attachTempFileName=annexeNames[1];
	            var attachRealFileName=annexeNames[0];;
	            var attachContainerId=attachTempFileName.substring(0,attachTempFileName.indexOf('<%=SystemSymbol.POINT%>'));
	            attaches.append("<span id='"+attachContainerId+"'><img width='150' style='border: 5px solid #CCC;padding: 5px;' height='110'  src='<%=basePath%>/guangjie/userimg/"+attachRealFileName+"<img src='/guangjie/pages/uploadify/img/download.png' style='cursor:pointer' onclick='downloadAttach(\""+attachRealFileName+"\",\""+attachTempFileName+"\")'/></span>");
	            offdocAnnexes+=attachRealFileName+"<%=SystemSymbol.ATTACHNAMESEP%>"+attachTempFileName+"<%=SystemSymbol.COMMA%>";
	            $("#offdocAnnexes").val(offdocAnnexes);
			}	
		}
	}
	//加载附件--回复附件
	function loadAttachreply(){
		var replyoffdocAnnexeStr=$("#replyoffdocAnnexes").val();
		var replyannexes=replyoffdocAnnexeStr.split("<%=SystemSymbol.COMMA%>");
		var replyoffdocAnnexes="";//公文附件名字集合
		if(replyannexes!=''){
			for(var i=0;i<replyannexes.length;i++){
				var replyannexeNames=replyannexes[i].split("<%=SystemSymbol.ATTACHNAMESEP%>");
				var replyattaches=$("#replyattaches");
	            var replyattachTempFileName=replyannexeNames[1];
	            var replyattachRealFileName=replyannexeNames[0];
	            var replyattachContainerId=replyattachTempFileName.substring(0,replyattachTempFileName.indexOf('<%=SystemSymbol.POINT%>'));
	            replyattaches.append("<span id='"+replyattachContainerId+"'>"+replyattachRealFileName+"<img src='/guangjie/pages/uploadify/img/download.png' style='cursor:pointer' onclick='downloadAttach(\""+replyattachRealFileName+"\",\""+replyattachTempFileName+"\")'/></span>");
	            replyoffdocAnnexes+=replyattachRealFileName+"<%=SystemSymbol.ATTACHNAMESEP%>"+replyattachTempFileName+"<%=SystemSymbol.COMMA%>";
	            $("#replyoffdocAnnexes").val(replyoffdocAnnexes);
			}	
		}
	}
	//加载附件--删除
	function loadgoodsimg(){
		var goodsimgstr=$("#goodsimgs").val();
		var imgarray=goodsimgstr.split("<%=SystemSymbol.COMMA%>");
		var goodsimgs="";
		if(imgarray!=''){
			for(var i=0;i<imgarray.length;i++){
				var imgattaches=$("#attaches");
	            var imgattachTempFileName=imgarray[i];
	            if(imgattachTempFileName!=''){
	            var imgattachContainerId=imgattachTempFileName.substring(0,imgattachTempFileName.indexOf('<%=SystemSymbol.POINT%>'));
	       	 imgattaches.append("<span id='"+imgattachContainerId+"'><img width='150' style='border: 5px solid #CCC;padding: 5px;' height='110'  src='/guangjie/guangjie/userimg/"+imgattachTempFileName+"'/><img src='<%=basePath%>pages/uploadify/img/uploadify-cancel.png' style='cursor:pointer' onclick='delAttach(\"ss\",\""+imgattachTempFileName+"\")'/></span>");
	            }
			}	
		}
	}
	//加载附件
	function loadgoodsimgnodel(){
		var goodsimgstr=$("#goodsimgs").val();
		var imgarray=goodsimgstr.split("<%=SystemSymbol.COMMA%>");
		var goodsimgs="";
		if(imgarray!=''){
			for(var i=0;i<imgarray.length;i++){
				var imgattaches=$("#attaches");
	            var imgattachTempFileName=imgarray[i];
	            if(imgattachTempFileName!=''){
	            var imgattachContainerId=imgattachTempFileName.substring(0,imgattachTempFileName.indexOf('<%=SystemSymbol.POINT%>'));
	       	 imgattaches.append("<span id='"+imgattachContainerId+"'><img width='150' style='border: 5px solid #CCC;padding: 5px;' height='110'  src='/guangjie/guangjie/userimg/"+imgattachTempFileName+"'/>");
	            }
			}	
		}
	}
	//下载附件
	function downloadAttach(attachRealFileName,attachTempFileName){
		window.open("<%=basePath%>DownloadAttach?attachRealFileName="+attachRealFileName+"&attachTempFileName="+attachTempFileName);
	}
	
	function openPdf(title,url){
		var prop="fullscreen=1,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no";
		window.open(url,title,prop);
	}
	//载入iWebPDF
	function loadPdf(){
		try{
		  var WebPDF=document.getElementById("WebPDF");
		  var recordID="<%=request.getParameter("recordID")%>";
		  //以下属性必须设置，实始化iWebPDF
		  WebPDF.WebUrl="<%=basePath%>LookPdf";//WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档 
		  WebPDF.RecordID=recordID;   //RecordID:本文档记录编号
		  WebPDF.FileName=recordID+".pdf";   //FileName:文档名称
		  WebPDF.UserName="admin";   			//UserName:操作用户名
		  WebPDF.ShowTools =0;               //工具栏可见（1,可见；0,不可见）
		  WebPDF.SaveRight = 1;               //是否允许保存当前文档（1,允许；0,不允许）
		  WebPDF.PrintRight = 1;              //是否允许打印当前文档（1,允许；0,不允许）
		  WebPDF.AlterUser = false;           //是否允许由控件弹出提示框 true表示允许  false表示不允许
		  WebPDF.ShowBookMark = 1;			//是否显示书签树按钮（1,显示；0,不显示）
		  WebPDF.ShowSigns = 1;         	    //设置签章工具栏当前是否可见（1,可见；0,不可见）
		  WebPDF.EnableTools("手写签名;批量验证;签章参数",2);
		  WebPDF.ShowTools = 0;
		  WebPDF.ShowMenus = 0;
		  WebPDF.EnableTools("打印文档",false);
		  WebPDF.SideWidth = 100;             //设置侧边栏的宽度
		  WebPDF.WebOpen();                   //打开该文档    交互OfficeServer的OPTION="LOADFILE"    <参考技术文档>
		  WebPDF.Zoom = 100;                  //缩放比例
		  WebPDF.Rotate = 360;                //当显示页释放角度
		  WebPDF.CurPage = 1;                 //当前显示的页码
		  WebPDF.EnableTools("电子签章", 2);
		}catch(e){
		 	alertMsg.error(e.description);            //显示出错误信息
		}
	}
	 //加载文件上传插件
	function upload(){
	var goodsimgs=$("#goodsimgs").val();
		$("#uploadify").uploadify(
		{
			'buttonText' : '上传图片',
			swf : '/guangjie/pages/uploadify/scripts/uploadify.swf',
			uploader: '/guangjie/UploadAttach',
			cancelImg :'/guangjie/pages/uploadify/img/uploadify-cancel.png', //取消按钮图片
			queueID:'fileQueue', 
			queueSizeLimit :10,//上传文件的数量
			buttonImage : null,
			displayData: 'speed',//进度条的显示方式 
			method:'get',//如果向后台传输数据，必须是get
			fileSizeLimit : '10MB',
    		fileTypeExts   : '*.png;*.jpg;*.gif;*.jpeg;',
			auto :true,//是否自动上传
			height : 20,
			rollover :true,
			multi : true,
			width : 74,
        	//检测FLASH失败调用
        	'onFallback':function(){
            	alertMsg.error("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
        	},
        	onUploadSuccess: function (file, data, response){
                var attaches=$("#attaches");
                var attach=eval("("+data+")");
                var attachTempFileName=attach.attachTempFileName;
                var attachRealFileName=attach.attachRealFileName;
                var attachContainerId=attachTempFileName.substring(0,attachTempFileName.indexOf("."));
                attaches.append("<span id='"+attachContainerId+"'><img width='150' style='border: 5px solid #CCC;padding: 5px;' height='110'  src='<%=basePath%>/guangjie/userimg/"+attachTempFileName+"'/><img src='<%=basePath%>pages/uploadify/img/uploadify-cancel.png' style='cursor:pointer' onclick='delAttach(\""+attachRealFileName+"\",\""+attachTempFileName+"\")'/></span>");
                goodsimgs+=attachTempFileName+"<%=SystemSymbol.COMMA%>";
                $("#goodsimgs").val(goodsimgs);
        	}
		});
	}
	//删除附件  
	function delAttach(attachRealFileName,attachTempFileName) {
		$.post("<%=basePath%>DelAttach",{attachTempFileName:attachTempFileName},function(data){
			var attach=eval("("+data+")");
			var attachFileName=attach.attachTempFileName;
            var attachContainerId=attachFileName.substring(0,attachFileName.indexOf("<%=SystemSymbol.POINT%>"));
            var gs=$("#goodsimgs").val();
            var attachFileNameStr=attachTempFileName+"<%=SystemSymbol.COMMA%>";
            gs=gs.replace(attachFileNameStr,"<%=SystemSymbol.EMPTY%>");
            $("#goodsimgs").val(gs);
			$("#"+attachContainerId).remove();
		});
	}
	//删除附件  --回复附件
	function delAttachreply(attachRealFileName,attachTempFileName) {
		$.post("<%=basePath%>DelAttach",{attachTempFileName:attachTempFileName},function(data){
			var attach=eval("("+data+")");
			var attachFileName=attach.attachTempFileName;
            var attachContainerId=attachFileName.substring(0,attachFileName.indexOf("<%=SystemSymbol.POINT%>"));
            var offdocAnnexes=$("#offdocAnnexes").val();
            var attachFileNameStr=attachTempFileName+"<%=SystemSymbol.COMMA%>";
            offdocAnnexes=offdocAnnexes.replace(attachFileNameStr,"<%=SystemSymbol.EMPTY%>");
            $("#offdocAnnexes").val(offdocAnnexes);
			$("#"+attachContainerId).remove();
		});
	}
//-->
</script>
