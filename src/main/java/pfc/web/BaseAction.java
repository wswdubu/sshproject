package pfc.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pfc.bean.User;
import pfc.util.Pager;

public class BaseAction extends ActionSupport  {
	public Integer pageNum=1;

	public Integer currentPage = 1; // 当前是第几页
	
	public String targetType="navTab"; // navTab或dialog，用来标记是navTab上的分页还是dialog上的分页

	public Integer totalCount=0; // 总条数

	public Integer numPerPage=10; // 每页显示多少条

	public Integer pageNumShown=10; // 页标数字多少个

	public Integer sysusersId;
	
	public Integer pageCount = 0; // 总页数
	
	public String ids;
	
	public String names;
	
	public String orderField;//排序字段
	
	public String orderDirection;//排序方向
	
	public String tid;
	
	private Subject subject;//当前主题,相当于当前登录的用户
	
	
	
	public String jsonstring;
	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式化
	SimpleDateFormat ss=new SimpleDateFormat("yyyy-MM-dd");//日期格式化

	
	public Pager pager;//分页实体
	/**
	 * 获取当前主题
	 * */
	public Subject getMySubject(){
		return SecurityUtils.getSubject();
	}
	public void returnjson(String objlist){
		JSONObject object=new JSONObject();
		object.put("statusCode", "200");
		object.put("message", OPERATIONSUCCESS);
		object.put("navTabId", objlist);
		object.put("rel", "");
		object.put("callbackType", "closeCurrent");
		object.put("forwardUrl", "");
		out(object);
	}
	public int getWeekOfDate(){
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return w;
	}
	public void returnjsondel(String objlist){
		JSONObject object=new JSONObject();
		object.put("statusCode", "200");
		object.put("message", OPERATIONSUCCESS);
		object.put("navTabId", objlist);
		object.put("rel", "");
		object.put("forwardUrl", "");
		out(object);
	}
	/**
	 * 获取当前登录用户
	 * */
	public static User currentuser(){
		if(SecurityUtils.getSubject().getSession()!=null){
			User user = (User)SecurityUtils.getSubject().getSession().getAttribute("currentuser");//当session中获取当前用户的详细信息
			return user;
		}
		return null;
	}
	public void page() {
		totalCount=pager.getTotalCount();
		pageCount = totalCount % numPerPage == 0 ? totalCount / numPerPage: totalCount / numPerPage + 1;
	}
	public Timestamp getCurrentTime(){//获取当前时间
		return new Timestamp(System.currentTimeMillis());
	}
	public void out(JSONObject object){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
	    try {
			PrintWriter out=response.getWriter();
			out.print(object);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public long getMinBetweenDate(String datestart,String dateend){
		long between = 0;
        try {
            java.util.Date begin = s.parse(datestart);
            java.util.Date end = s.parse(dateend);
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return between/(60*1000);
	}
	public void out(String object){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
	    try {
			PrintWriter out=response.getWriter();
			out.print(object);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//生成饼图
	public static JSONObject genPie(String data, String datavalue, String title) {
		JSONObject optionJson = new JSONObject();
		// 数据项json数组
		JSONArray datajsonArray = new JSONArray();
		for (String str : data.split(",")) {
			datajsonArray.add(str);
		}
		// 标题json数组
		JSONArray titlejsonArray = new JSONArray();
		titlejsonArray.add(title);

		// 数据值json数组
		JSONArray datavaluejsonArray = new JSONArray();
		for (String str : datavalue.split(",")) {
			datavaluejsonArray.add(Integer.valueOf(str));
		}
		JSONArray seriesarry = new JSONArray();
		for (int i = 0; i < datajsonArray.size(); i++) {
			JSONObject datavalueobj = new JSONObject();
			datavalueobj.put("name", datajsonArray.get(i));
			datavalueobj.put("value", datavaluejsonArray.get(i));
			seriesarry.add(datavalueobj);
		}
        //title配置
		JSONObject tjson = new JSONObject();
		tjson.put("text", title);
		tjson.put("x", "center");
		
		optionJson.put("title", tjson);
		// tooltip配置
		JSONObject tooltipobj = new JSONObject();
		tooltipobj.put("trigger", "item");
		tooltipobj.put("formatter", "{b} : <br/>{c} ({d}%)");
		optionJson.put("tooltip", tooltipobj);
		// legend配置
		JSONObject legendobj = new JSONObject();
		legendobj.put("trigger", "item");
		legendobj.put("y", "bottom");
		legendobj.put("data", datajsonArray);
		legendobj.put("borderColor", "#ccc");
		legendobj.put("borderWidth", "1");
		optionJson.put("legend", legendobj);
		optionJson.put("calculable", "true");

		// 数据值设置
		
		String formatter = "function (params){return  params.name +','+ (params.percent - 0).toFixed(0) + '%'}";
		
		JSONArray series = new JSONArray();
		JSONObject seriesObj = new JSONObject();
		seriesObj.put("name", titlejsonArray);
		seriesObj.put("type", "pie");
		seriesObj.put("radius", "39%");
		seriesObj.put("center", "['50%', '50%']");
		JSONObject label = new JSONObject();
		label.put("formatter", formatter);
		JSONObject normal = new JSONObject();
		normal.put("label", label);
		JSONObject itemStyle = new JSONObject();
		itemStyle.put("normal", normal);
		seriesObj.put("data", seriesarry);
		seriesObj.put("itemStyle", itemStyle);
		series.add(seriesObj);
		optionJson.put("series", series);
		return optionJson;
	}
	
	//生成柱状图
	public static JSONObject genBar(String xdata, List<String> ydatas, List<String> titles) {
		JSONObject optionJson = new JSONObject();
		// 数据项json数组
		JSONArray datajsonArray = new JSONArray();
		for (String str : xdata.split(",")) {
			datajsonArray.add(str);
		}
		JSONArray series = new JSONArray();
		JSONArray titlejsonArray = new JSONArray();
		for (int i = 0; i < ydatas.size(); i++) {
			JSONArray datavaluejsonArray = new JSONArray();
			for (String str : ydatas.get(i).split(",")) {
				datavaluejsonArray.add(Integer.valueOf(str));
			}
			JSONObject seriesObj = new JSONObject();
			if(titles.get(i).contains("@")){
				titlejsonArray.add(titles.get(i).subSequence(0, titles.get(i).indexOf("@")));
				seriesObj.put("name", titles.get(i).subSequence(0, titles.get(i).indexOf("@")));
				seriesObj.put("stack", titles.get(i).substring(titles.get(i).indexOf("@")+1));
			}else{
				titlejsonArray.add(titles.get(i));
				seriesObj.put("name", titles.get(i));
			}
			seriesObj.put("type", "bar");
			seriesObj.put("data", datavaluejsonArray);
			series.add(seriesObj);
		}
		
		// legend配置
		JSONObject legendobj = new JSONObject();
		legendobj.put("data", titlejsonArray);
		legendobj.put("y", "10px;");
		optionJson.put("legend", legendobj);
		//调整位置
		JSONObject grid = new JSONObject();
        grid.put("y2", 90);//距离底部距离
        grid.put("y", 30);//距离顶部距离
        optionJson.put("grid", grid);
		// tooltip配置
		JSONObject tooltipobj = new JSONObject();
		tooltipobj.put("trigger", "item");
		tooltipobj.put("formatter", "{a} {c}");
		optionJson.put("tooltip", tooltipobj);
		// x轴数据项配置
		JSONArray xAxis = new JSONArray();
		JSONObject xObj = new JSONObject();
		xObj.put("type", "category");
		xObj.put("data", datajsonArray);
		xObj.put("splitLine", "false");//去掉网格属性
		JSONObject axisLabel = new JSONObject();
		axisLabel.put("interval", 0);
		axisLabel.put("rotate", -40);
		xObj.put("axisLabel", axisLabel);
		xAxis.add(xObj);
		optionJson.put("xAxis", xAxis);
		// y轴数据项配置
		JSONArray yAxis = new JSONArray();
		JSONObject yObj = new JSONObject();
		yObj.put("type", "value");
		yObj.put("splitLine", "false");//去掉网格属性
		
		yAxis.add(yObj);
		optionJson.put("yAxis", yAxis);
		
		optionJson.put("series", series);
		return optionJson;
	}
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Integer getPageNumShown() {
		return pageNumShown;
	}

	public void setPageNumShown(Integer pageNumShown) {
		this.pageNumShown = pageNumShown;
	}

	public Integer getSysusersId() {
		return sysusersId;
	}

	public void setSysusersId(Integer sysusersId) {
		this.sysusersId = sysusersId;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
	
	
	



	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}



	public String getJsonstring() {
		return jsonstring;
	}
	public void setJsonstring(String jsonstring) {
		this.jsonstring = jsonstring;
	}

	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}





	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}









	public static final String OPERATIONSUCCESS="操作成功";
	public static final String OPERATIONFAILURE="操作失败";
	public static final String DELETESUCCESS="删除成功";
	public static final String STARTSUCCESS="启用成功";
	public static final String STOPSUCCESS="停用成功";
	public static final String RESETPASSWORDSUCCESS="重置密码成功";
	public static final String OPERATYPE_UPDATE="修改";
	public static final String OPERATYPE_INSERT="增加";
	public static final String OPERATYPE_DEL="删除";
	public static final String OPERATYPE_LOGIN="登录";
	public static final String OPERATYPE_RESETPASSWORD="重置密码";
	public static final String OPERATYPE_START="启用";
	public static final String OPERATYPE_STOP="停用";
	public static final String OPERATYPE_LOGOUT="注销";
	public static final String LOGTYPE_SYS="系统管理";
	public static final String LOGTYPE_OFFDOC="公文管理";
	
}
