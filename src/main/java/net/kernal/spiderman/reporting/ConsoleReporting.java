package net.kernal.spiderman.reporting;

import java.io.PrintStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.kernal.spiderman.K;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.downloader.Downloader.Request;
import net.kernal.spiderman.downloader.Downloader.Response;
import net.kernal.spiderman.parser.Parser;
import net.kernal.spiderman.task.Task;

/**
 * 控制台报告者, 会把报告信息在控制台打印出来
 * @author 赖伟威 l.weiwei@163.com 2015-12-01
 * @author 赖伟威 l.weiwei@163.com 2015-12-30
 *
 */
public class ConsoleReporting implements Reporting {

	private long startAt;
	
	public void reportStart() {
		this.startAt = System.currentTimeMillis();
		System.out.println("[Spiderman][开始]"+K.formatNow()+"\r\n");
	}
	
	public void reportDownload(Response response) {
		StringBuilder sb = new StringBuilder("[Spiderman][下载网页]:"+K.formatNow()+"\r\n  ["+response.getStatusCode()+"]"+response.getRequest().getUrl());
		if (K.isNotBlank(response.getLocation())) {
			sb.append("\r\n  redirect ").append(response.getLocation());
		} 
		System.out.println(sb.toString()+"\r\n");
	}

	public void reportNewTask(Task newTask) {
		System.out.println("[Spiderman][创建"+newTask.getType()+"任务]"+K.formatNow()+"\r\n  "+newTask.getRequest().getUrl()+"\r\n");
	}

	public void reportParsedResult(Parser.ParsedResult parsedResult) {
		System.err.println("[Spiderman][解析结果]"+K.formatNow()+"\r\n  "+JSON.toJSONString(parsedResult.first(), SerializerFeature.PrettyFormat)+"\r\n");
	}
	
	public void reportStop(Spiderman.Counter counter) {
		long cost = System.currentTimeMillis() - this.startAt;
		String fmt = "\r\n\r\n\r\n[Spiderman][结束]%s\r\n  总共花费时间:%sms \r\n  重复校验队列:%s \r\n  下载(主)线程池: 总数(%s) 运行中(%s) 已完成(%s) \r\n  下载(次)线程池: 总数(%s) 运行中(%s) 已完成(%s) \r\n  解析(主)线程池: 总数(%s) 运行中(%s) 已完成(%s) \r\n  解析(次)线程池: 总数(%s) 运行中(%s) 已完成(%s) \r\n  计数器(主): 已下载(%s) 已解析目标(%s) 待下载(%s) 待解析(%s) \r\n  计数器(次): 已下载(%s) 已解析目标(%s) 待下载(%s) 待解析(%s)\r\n\r\n\r\n";
		System.out.println(String.format(fmt, K.formatNow(), cost, counter.getCountDuplicateCheckQueue().get(),
				counter.getPrimaryDownloadPool().getPoolSize(), counter.getPrimaryDownloadPool().getActiveCount(), counter.getPrimaryDownloadPool().getCompletedTaskCount(),
				counter.getSecondaryDownloadPool().getPoolSize(), counter.getSecondaryDownloadPool().getActiveCount(), counter.getSecondaryDownloadPool().getCompletedTaskCount(),
				counter.getPrimaryParsePool().getPoolSize(), counter.getPrimaryParsePool().getActiveCount(), counter.getPrimaryParsePool().getCompletedTaskCount(),
				counter.getSecondaryParsePool().getPoolSize(), counter.getSecondaryParsePool().getActiveCount(), counter.getSecondaryParsePool().getCompletedTaskCount(),
				counter.getCountPrimaryDownload().get(), counter.getCountPrimaryParsed(), counter.getCountPrimaryDownloadQueue().get(), counter.getCountPrimaryParseQueue().get(),
				counter.getCountSecondaryDownload().get(), counter.getCountSecondaryParsed(), counter.getCountSecondaryDownloadQueue().get(), counter.getCountSecondaryParseQueue().get()
				));
	}

	public void reportDuplicateCheck(String key, boolean duplicate, Request request) {
		PrintStream ps = System.out;
		ps.println("\r\n[Spiderman][重复校验]"+K.formatNow()+"\r\n  key:"+key+" \r\n  duplicate:"+duplicate+" \r\n  url:"+request.getUrl()+"\r\n");
	}

}
