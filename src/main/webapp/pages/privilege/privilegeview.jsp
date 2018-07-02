<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="/per_insertOrUpdate.htm" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label><tsp:transproname proname="privilege.priurl"/>：</label>
				${privilege.priurl}
			</p>
			<p>
				<label><tsp:transproname proname="privilege.priname"/>：</label>
				${privilege.priname}
			</p>
			<p>
				<label><tsp:transproname proname="privilege.type"/>：</label>
				<tsp:selectview name="privilege.type" value="${privilege.type}"/>
			</p>
			<div class="divider"></div>
			<p>
				<label><tsp:transproname proname="privilege.roles"/>：</label>
				<input name="roles.ids" value="${roles.ids}" type="hidden"/>
				${roles.names}
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
