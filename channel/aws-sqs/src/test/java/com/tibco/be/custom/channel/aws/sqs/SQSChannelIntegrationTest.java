package com.tibco.be.custom.channel.aws.sqs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;
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
import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.cep.runtime.model.TypeManager.TypeDescriptor;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.model.event.impl.ObjectPayload;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.cep.studio.common.util.Path;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

//import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
//import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
//import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
//import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
//import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

/**
 * Integration tests for validating AWS SQS Channel, BE system classes are mocked as well as AWS SQS service
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SQSChannelIntegrationTest {

	// Test data for the fields
	final static String queueName = "test-queue";
	final static String accountNo = "000000000000";
    final static String destinationName = "testDest";
    final static String eventURI = "/Events/testEvent";
    final static String channelURI = "/Channels/SqsChannel";
    final static String eventPayloadJSON = "{\"msg\":\"Hello World !!\"}";
    
    // Objects to be mocked
    private RuleServiceProviderManager rspMgrInstance;
    private TypeManager typeManager;
    private TypeDescriptor typeDescriptor;
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
			queueUrl = localStackContainer.getEndpointOverride(Service.SQS).toString().replace("127.0.0.1", "localhost") 
					+ Path.SEPARATOR 
					+ accountNo
					+ Path.SEPARATOR
					+ queueName;
			
			mockObjects();
			
			SqsDriver sqsDriver = new SqsDriver();
			sqsChannel = sqsDriver.createChannel(channelManager, channelURI, channelConfig);
			sqsChannel.init();
			sqsChannel.connect();
			CustomDestination destination = (CustomDestination) sqsChannel.getDestinations().get(channelURI + Path.SEPARATOR + destinationName);
			sqsDestination = (SqsDestination) destination.getBaseDestination();
			
			//create test queue
			String queueUrl = getQueueUrl();
			if (queueUrl == null) {

					CreateQueueResult createQueueResult = sqsDestination.getSQSClient().createQueue(queueName);


//				CreateQueueResponse createQueueResponse = sqsDestination.getSQSClient().createQueue( CreateQueueRequest.builder()
//						.queueName(queueName)
//						.build());

				assertNotNull(createQueueResult , String.format("Queue[%s] created !!", queueName));
				
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@BeforeEach
	void prerequisitesExist() {
		assertNotNull(localStackContainer);
		assertTrue(localStackContainer.isRunning());
		
		assertNotNull(sqsChannel);
		assertNotNull(sqsDestination);
		
		String queueUrl = getQueueUrl();
		assertNotNull(queueUrl);
		assertEquals(queueUrl, this.queueUrl);
	}
	
	@Test
	@Order(2)
	public void testRecordsReceived() {
		TestEventProcessor evp = new TestEventProcessor(eventPayloadJSON, 1);
		sqsDestination.setEventProcessor(evp);
		try {
			sqsDestination.bind(evp);
			sqsDestination.start();
			
			while (!evp.allMessagesReceived()) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(1)
	public void testRecordsSent() {
		try (MockedStatic<RuleServiceProviderManager> rspMgrStatic = Mockito.mockStatic(RuleServiceProviderManager.class)) {
			rspMgrStatic.when(() -> RuleServiceProviderManager.getInstance()).thenReturn(rspMgrInstance);
			Mockito.when(rspMgrInstance.getDefaultProvider()).thenReturn(rsp);

			SimpleEvent simpleEvent = new TestSimpleEvent(1l, "testEvent", channelURI + Path.SEPARATOR + destinationName, eventURI);
			simpleEvent.setPayload(new ObjectPayload(eventPayloadJSON));
			EventWithId eventWithId;
			try {
				eventWithId = TestExtendedEventImpl.createInstance(simpleEvent);
				sqsDestination.send(eventWithId, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
	private void mockObjects() {
		final Properties channelProperties = new Properties();
		channelProperties.put(SqsDestination.CONFIG_AWS_REGION, localStackContainer.getRegion());
		channelProperties.put(SqsDestination.CONFIG_AWS_SQS_CREDENTIALS_ACCESS_KEY, localStackContainer.getAccessKey());
		channelProperties.put(SqsDestination.CONFIG_AWS_SQS_CREDENTIALS_SECRET_KEY, localStackContainer.getSecretKey());
		
		rspMgrInstance = Mockito.mock(RuleServiceProviderManager.class);
		rsp = Mockito.mock(RuleServiceProvider.class);
		typeManager = Mockito.mock(TypeManager.class);
		typeDescriptor = Mockito.mock(TypeDescriptor.class);
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
		Mockito.when(rsp.getTypeManager()).thenReturn(typeManager);
		
		Mockito.when(typeManager.getTypeDescriptor(TestSimpleEvent.class)).thenReturn(typeDescriptor);
		Mockito.when(typeDescriptor.getTypeId()).thenReturn(1011);
		
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
		destinationProperties.put(SqsDestination.CONFIG_POLL_INTERVAL, 20);
		destinationProperties.put(SqsDestination.CONFIG_THREADS, 1);
		destinationProperties.put(SqsDestination.CONFIG_MAX_MESSAGES, 1);
		Mockito.when(destinationConfig.getProperties()).thenReturn(destinationProperties);
		Mockito.when(channelConfig.getDestinations()).thenReturn(destinationConfigs);
	}

	private String getQueueUrl() {
		String queueUrl = null;
		//GetQueueUrlResponse queueUrlResponse = null;
		try {

			AmazonSQS sqsClient = sqsDestination.getSQSClient();

			GetQueueUrlRequest request = new GetQueueUrlRequest()
					.withQueueName(getQueueUrl());

			return sqsDestination.getSQSClient().getQueueUrl(queueName).getQueueUrl();

		} catch (Exception e) {}

		return queueUrl;
	}
}
