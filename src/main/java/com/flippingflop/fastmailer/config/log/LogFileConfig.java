package com.flippingflop.fastmailer.config.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@ConditionalOnProperty(name = "log.file.enabled", havingValue = "true")
public class LogFileConfig {

    private static final int MAX_HISTORY_FILE_AMOUNT = 30;

    @Value("${log.file.root-dir}")
    public String logRootDir;

    @PostConstruct
    public void postConstruct() {
        try {
            /* Normalize variables */
            if (!logRootDir.endsWith("/") && !logRootDir.endsWith("\\")) {
                logRootDir = logRootDir + getDirectorySeparator();
            }

            /* Log Configurations */
            // Context initialization.
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.reset();

            // Live log config.
            RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
            fileAppender.setContext(loggerContext);
            fileAppender.setName("timestamp-file-appender");
            fileAppender.setFile(logRootDir + "fastmailer.log");

            // Archiving config.
            TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
            rollingPolicy.setContext(loggerContext);
            rollingPolicy.setParent(fileAppender);
            rollingPolicy.setFileNamePattern(logRootDir + "archived" + getDirectorySeparator() + "fastmailer-%d{yyyy-MM-dd}.%i.log");
            rollingPolicy.setMaxHistory(MAX_HISTORY_FILE_AMOUNT);

            // File size and time-based triggering policy.
            SizeAndTimeBasedFNATP<ILoggingEvent> fileNameAndTriggeringPolicy = new SizeAndTimeBasedFNATP<>();
            fileNameAndTriggeringPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(fileNameAndTriggeringPolicy);
            rollingPolicy.start();
            fileNameAndTriggeringPolicy.start();

            // Log format config.
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(loggerContext);
            encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
            encoder.start();

            fileAppender.setEncoder(encoder);
            fileAppender.setRollingPolicy(rollingPolicy);
            fileAppender.start();

            // Set log level.
            Logger rootLogger = loggerContext.getLogger("ROOT");
            rootLogger.setLevel(Level.INFO);
            rootLogger.addAppender(fileAppender);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Get directory separator such as / or \ depending on the OS.
     * @return
     *  - \\ for windows.<br>
     *  - / for others.
     */
    private String getDirectorySeparator() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return "\\";
        } else {
            return "/";
        }
    }

}
