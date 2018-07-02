<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="privilege_insert.do" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
				<input name="privilege.id" type="hidden"/>
			<p>
				<label><tsp:transproname proname="privilege.priurl"/>：</label>
				<input name="privilege.priurl" type="text" maxlength="150" value=""/>
			</p>
			<p>
				<label><tsp:transproname proname="privilege.priname"/>：</label>
				<input name="privilege.priname" type="text" maxlength="150" value=""/>
			</p>
			<p>
				<label><tsp:transproname proname="privilege.type"/>：</label>
				<tsp:select name="privilege.type"/>
			</p>
			<div class="divider"></div>
			<p>
				<label><tsp:transproname proname="privilege.roles"/>：</label>
				<input name="roles.ids" type="hidden"/>
				<input name="roles.rolename" type="text"/>
				<a class="btnLook" href="role_listlooks.do" lookupGroup="roles">查找带回</a>	
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
