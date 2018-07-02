package pfc.service.log.impl;

import org.aspectj.lang.JoinPoint;

import pfc.service.log.LogService;

public class LogServiceImpl implements LogService {

	long startTime;
	long endTime;
	public void log() {
		startTime=System.currentTimeMillis();   //获取开始时间
	}

	public void logArg(JoinPoint point) {
		// 获取目标方法签名   
		String signature = point.getSignature().toString();   
		// 获取方法名   
		String methodName = signature.substring(signature.lastIndexOf(".") + 1,signature.indexOf("(")); 
		endTime=System.currentTimeMillis(); //获取结束时间
		/*log=new SysOffdoclog();
		log.setOperationTime(new Timestamp(System.currentTimeMillis()));
		if(SysContent.getSession()!=null){
			SysUser user=(SysUser) SysContent.getSession().getAttribute("currentuser");
			log.setSysUser(user);
		}
		log.setOperationType(methodName);
		log.setOperationDesc(point.getTarget().getClass().toString());*/
		//dao.save(log);
		System.out.println(point.getTarget().getClass().toString()+"类的 "+methodName+"方法运行时间： "+(endTime-startTime)+"ms");
	}

	//有参并有返回值的方法

	    public void logArgAndReturn(JoinPoint point, Object returnObj) {
	       //此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
	        Object[] args = point.getArgs();
	        System.out.println("目标参数列表：");
	        if (args != null) {
	        	for (Object obj : args) {
	               System.out.println(obj + ",");
	           }
	            System.out.println();
	            System.out.println("执行结果是：" + returnObj);
	        }
	    }

}
