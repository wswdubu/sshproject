<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<!DOCTYPE html>
<html>
<head>
	<title>管理系统</title>
	<link rel="stylesheet" href="css/style.css">
	<link href="css/popup-box.css" rel="stylesheet" type="text/css" media="all" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>

	</script><script src="js/jquery.min.js"></script>
<script src="js/jquery.magnific-popup.js" type="text/javascript"></script>
<script type="text/javascript" src="js/modernizr.custom.53451.js"></script> 
 <script>
						$(document).ready(function() {
						$('.popup-with-zoom-anim').magnificPopup({
							type: 'inline',
							fixedContentPos: false,
							fixedBgPos: true,
							overflowY: 'auto',
							closeBtnInside: true,
							preloader: false,
							midClick: true,
							removalDelay: 300,
							mainClass: 'my-mfp-zoom-in'
						});
																						
						});
</script>	
		
</head>
<body>
	<h1>管理系统</h1>
	<div class="w3layouts">
		<div class="signin-agile">
			<h2>登录 </h2>
			<form action="login_login.do" method="post">
				<input type="text" name="username" class="name" placeholder="用户名" required="">
				<input type="password" name="password" class="password" placeholder="密码" required="">
				<ul>
					<li>
						<input type="checkbox" id="brand1" value="">
					</li>
				</ul>
				<div class="clear"></div>
				<input type="submit" value="登录">
			</form>
		</div>
		<div class="signup-agileinfo">
			<h3>管理系统</h3>
			<p>ssh开发平台方便ssh项目搭建。</p>
			<div class="more">
				<a class="book popup-with-zoom-anim button-isi zoomIn animated" data-wow-delay=".5s" href="#small-dialog">点击注册</a>				
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="footer-w3l">
		<p class="agileinfo"> 
 </p>
	</div>
	<div class="pop-up"> 
	<div id="small-dialog" class="mfp-hide book-form">
		<h3>用户注册 </h3>
			<form action="user_regist.do" method="post" id="studentform">
				<input type="hidden" name="user.usertype" value="1"/>
				<input type="text" name="user.userid" placeholder="用户名" required=""/>
				<input type="text" name="user.realname" placeholder="真实姓名" required=""/>
				<input type="text" name="user.email" class="email" placeholder="邮件" required=""/>
				<input type="password" name="user.password" class="password" placeholder="密码" required=""/>
				<input type="password"  class="password" placeholder="重复密码" required=""/>					
				<input type="button" value="点击注册" onclick="zhuce();">
			</form>
			<script type="text/javascript">
				function zhuce(){
					var str_data=$("#studentform input").
					map(function(){return ($(this).attr("name")+'='+$(this).val());})
					.get().join("&");
						$.ajax({
							   type: "POST",
							   url: "user_regist.do",
							   data: str_data,
							   success: function(msg){
							     alert(msg );
							   }
							});
				}
			</script>
	</div>
</div>	
<body>
</html>