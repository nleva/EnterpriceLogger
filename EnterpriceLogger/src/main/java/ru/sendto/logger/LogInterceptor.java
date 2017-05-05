package ru.sendto.logger;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Log
@Priority(Interceptor.Priority.APPLICATION)
public class LogInterceptor {

//	@Inject
	Logger log;

	
	
	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		
//		log.set
		log = Logger.getLogger(ic.getTarget().getClass().getSimpleName());
		Log cfg = null;
		cfg = ic.getTarget().getClass().getAnnotation(Log.class);
		Log methodCfg = ic.getMethod().getAnnotation(Log.class);
		cfg = methodCfg==null?cfg:methodCfg;
		
//		log.getGlobal()
		
		long ts=System.nanoTime();
		
		
		Object res =null;
		String resString;
		
		
		try{
			res = ic.proceed();
			
		}finally {
			resString = ic.getMethod().getReturnType().equals(Void.TYPE)?"void":""+res;
			
			String time = cfg.logTime()?
					((System.nanoTime()-ts)/1000/1000f+ "ms ") : "";
			switch (cfg.maxResultLength()) {
			case -1://Do nothing
				break;
			case 0:
				resString="";
				break;
			default:
				resString=resString.substring(0, Math.min(cfg.maxResultLength(), resString.length()));
				break;
			}

			String[] paramStrings = new String[ic.getParameters().length];
			String paramString = "";
			
			for (int i = 0; i < paramStrings.length; i++) {
				switch (cfg.maxParamLength()) {
				case -1:
					paramStrings[i]=""+ic.getParameters()[i];
					break;
				case 0:
					paramStrings[i]="-";
					break;
				default:
					paramStrings[i]=""+ic.getParameters()[i];
					paramStrings[i]=paramStrings[i].substring(0, Math.min(cfg.maxParamLength(), paramStrings[i].length()));
					break;
				}
			}
			paramString=Arrays.toString(paramStrings);
			paramString=paramString.substring(1,paramString.length()-1);
			
			log.fine(time+ic.getTarget().getClass().getSimpleName()+"."+ic.getMethod().getName()
					+"("
					+ paramString
					+ ")"
					+ " -> "
					+ resString);
		}
		return res;
	}
	public static void main(String[] args) {
		System.out.println(Arrays.toString(new String[]{"123","321"}).substring(1,-1));
	}
}
