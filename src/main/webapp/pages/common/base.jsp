<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<%@ taglib prefix="tsp" uri="http://www.pfc.com"%>
<%@page import="pfc.util.*"%>
<script src="<%=basePath %>pages/common/base.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript" src="<%=basePath %>pages/js/datepicker/WdatePicker.js"></script>


