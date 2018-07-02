<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<form id="pagerForm" method="post" action="user_list.do">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="user_list.do" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label><tsp:transproname proname="user.realname"/>：</label>
				<input type="text" name="user.realname" value=""/>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
				<li><a class="button" href="pages/user/userquery.jsp" target="dialog" mask="true" title="查询框"><span>高级检索</span></a></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="user_insertUI.do" target="navTab"><span>添加</span></a></li>
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="userids" postType="string" href="user_del.do" class="delete"><span>删除</span></a></li>
			<li><a class="edit" href="user_updateUI.do?user.id={sid_entity}" target="navTab" warn="请选择一条"><span>修改</span></a></li>
			<li><a class="edit" href="user_view.do?user.id={sid_entity}" target="navTab" warn="请选择一条"><span>查看</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>
	<div layoutH="116" id="w_list_print">
	<table class="table" width="98%" targetType="navTab" asc="asc" desc="desc">
		<thead>
			<tr>
			    <th width="22"><input type="checkbox" group="userids" class="checkboxCtrl"></th>
				<th orderField="id" <s:if test="orderField=='id'">class="${orderDirection}"</s:if> title="排序">编号</th> 
				<th orderField="userid" <s:if test="orderField=='userid'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.userid"/></th>
				<th orderField="realname" <s:if test="orderField=='realname'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.realname"/></th>
				<th orderField="gender" <s:if test="orderField=='gender'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.gender"/></th>
				<th orderField="birthdate" <s:if test="orderField=='birthdate'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.birthdate"/></th>
				<th orderField="email" <s:if test="orderField=='email'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.email"/></th>
				<th orderField="phone" <s:if test="orderField=='phone'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.phone"/></th>
			   	<th orderField="usertype" <s:if test="orderField=='usertype'">class="${orderDirection}"</s:if>><tsp:transproname proname="user.usertype"/></th>
			    <th>操作</th>
			</tr>
		</thead>
		<tbody>
		<s:iterator id="pojo" value="userlist">
			<tr target="sid_entity" rel="${pojo.id}" ondblclick="javascript:dbltable('user_view.do?user.id=${pojo.id}', 'pdialogid' ,'查看', 800, 600);">
			    <td><input name="userids" value="${pojo.id}" type="checkbox"></td>
				<td>${pojo.id}</td>
				<td>${pojo.userid}</td>
				<td>${pojo.realname}</td>
				<td><tsp:selectview name="user.gender" value="${pojo.gender}"/></td>
				<td> <fmt:formatDate value="${pojo.birthdate}" pattern="yyyy-MM-dd"/></td>
				<td>${pojo.email}</td>
				<td>${pojo.phone}</td>
				<td><tsp:selectview name="user.usertype" value="${pojo.usertype}"/></td>
				<td>
				    <a title="删除" target="ajaxTodo" href="user_del.do?userids=${pojo.id}" class="btnDel">删除</a>
					<a title="编辑"  target="navTab" mask="true" href="user_updateUI.do?user.id=${pojo.id}" class="btnEdit">编辑</a>
					<a title="查看"  target="navTab" mask="true" href="user_view.do?user.id=${pojo.id}">查看</a>
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

