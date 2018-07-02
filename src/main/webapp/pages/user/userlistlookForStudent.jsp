<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<form id="pagerForm" method="post" action="user_listlooks.do">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dwzSearch(this, 'dialog');" action="user_listlooks.do" method="post">
	<div class="searchBar">
		<div class="subBar">
			<ul>
				<li><div class="button"><div class="buttonContent"><button type="button" multLookup="student" warn="请选择">选择带回</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div layoutH="116" id="w_list_print">
	<table class="table" width="98%" targetType="dialog" asc="asc" desc="desc">
		<thead>
			<tr>
			    <th width="22"><input type="checkbox" group="student" class="checkboxCtrl"></th>
				<th orderField="id" <s:if test="orderField=='id'">class="${orderDirection}"</s:if> title="排序">编号</th> 
				<th orderField="userid" <s:if test="orderField=='userid'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.userid"/></th>
				<th orderField="realname" <s:if test="orderField=='realname'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.realname"/></th>
				<th orderField="gender" <s:if test="orderField=='gender'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.gender"/></th>
				<th orderField="birthdate" <s:if test="orderField=='birthdate'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.birthdate"/></th>
				<th orderField="identifynum" <s:if test="orderField=='identifynum'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.identifynum"/></th>
				<th orderField="email" <s:if test="orderField=='email'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.email"/></th>
				<th orderField="phone" <s:if test="orderField=='phone'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.phone"/></th>
			</tr>
		</thead>
		<tbody>
		<s:iterator id="pojo" value="userlist">
			<tr target="sid_entity" rel="${pojo.id}">
			    <td><input name="student" value="{ids:'${pojo.id}', names:'${pojo.realname}'}" type="checkbox"></td>
				<td>${pojo.id}</td>
				<td>${pojo.userid}</td>
				<td>${pojo.realname}</td>
				<td><tsp:selectview name="user.gender" value="${pojo.gender}"/></td>
				<td> <fmt:formatDate value="${pojo.birthdate}" pattern="yyyy-MM-dd"/></td>
				<td>${pojo.identifynum}</td>
				<td>${pojo.email}</td>
				<td>${pojo.phone}</td>
			</tr>
		 </s:iterator>
		</tbody>
	</table>
	</div>
</div>

