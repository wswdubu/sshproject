<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="role_insert.do" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
				<input name="role.id" type="hidden"/>
			<p>
				<label><tsp:transproname proname="role.rolename"/>：</label>
				<input name="role.rolename" type="text" maxlength="150" value=""/>
			</p>
			<p>
				<label><tsp:transproname proname="role.roledesc"/>：</label>
				<input name="role.roledesc" type="text" maxlength="150" value=""/>
			</p>
			<div class="divider"></div>
			<p>
				<label><tsp:transproname proname="role.privileges"/>：</label>
				<input name="privileges.ids" type="hidden"/>
				<input name="privileges.priname" type="text"/>
				<a class="btnLook" href="privilege_listlooks.do" lookupGroup="privileges">查找带回</a>	
			</p>
			<p>
				<label><tsp:transproname proname="role.users"/>：</label>
				<input name="users.ids" type="hidden"/>
				<input name="users.realname" type="text"/>
				<a class="btnLook" href="user_listlooks.do" lookupGroup="users">查找带回</a>	
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
