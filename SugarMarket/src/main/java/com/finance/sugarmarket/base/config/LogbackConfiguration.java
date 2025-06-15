package com.finance.sugarmarket.base.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.logtail.logback.LogtailAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class LogbackConfiguration {

    @Value("${logtail.sourceToken:}")
    private String logtailToken;

    @Value("${logtail.ingestUrl:}")
    private String logtailUrl;

    @PostConstruct
    public void configureLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        // Configure root logger
        ch.qos.logback.classic.Logger rootLogger = context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);

        // Check if Logtail is configured
        if (isLogtailConfigured()) {
            // Log only to Betterstack/Logtail in production
            LogtailAppender logtailAppender = createLogtailAppender(context);
            rootLogger.addAppender(logtailAppender);
            logtailAppender.start();

            // Optional: Add a log statement to indicate we're using Logtail
            System.out.println("Logging configured for Betterstack/Logtail only");
        } else {
            // Log only to console when Logtail is not configured
            ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(context);
            rootLogger.addAppender(consoleAppender);
            consoleAppender.start();

            // Optional: Add a log statement to indicate we're using console
            System.out.println("Logging configured for Console only");
        }
    }

    private boolean isLogtailConfigured() {
        return StringUtils.hasText(logtailToken) && StringUtils.hasText(logtailUrl);
    }

    private ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext context) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setName("Console");

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n");
        encoder.start();

        consoleAppender.setEncoder(encoder);
        return consoleAppender;
    }

    private LogtailAppender createLogtailAppender(LoggerContext context) {
        LogtailAppender logtailAppender = new LogtailAppender();
        logtailAppender.setContext(context);
        logtailAppender.setName("Logtail");
        logtailAppender.setAppName("MyApp");
        logtailAppender.setSourceToken(logtailToken);
        logtailAppender.setIngestUrl(logtailUrl);
        logtailAppender.setMdcFields("requestId,requestTime");
        logtailAppender.setMdcTypes("string,int");

        return logtailAppender;
    }

    // Alternative Spring Bean approach (if needed)
    @Bean
    @ConditionalOnProperty(name = {"logtail.sourceToken", "logtail.ingestUrl"})
    public LogtailAppender logtailAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return createLogtailAppender(context);
    }
}