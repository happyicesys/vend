package weixin.popular.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.ado.SqlADO;

import beans.clsGroupBean;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.token.Token;

/**
 * TokenManager token 自动刷新
 * @author LiYi
 *
 */
public class TokenManager{

	private static ScheduledExecutorService scheduledExecutorService;

	private static Map<String,String> tokenMap = new LinkedHashMap<String,String>();

	private static Map<String,ScheduledFuture<?>> futureMap = new HashMap<String, ScheduledFuture<?>>();

	private static int poolSize = 2;
	
	private static boolean daemon = Boolean.TRUE;

	/**
	 * 初始化 scheduledExecutorService
	 */
	private static void initScheduledExecutorService(){
		scheduledExecutorService =  Executors.newScheduledThreadPool(poolSize,new ThreadFactory() {

			@Override
			public Thread newThread(Runnable arg0) {
				Thread thread = Executors.defaultThreadFactory().newThread(arg0);
				//设置守护线程
				thread.setDaemon(daemon);
				return thread;
			}
		});
	}

	/**
	 * 设置线程池
	 * @param poolSize
	 */
	public static void setPoolSize(int poolSize){
		TokenManager.poolSize = poolSize;
	}
	
	/**
	 * 设置线程方式
	 * @param daemon
	 */
	public static void setDaemon(boolean daemon) {
		TokenManager.daemon = daemon;
	}

	/**
	 * 初始化token 刷新，每118分钟刷新一次。
	 * @param appid
	 * @param secret
	 */
	public static void init(final String appid,final String secret){
		if(scheduledExecutorService == null){
			initScheduledExecutorService();
		}
		if(futureMap.containsKey(appid)){
			futureMap.get(appid).cancel(true);
		}
		ScheduledFuture<?> scheduledFuture =  scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Token token = TokenAPI.token(appid,secret);
				tokenMap.put(appid,token.getAccess_token());
			}
		},0,118,TimeUnit.MINUTES);
		futureMap.put(appid, scheduledFuture);
	}

	/**
	 * 取消 token 刷新
	 */
	public static void destroyed(){
		scheduledExecutorService.shutdownNow();
	}

	/**
	 * 获取 access_token
	 * @param appid
	 * @return
	 */
	public static String getToken(String appid){
		return tokenMap.get(appid);
	}

	/**
	 * 获取第一个appid 的 access_token
	 * 适用于单一微信号
	 * @param appid
	 * @return
	 */
	public static String getDefaultToken(){
		Object[] objs = tokenMap.values().toArray();
		return objs.length>0?objs[0].toString():null;
	}
	
	private static Map<String, Token> m_token=new HashMap<String, Token>();
	public static String FreshToken(clsGroupBean bean) 
	{
		boolean need_refresh=false;
		if(bean==null)
		{
			return null;
		}
		
		long current=System.currentTimeMillis()/1000;
		Token token=m_token.get(bean.getGroupname()+bean.getId());
		if(token==null)
		{
			need_refresh=true;
		}
		else
		{
			if(token.getNext_gmt_time()<current)
			{
				need_refresh=true;
			}			
		}
		
		if(need_refresh)
		{
			
			token = TokenAPI.token(bean.getWx_appid(),bean.getAppsecret());
			
			if(token==null)
			{
				return null;
			}
			else
			{
				token.setNext_gmt_time((int)current+7200-10);
				m_token.put(bean.getGroupname()+bean.getId(), token);
			}
		}
		System.out.println(token.getAccess_token());
		return token.getAccess_token();
	}

}
