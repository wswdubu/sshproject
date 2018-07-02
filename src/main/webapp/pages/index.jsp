<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理系统</title>

<link href="themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link href="uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
<!--[if IE]>
<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="js/speedup.js" type="text/javascript"></script>
<![endif]-->

<script src="js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="js/jquery.cookie.js" type="text/javascript"></script>
<script src="js/jquery.validate.js" type="text/javascript"></script>
<script src="js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>



<script src="bin/dwz.min.js" type="text/javascript"></script>

<script src="js/dwz.regional.zh.js" type="text/javascript"></script>
<%@include file="/pages/common/base.jsp"%>
<script type="text/javascript">
$(function(){
	DWZ.init("dwz.frag.xml", {
		loginUrl:"login_dialog.html", loginTitle:"登录",	// 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
		statusCode:{ok:200, error:300, timeout:301}, //【可选】
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"themes"}); // themeBase 相对于index页面的主题base路径
		}
	});
});

</script>
</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<img src="" alt="" height="65"/>
				<ul class="nav">
					<li>${currentuser.realname}您好</li>
					<li><a href="<%=basePath%>login_logout.do">退出</a></li> 
					
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="azure"><div class="selected">天蓝</div></li>
					<li theme="default"><div>蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<!--<li theme="red"><div>红色</div></li>-->
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					
				</ul>
			</div>

			<!-- navMenu -->
			
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>

				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader">
						<h2><span>Folder</span>管理系统</h2>
					</div> 
					<div class="accordionContent">
						<ul class="tree treeFolder">
							<li><a><tsp:transproname proname="mymenu"/></a>
								<ul>
									<shiro:hasPermission name="userlist">
									<li><a href="<%=basePath%>user_list.do?user.id=0" target="navTab" rel="userlist"><tsp:transproname proname="usermenu"/></a></li>
									</shiro:hasPermission>
									<shiro:hasPermission name="rolelist">
									<li><a href="<%=basePath%>role_list.do?role.id=0" target="navTab" rel="rolelist"><tsp:transproname proname="rolemenu"/></a></li>
									</shiro:hasPermission>
									<shiro:hasPermission name="privilegelist">
									<li><a href="<%=basePath%>privilege_list.do?privilege.id=0" target="navTab" rel="privilegelist"><tsp:transproname proname="privilegemenu"/></a></li>
									</shiro:hasPermission>
									<shiro:hasPermission name="variablelist">
									<li><a href="<%=basePath%>variable_list.do?variable.id=0" target="navTab" rel="variablelist"><tsp:transproname proname="variablemenu"/></a></li>
									</shiro:hasPermission>
									<li><a href="<%=basePath%>demo_list.do?demo.id=0" target="navTab" rel="demolist">demo</a></li>
									
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
						</div>
						<div class="pageFormContent" layoutH="80" style="margin-right:230px">
						</div>
						<div style="width:230px;position: absolute;top:60px;right:0" layoutH="80">
						</div>
					</div>
					
				</div>
			</div>
		</div>

	</div>

<div id="footer">管理系统</div>

</body>
</html>
