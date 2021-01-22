package com.tibco.be.custom.channel.aws.sqs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.TestEventProcessor;
import com.tibco.be.custom.channel.TestExtendedEventImpl;
import com.tibco.be.custom.channel.TestSimpleEvent;
import com.tibco.be.custom.channel.aws.sqs.serializer.SqsTextSerializer;
import com.tibco.be.custom.channel.framework.CustomChannel;
import com.tibco.be.custom.channel.framework.CustomDestination;
import com.tibco.be.util.BEProperties;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.repo.DeployedBEProject;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.channel.Channel;
import com.tibco.cep.runtime.channel.ChannelConfig;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.DestinationConfig;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.model.event.impl.ObjectPayload;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.studio.common.util.Path;

import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

/**
 * Integration tests for validating AWS SQS Channel, BE system classes are mocked as well as AWS SQS service
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SQSChannelIntegrationTest {

	// Test data for the fields
	final static String queueName = "test-queue";
    final static String channelName = "SQS";
    final static String folderPath = "/Channels";
    final static String destinationName = "testDest";
    final static String eventURI = "/Events/testEvent";
    final static String driverType = "AWS-SQS";
    final static String eventFolderPath = "/Events";
    final static String channelURI = "/Channels/SqsChannel";
    final static String eventPayloadJSON = "{\"msg\":\"Hello World !!\"}";
    
    // Objects to be mocked
    private ChannelManager channelManager;
    private ChannelConfig channelConfig;
	private RuleServiceProvider rsp;
    private DeployedBEProject deployedBEProject;
    private Logger logger;
    private GlobalVariables globalVariables;
    private DestinationConfig destinationConfig;
    private BEProperties beProperties;

    private String queueUrl;
    private Channel sqsChannel;
    private SqsDestination sqsDestination;
    
    private static DockerImageName localStackImage = DockerImageName.parse("localstack/localstack:0.12.5");

    @Container
    private static LocalStackContainer localStackContainer = new LocalStackContainer(localStackImage)
            .withServices(Service.SQS);
    
	@BeforeAll
	void setup() {
		try {
			queueUrl = localStackContainer.getEndpointOverride(Service.SQS).toString() + Path.SEPARATOR + queueName;
			
			final Properties channelProperties = new Properties();
			channelProperties.put(SqsDestination.CONFIG_AWS_REGION, localStackContainer.getRegion());
			channelProperties.put(SqsDestination.CONFIG_AWS_SQS_ACCESS_KEY, localStackContainer.getAccessKey());
			channelProperties.put(SqsDestination.CONFIG_AWS_SQS_SECRET_KEY, localStackContainer.getSecretKey());
			
			rsp = Mockito.mock(RuleServiceProvider.class);
			channelManager = Mockito.mock(ChannelManager.class);
			channelConfig = Mockito.mock(ChannelConfig.class);
			deployedBEProject = Mockito.mock(DeployedBEProject.class);
			logger = Mockito.mock(Logger.class);
			globalVariables = Mockito.mock(GlobalVariables.class);
			destinationConfig = Mockito.mock(DestinationConfig.class);
			beProperties = Mockito.mock(BEProperties.class);
			
			Mockito.when(channelManager.getRuleServiceProvider()).thenReturn(rsp);
			Mockito.when(rsp.getProject()).thenReturn(deployedBEProject);
			Mockito.when(rsp.getProperties()).thenReturn(beProperties);
			Mockito.when(rsp.getLogger(SqsDriver.class)).thenReturn(logger);
			Mockito.when(rsp.getLogger(SqsTextSerializer.class)).thenReturn(logger);
			Mockito.when(rsp.getLogger(CustomChannel.class)).thenReturn(logger);
			Mockito.when(rsp.getGlobalVariables()).thenReturn(globalVariables);
			Mockito.when(deployedBEProject.getGlobalVariables()).thenReturn(globalVariables);
			Mockito.when(channelConfig.getProperties()).thenReturn(channelProperties);
			Mockito.when(globalVariables.substituteVariables(Mockito.anyString())).thenAnswer(new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					return (String)args[0];
				}
			});
			
			List<DestinationConfig> destinationConfigs = new ArrayList<DestinationConfig>();
			destinationConfigs.add(destinationConfig);
			
			Mockito.when(destinationConfig.getEventSerializer()).thenReturn(new SqsTextSerializer());
			Mockito.when(destinationConfig.getName()).thenReturn(destinationName);
			Mockito.when(destinationConfig.getURI()).thenReturn(channelURI + Path.SEPARATOR + destinationName);
			
			final Properties destinationProperties = new Properties();
			destinationProperties.put(SqsDestination.CONFIG_QUEUE_URL, queueUrl);
			destinationProperties.put(SqsDestination.CONFIG_POLL_INTERVAL, 30);
			destinationProperties.put(SqsDestination.CONFIG_THREADS, 1);
			destinationProperties.put(SqsDestination.CONFIG_MAX_MESSAGES, 1);
			Mockito.when(destinationConfig.getProperties()).thenReturn(destinationProperties);
			Mockito.when(channelConfig.getDestinations()).thenReturn(destinationConfigs);
			
			SqsDriver sqsDriver = new SqsDriver();
			sqsChannel = sqsDriver.createChannel(channelManager, channelURI, channelConfig);
			sqsChannel.init();
			sqsChannel.connect();
			CustomDestination destination = (CustomDestination) sqsChannel.getDestinations().get(channelURI + Path.SEPARATOR + destinationName);
			sqsDestination = (SqsDestination) destination.getBaseDestination();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@BeforeEach
	void queueExists() {
		assertNotNull(sqsChannel);
		assertNotNull(sqsDestination);
		
		GetQueueUrlResponse queueUrlResponse = null;
		try {
			queueUrlResponse = sqsDestination.getSQSClient().getQueueUrl(GetQueueUrlRequest
					.builder()
					.queueName(queueName)
					.build());
		} catch (QueueDoesNotExistException queueDoesNotExist) {

		}
		assertNotNull(queueUrlResponse);
		assertEquals(queueUrlResponse.queueUrl(), queueUrl);
	}
	
	@Test
	@Order(2)
	public void testRecordsReceived() {
		TestEventProcessor evp = new TestEventProcessor(eventPayloadJSON);
		sqsDestination.setEventProcessor(evp);
		try {
			sqsDestination.bind(evp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(1)
	public void testRecordsSent() {
		SimpleEvent simpleEvent = new TestSimpleEvent(1l, "testEvent");
		simpleEvent.setPayload(new ObjectPayload(eventPayloadJSON));
		EventWithId eventWithId;
		try {
			eventWithId = TestExtendedEventImpl.createInstance(simpleEvent);
			sqsDestination.send(eventWithId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@AfterAll
	void destory() {
		try {
			if (sqsDestination != null) sqsDestination.close();
			if (sqsChannel != null) sqsChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
