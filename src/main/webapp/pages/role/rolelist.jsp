<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<form id="pagerForm" method="post" action="role_list.do">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="role_list.do" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label><tsp:transproname proname="role.rolename"/>：</label>
				<input type="text" name="role.rolename" value=""/>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
				<li><a class="button" href="pages/role/rolequery.jsp" target="dialog" mask="true" title="查询框"><span>高级检索</span></a></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="role_insertUI.do" target="navTab"><span>添加</span></a></li>
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="roleids" postType="string" href="role_del.do" class="delete"><span>删除</span></a></li>
			<li><a class="edit" href="role_updateUI.do?role.id={sid_entity}" target="navTab" warn="请选择一条"><span>修改</span></a></li>
			<li><a class="edit" href="role_view.do?role.id={sid_entity}" target="navTab" warn="请选择一条"><span>查看</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>
	<div layoutH="116" id="w_list_print">
	<table class="table" width="98%" targetType="navTab" asc="asc" desc="desc">
		<thead>
			<tr>
			    <th width="22"><input type="checkbox" group="roleids" class="checkboxCtrl"></th>
				<th orderField="id" <s:if test="orderField=='id'">class="${orderDirection}"</s:if> title="排序">编号</th> 
				<th orderField="rolename" <s:if test="orderField=='rolename'">class="${orderDirection}"</s:if>><tsp:transproname proname="role.rolename"/></th>
				<th orderField="roledesc" <s:if test="orderField=='roledesc'">class="${orderDirection}"</s:if>><tsp:transproname proname="role.roledesc"/></th>
			    <th>操作</th>
			</tr>
		</thead>
		<tbody>
		<s:iterator id="pojo" value="rolelist">
			<tr target="sid_entity" rel="${pojo.id}" ondblclick="javascript:dbltable('role_view.do?role.id=${pojo.id}', 'pdialogid' ,'查看', 800, 600);">
			    <td><input name="roleids" value="${pojo.id}" type="checkbox"></td>
				<td>${pojo.id}</td>
				<td>${pojo.rolename}</td>
				<td>${pojo.roledesc}</td>
				<td>
				    <a title="删除" target="ajaxTodo" href="role_del.do?roleids=${pojo.id}" class="btnDel">删除</a>
					<a title="编辑"  target="navTab" mask="true" href="role_updateUI.do?role.id=${pojo.id}" class="btnEdit">编辑</a>
					<a title="查看"  target="navTab" mask="true" href="role_view.do?role.id=${pojo.id}">查看</a>
				</td>
			</tr>
		 </s:iterator>
		</tbody>
	</table>
	</div>
	<div class="panelBar" >
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
			<option <s:if test="numPerPage==5">selected="selected"</s:if> value="5" >5</option>
				<option <s:if test="numPerPage==10">selected="selected"</s:if> value="10" >10</option>
				<option <s:if test="numPerPage==20">selected="selected"</s:if> value="20" >20</option>
				<option <s:if test="numPerPage==50">selected="selected"</s:if> value="50">50</option>
				<option <s:if test="numPerPage==100">selected="selected"</s:if> value="100">100</option>
				<option <s:if test="numPerPage==200">selected="selected"</s:if> value="200">200</option>
			</select>
			<span>条，共${totalCount}条</span>
		</div>
		
		<div class="pagination" targetType="${targetType}" totalCount="${totalCount}" numPerPage="${numPerPage}" pageNumShown="10" currentPage="${pageNum}"></div>

	</div>
</div>

