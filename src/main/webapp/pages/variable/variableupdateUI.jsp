<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="variable_update.do" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label><tsp:transproname proname="variable.varname"/>：</label>
				<input name="variable.varname" type="text" maxlength="150" value="${variable.varname }"/>
			</p>
			<p>
				<label><tsp:transproname proname="variable.varvalue"/>：</label>
				<input name="variable.varvalue" type="text" maxlength="150" value="${variable.varvalue }"/>
			</p>
			<p>
				<label><tsp:transproname proname="variable.returnname"/>：</label>
				<input name="variable.returnname" type="text" maxlength="150" value="${variable.returnname }"/>
			</p>
			<div class="divider"></div>
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
