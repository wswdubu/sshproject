<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="/per_insertOrUpdate.htm" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label><tsp:transproname proname="user.userid"/>：</label>
				${user.userid}
			</p>
			<p>
				<label><tsp:transproname proname="user.realname"/>：</label>
				${user.realname}
			</p>
			<p>
				<label><tsp:transproname proname="user.password"/>：</label>
				${user.password}
			</p>
			<p>
				<label><tsp:transproname proname="user.gender"/>：</label>
				<tsp:selectview name="user.gender" value="${user.gender}"/>
			</p>
			<p>
				<label><tsp:transproname proname="user.birthdate"/>：</label>
				<td> <fmt:formatDate value="${user.birthdate}" pattern="yyyy-MM-dd"/></td>
			</p>
			<p>
				<label><tsp:transproname proname="user.identifynum"/>：</label>
				${user.identifynum}
			</p>
			<p>
				<label><tsp:transproname proname="user.email"/>：</label>
				${user.email}
			</p>
			<p>
				<label><tsp:transproname proname="user.phone"/>：</label>
				${user.phone}
			</p>
			<p>
				<label><tsp:transproname proname="user.homeaddress"/>：</label>
				${user.homeaddress}
			</p>
			<p>
				<label><tsp:transproname proname="user.createtime"/>：</label>
				<td> <fmt:formatDate value="${user.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</p>
			<p>
				<label><tsp:transproname proname="user.description"/>：</label>
				${user.description}
			</p>
			<p>
				<label><tsp:transproname proname="user.usertype"/>：</label>
				<tsp:selectview name="user.usertype" value="${user.usertype}"/>
			</p>
			<div class="divider"></div>
			<p>
				<label><tsp:transproname proname="user.roles"/>：</label>
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
