package ru.sendto.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import ru.sendto.logger.dto.TextLogDto;

public class LoggerProducer {

	@Inject @Fatal Event<TextLogDto> severe;
	@Inject @Warning Event<TextLogDto> warning;
	@Inject @Info Event<TextLogDto> info;
	@Inject @Trace Event<TextLogDto> trace;
	@Inject @Off Event<TextLogDto> off;
	
	@Inject Event<Logger> loggerBus;

	@Produces @Named String levChatId = "121462543";
	@Produces @Named String talismanovChatId = "190199592";
	
	@Produces @Named String traceChatId="-193307170";
	@Produces @Named String infoChatId="-193307170";
	@Produces @Named String warnChatId="-193307170";
	@Produces @Named String fatalChatId="-156008876";
	
	

	@PostConstruct
	public void initOtherLoggers(){
		Logger logger = Logger.getGlobal();
		addHandler(logger);
		loggerBus.fire(logger);
	}
	
	@Produces
	@Default
	public Logger produceLog(InjectionPoint injectionPoint) {
		Logger logger = Logger.getLogger(
				injectionPoint
				.getMember()
				.getDeclaringClass()
				.getSimpleName());
		addHandler(logger);
		return logger;
	}

	private void addHandler(Logger logger) {
		logger.addHandler(new Handler() {
			
			@Override
			public void publish(LogRecord record) {
				Event<TextLogDto> bus = off;
				int i = record.getLevel().intValue()/100;
				i=i<0?0:i;
				switch (i) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					bus = trace;
					break;
				case 6:
				case 7:
				case 8:
					bus = info;
					break;
				case 9:
					bus = warning;
					break;
				case 10:
					bus = severe;
					break;
				default:
					break;
				}
				StringBuilder sb = new StringBuilder(record.getSourceClassName())
						.append(".")
						.append(record.getSourceMethodName())
						.append("(): ")
						.append(record.getMessage())
						.append(";\n");
				for(StackTraceElement ste : record.getThrown().getStackTrace()){
					sb.append(ste.toString());
				}
				final int MAX_LEN = 4000;
				bus.fire(new TextLogDto().setText(sb.substring(0, sb.length()>MAX_LEN?MAX_LEN:sb.length()).toString()));
			}
			
			@Override
			public void flush() {
			}
			
			@Override
			public void close() throws SecurityException {
			}
		});
	}
}
