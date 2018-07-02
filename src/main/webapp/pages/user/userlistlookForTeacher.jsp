<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<form id="pagerForm" method="post" action="user_listlook.do">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageContent">
	<div layoutH="116" id="w_list_print">
	<table class="table" width="98%" targetType="dialog" asc="asc" desc="desc">
		<thead>
			<tr>
				<th orderField="realname" <s:if test="orderField=='realname'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.realname"/></th>
				<th orderField="gender" <s:if test="orderField=='gender'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.gender"/></th>
				<th orderField="email" <s:if test="orderField=='email'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.email"/></th>
				<th orderField="phone" <s:if test="orderField=='phone'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.phone"/></th>
			    <th>选择</th>
			</tr>
		</thead>
		<tbody>
		<s:iterator id="pojo" value="userlist">
			<tr target="sid_entity" rel="${pojo.id}">
				<td>${pojo.realname}</td>
				<td><tsp:selectview name="user.gender" value="${pojo.gender}"/></td>
				<td>${pojo.email}</td>
				<td>${pojo.phone}</td>
				<td>
			<a class="btnSelect" href="javascript:$.bringBack({id:'${pojo.id }',realname:'${pojo.realname}'})" multLookup="empItems" title="选择">选择</a>
				</td>
			</tr>
		 </s:iterator>
		</tbody>
	</table>
	</div>
	<div class="panelBar" >
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="dialogPageBreak({numPerPage:this.value})">
			<option <s:if test="numPerPage==5">selected="selected"</s:if> value="5" >5</option>
				<option <s:if test="numPerPage==10">selected="selected"</s:if> value="10" >10</option>
				<option <s:if test="numPerPage==20">selected="selected"</s:if> value="20" >20</option>
				<option <s:if test="numPerPage==50">selected="selected"</s:if> value="50">50</option>
				<option <s:if test="numPerPage==100">selected="selected"</s:if> value="100">100</option>
				<option <s:if test="numPerPage==200">selected="selected"</s:if> value="200">200</option>
			</select>
			<span>条，共${totalCount}条</span>
		</div>
		
		<div class="pagination" targetType="dialog" totalCount="${totalCount}" numPerPage="${numPerPage}" pageNumShown="10" currentPage="${pageNum}"></div>

	</div>
</div>

