<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<form id="pagerForm" method="post" action="variable_listlooks.do">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dwzSearch(this, 'dialog');" action="variable_listlooks.do" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label><tsp:transproname proname="variable.varname"/>：</label>
				<input type="text" name="variable.varname" value=""/>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" multLookup="variable" warn="请选择">选择带回</button></div></div></li>
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
			    <th width="22"><input type="checkbox" group="variable" class="checkboxCtrl"></th>
				<th orderField="id" <s:if test="orderField=='id'">class="${orderDirection}"</s:if> title="排序">编号</th> 
				<th orderField="varname" <s:if test="orderField=='varname'">class="${orderDirection}"</s:if>><tsp:transproname proname="variable.varname"/></th>
				<th orderField="varvalue" <s:if test="orderField=='varvalue'">class="${orderDirection}"</s:if>><tsp:transproname proname="variable.varvalue"/></th>
				<th orderField="returnname" <s:if test="orderField=='returnname'">class="${orderDirection}"</s:if>><tsp:transproname proname="variable.returnname"/></th>
			</tr>
		</thead>
		<tbody>
		<s:iterator id="pojo" value="variablelist">
			<tr target="sid_entity" rel="${pojo.id}">
			    <td><input name="variable" value="{ids:'${pojo.id}', varname:'${pojo.varname}'}" type="checkbox"></td>
				<td>${pojo.id}</td>
				<td>${pojo.varname}</td>
				<td>${pojo.varvalue}</td>
				<td>${pojo.returnname}</td>
			</tr>
		 </s:iterator>
		</tbody>
	</table>
	</div>
</div>

