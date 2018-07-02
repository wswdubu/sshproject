<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="/per_insertOrUpdate.htm" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label><tsp:transproname proname="role.rolename"/>：</label>
				${role.rolename}
			</p>
			<p>
				<label><tsp:transproname proname="role.roledesc"/>：</label>
				${role.roledesc}
			</p>
			<div class="divider"></div>
			<p>
				<label><tsp:transproname proname="role.privileges"/>：</label>
				<input name="privileges.ids" value="${privileges.ids}" type="hidden"/>
				${privileges.names}
			</p>
			<p>
				<label><tsp:transproname proname="role.users"/>：</label>
				<input name="users.ids" value="${users.ids}" type="hidden"/>
				${users.names}
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">返回</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
