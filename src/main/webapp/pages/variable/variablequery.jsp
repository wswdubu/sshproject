<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="variable_list.do" class="pageForm" onsubmit="return navTabSearch(this);">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><tsp:transproname proname="variable.varname"/>：</label>
				<input name="variable.varname" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="variable.varvalue"/>：</label>
				<input name="variable.varvalue" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="variable.returnname"/>：</label>
				<input name="variable.returnname" type="text" maxlength="150" />
			</div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">开始检索</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="reset">清空重输</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
