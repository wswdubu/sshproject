<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%@include file="/pages/common/base.jsp" %>
<base href="<%=basePath%>">
<div class="pageContent">
	<form method="post" action="user_list.do" class="pageForm" onsubmit="return navTabSearch(this);">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><tsp:transproname proname="user.userid"/>：</label>
				<input name="user.userid" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.realname"/>：</label>
				<input name="user.realname" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.password"/>：</label>
				<input name="user.password" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.gender"/>：</label>
				<tsp:select name="user.gender" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.birthdate"/>起：</label>
				<input class="Wdate" name="user.birthdateStart" id="birthdateStart" type="text" maxlength="150" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'birthdateEnd\')||\'2120-10-01\'}'})" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.birthdate"/>止：</label>
				<input class="Wdate" name="user.birthdateEnd" id="birthdateEnd" type="text" maxlength="150" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'birthdateStart\')}'})"/>
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.identifynum"/>：</label>
				<input name="user.identifynum" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.email"/>：</label>
				<input name="user.email" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.phone"/>：</label>
				<input name="user.phone" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.homeaddress"/>：</label>
				<input name="user.homeaddress" type="text" maxlength="150" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.createtime"/>起：</label>
				<input class="Wdate" name="user.createtimeStart" id="createtimeStart" type="text" maxlength="150" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'createtimeEnd\')||\'2120-10-01\'}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.createtime"/>止：</label>
				<input class="Wdate" name="user.createtimeEnd" id="createtimeEnd" type="text" maxlength="150" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'createtimeStart\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			</div>
			<div class="unit">
				<label><tsp:transproname proname="user.description"/>：</label>
				<input name="user.description" type="text" maxlength="150" />
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
