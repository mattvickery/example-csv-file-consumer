package com.gds;

import com.gds.batch.SpringBatchJobExecutionContext;
import com.gds.batch.SpringBatchJobExecutor;
import com.gds.batch.StepExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.aggregator.AggregatingMessageHandler;
import org.springframework.integration.aggregator.HeaderAttributeCorrelationStrategy;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.state;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 02/10/2017
 */
@ImportResource("classpath:META-INF/spring/batch/csv-file-writer.xml")
@EnableIntegration
@PropertySource("classpath:properties/polling-file-consumer.properties")
public class FileAggregatorConfiguration {

    @Value("${inbound.path.dir}")
    private String inboundFilePathName;
    @Value("${inbound.registered.extension}")
    private String registeredExtension;
    @Value("${inbound.registered.prefix}")
    private String registeredPrefix;

    private static final String CORRELATION_ID_HEADER_KEY = "correlationId";
    private static final String FILE_NAME_HEADER_KEY = "fileName";

    @Autowired
    private SpringBatchJobExecutor springBatchJobExecutor;
    @Autowired
    private ApplicationContext ctx;

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "2000"))
    public MessageSource<File> fileSystemMessageSource() {

        final FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
        fileReadingMessageSource.setDirectory(new File(inboundFilePathName));
        fileReadingMessageSource.setFilter(new CompositeFileListFilter<File>()
                .addFilter(new AcceptOnceFileListFilter<>())
                .addFilter(files -> Arrays.asList(files).stream()
                        .filter(f -> f.getName().toLowerCase().endsWith(registeredExtension))
                        .filter(f -> f.getName().toLowerCase().startsWith(registeredPrefix))
                        .collect(Collectors.toList())));
        fileReadingMessageSource.setScanEachPoll(true);
        return fileReadingMessageSource;
    }

    @Bean
    @Transformer(inputChannel = "fileInputChannel", outputChannel = "aggregatorChannel")
    public HeaderEnricher enrichHeaders() {

        Map<String, HeaderValueMessageProcessor<?>> headersToAdd = new HashMap<>();
        headersToAdd.put(CORRELATION_ID_HEADER_KEY, new StaticHeaderValueMessageProcessor<>("1"));
        Expression expression = new SpelExpressionParser().parseExpression("payload.getName()");
        headersToAdd.put(FILE_NAME_HEADER_KEY,
                new ExpressionEvaluatingHeaderValueMessageProcessor<>(expression, String.class));
        HeaderEnricher enricher = new HeaderEnricher(headersToAdd);
        return enricher;
    }

    @Bean
    @ServiceActivator(inputChannel = "aggregatorChannel")
    public MessageHandler aggregateFiles() {

        final AggregatingMessageHandler handler = new AggregatingMessageHandler(
                loggingMessageGroupProcessor(),
                defaultMessageGroupStore(),
                new HeaderAttributeCorrelationStrategy(CORRELATION_ID_HEADER_KEY),
                group -> group.getMessages().size() == 2);
        handler.setOutputChannelName("loggerChannel");
        handler.setExpireGroupsUponCompletion(true);
        return handler;
    }

    @Bean
    @ServiceActivator(inputChannel = "loggerChannel")
    public MessageHandler messagePrinter() {
        return message -> {
            System.out.print("Messages released:");
            message.getHeaders().entrySet().stream().forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));
        };
    }

    @Bean
    public MessageGroupProcessor loggingMessageGroupProcessor() {

        return group -> {
            state(group.getMessages().size() == 2, "Internal Error: Incorrect group size.");
            final List<Message<?>> messages = new ArrayList<>(group.getMessages());
            messages.forEach(message -> {
                final String fileName = (String) message.getHeaders().get(FILE_NAME_HEADER_KEY);
                final SpringBatchJobExecutionContext executionContext;
                if (fileName.toLowerCase().startsWith(registeredPrefix + "customer")) {
                    // Better to look this up as a bean instead.
                    executionContext
                            = new SpringBatchJobExecutionContext(new FileSystemResource((File) message.getPayload()), "customer");
                    executionContext.resolve(ctx);
                } else if (fileName.toLowerCase().startsWith(registeredPrefix + "product")) {
                    // Better to look this up as a bean instead.
                    executionContext
                            = new SpringBatchJobExecutionContext(new FileSystemResource((File) message.getPayload()), "product");
                    executionContext.resolve(ctx);
                } else {
                    throw new IllegalStateException("Unexpected file consumed for processing.");
                }
                final StepExecutionResponse response = springBatchJobExecutor.execute(executionContext);
                System.out.println(response.getExceptionMessages().stream().collect(Collectors.joining(">>")));
            });
            return (MessageGroupProcessor) mgp -> "group processing completed";
        };
    }

    @Bean
    public SimpleMessageStore defaultMessageGroupStore() {
        return new SimpleMessageStore();
    }
}