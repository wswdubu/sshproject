<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="/per_insertOrUpdate.htm" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label><tsp:transproname proname="variable.varname"/>：</label>
				${variable.varname}
			</p>
			<p>
				<label><tsp:transproname proname="variable.varvalue"/>：</label>
				${variable.varvalue}
			</p>
			<p>
				<label><tsp:transproname proname="variable.returnname"/>：</label>
				${variable.returnname}
			</p>
			<div class="divider"></div>
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
