package com.tibco.cep.driver.sb.internal;

import java.util.Iterator;
import java.util.LinkedList;

import com.streambase.sb.StreamBaseException;
import com.streambase.sb.StreamProperties;
import com.streambase.sb.Tuple;
import com.streambase.sb.client.DequeueResult;
import com.streambase.sb.client.StreamBaseClient;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.channel.Channel;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.impl.DefaultSerializationContext;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.model.event.impl.AbstractEventContext;
import com.tibco.cep.runtime.session.RuleSession;

public class SBDequeueClient implements Runnable, ISBClient {

    class SBMessageContext extends AbstractEventContext {

        private StreamBaseClient client;
		private SBDestination dest;
		private Tuple msg;

		SBMessageContext(StreamBaseClient client, SBDestination dest, Tuple message) {
        	this.client = client;
        	this.dest = dest;
        	this.msg = message;
        }
		
        public void acknowledge() {
         //   logger.log(Level.INFO, "Acknowledging sb msg [%s]", this.msg);
//            try {
//            } catch (Exception e) {
//                logger.log(Level.WARN, e, "Can't acknowledge sb msg [%s]", this.msg);
//            }
        }

        public boolean reply(SimpleEvent replyEvent) {
            logger.log(Level.INFO, "Replying to sb msg [%s]", this.msg);
//            try {
//                String subject = msg.getReplySubject();
//                if (subject != null) {
//                    Map override = new HashMap();
//                    dest.send(replyEvent, override);
//                    return true;
//                }
//            }
//            catch (Exception e) {
//                channel.getLogger().log(Level.WARN, e, "Can't reply sb msg [%s]", this.msg);
//            }
            return false;
        }

        public boolean canReply() {
            logger.log(Level.INFO, "Acknowledging sb msg [%s]", this.msg);
            return false;//(null != this.msg.getReplySubject());
        }

        /**
         *
         * @return
         */
        public Channel.Destination getDestination() {
            return dest;
        }

        /**
         *
         * @return
         */
        public Object getMessage() {
        	return msg;
        }

        @Override
		public String replyImmediate(SimpleEvent replyEvent) {
			reply(replyEvent);
			return null;
		}
    }

	private Logger				logger;
	private volatile boolean	shutdown;
	private StreamBaseClient	client;

	private String streamName;
	private SBDestination destination;

	private Status status;

	private LinkedList<Object> payloadQueue;

	private RuleSession boundSession;
	private boolean subscribed = false;
	private Thread dequeueThread;

	public SBDequeueClient(
			RuleSession session, 
			StreamBaseClient client,
			Logger logger,
			String streamName,
			SBDestination destination) {
		this.boundSession = session;
		this.client = client;
		this.logger = logger;
		this.streamName = streamName;
		this.destination = destination;
		this.status = Status.init;
	}

	public void shutdown() {
		shutdown = true;
	}

	@Override
	public final void start(int startMode) throws Exception {
	    Status oldstatus = status;
	    if (null == payloadQueue) {
	        payloadQueue = new LinkedList<Object>();
	    }
	    while (client != null && !subscribed) {
	    	try {
	    		if (destination.getPredicate() != null && destination.getPredicate().trim().length() > 0) {
	    			client.subscribe(streamName, null, destination.getPredicate());
	    		} else {
	    			client.subscribe(streamName);
	    		}
	    	} catch (StreamBaseException e) {
	    		if (e.getMessage() != null && e.getMessage().startsWith("NonExistentContainerException:")) {
	    			// don't like this check, but there's doesn't seem to be a way to check for the container not-yet-available exception
	    			continue;
	    		}
	    		logger.log(Level.ERROR, e, "Unable to subscribe to Stream '%s'.", streamName);
	    		throw e; // not sure whether to continue here or kill the engine, we could try to reconnect
	    	}
	    	logger.log(Level.INFO, "'%s' successfully subscribed to Stream '%s'", destination.getURI(), streamName);
	    	subscribed = true;
	    	break;
	    }
		if (subscribed && ChannelManager.ACTIVE_MODE == startMode) {
			startDequeueThread();
		}
        if (startMode == ChannelManager.SUSPEND_MODE) {
            if (status.equals(Status.running) == false) {
            	status = Status.suspended;
            }
        } else if (startMode == ChannelManager.ACTIVE_MODE) {
            status = Status.running;
            if (oldstatus.equals(Status.suspended) == true) {
            	// Consume any messages queued during suspend
                resume();
            }
        }
	}
	
	private void startDequeueThread() {
		dequeueThread = new Thread(this, "StreamBaseDequeuer-Daemon-"+streamName);
		dequeueThread.setDaemon(true);
		dequeueThread.start();
	}

	@Override
	public void run() {
		logger.log(Level.INFO, "'%s' started dequeuing from Stream '%s'", destination.getURI(), streamName);
		while (!shutdown) {
			if (!subscribed) {
				attemptResubscribe();
			}
			if (!subscribed) {
				continue;
			}
			DequeueResult dr = null;
			try {
				//client.dequeue() blocks until either data has arrived for a single stream,
				//or the client connection has been broken (returning null)
				while ((dr = client.dequeue()) != null) {
	    			processDequeueResult(dr);
				}
			} catch (StreamBaseException e) {
				if (!shutdown) {
					logger.log(Level.ERROR, "StreamBaseException during dequeue: "
							+ e.getMessage());
				}
			}
		}
		logger.log(Level.INFO, "Client connection ended: dequeuing thread exiting.");
		return;
	}
	
	private void attemptResubscribe() {
		// TODO : add reconnect logic?
	}

	private void processTuple(StreamProperties streamProperties, Tuple tuple) {
		if (destination.isSuspended()) {
			logger.log(Level.ERROR, "Destination '%s' is suspended", destination.getURI());
			return;
		}
		// Note that this runs synchronous to the dequeuing process, so this method should return promptly.
		logger.log(Level.TRACE, "Tuple dequeued: " + tuple.toString()
				+ " from streamProperties: "
				+ streamProperties.getQualifiedName());
		try {
			SimpleEvent event = (SimpleEvent) destination.getEventSerializer().deserialize(tuple, new DefaultSerializationContext(boundSession, destination));
			boundSession.assertObject(event, true);
		} catch (Exception e) {
			logger.log(Level.ERROR, "Exception during process Tuple: "
					+ e.getMessage());
		}
	}

	@Override
	public void suspend() throws Exception {
		status = Status.suspended;
	}

	@Override
	final public void resume() throws Exception {
        if (status.equals(Status.stopped) || status.equals(Status.suspended)) {
        	startDequeueThread();
        	status = Status.running;
        } else {
//            if (initialized) {
                status = Status.running;
//            }
        }
		if (Status.running == status) {
			
    		Object payload = null;
    		while((payload = payloadQueue.poll()) != null) {
    			DequeueResult dr = (DequeueResult) payload;
    			processDequeueResult(dr);
    		}
		}
	}
	
	private void processDequeueResult(DequeueResult dr) {
		if (dr.getStatus() != DequeueResult.GOOD) {
			logger.log(Level.WARN, "Dequeue result has an invalid status, result not processed");
			return;
		}
		if (status.equals(Status.suspended)) {
			payloadQueue.offer(dr);
		} else if (status.equals(Status.running)) {
			Iterator<Tuple> tuples = dr.iterator();

			while (tuples.hasNext()) {
				try {
					Tuple tuple = tuples.next();
					logger.log(Level.TRACE, "Dequeued Tuple from StreamBaseClient: '%s'", tuple);
					destination.onMessage(boundSession, new SBMessageContext(client, destination, tuple));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.log(Level.TRACE, "Dequeue client not ready - status: '%s'. DequeueResult not processed", status);
		}
	}
	
	@Override
	public void stop() throws Exception {
		status = Status.stopped;
		shutdown = true;
		if (dequeueThread != null) {
			dequeueThread.interrupt();
		}
	}
	
	@Override
	public void close() throws Exception {
		stop();
		if (client != null) {
			client.unsubscribe(streamName);
		}
		status = Status.closed;
	}

    public static enum Status {
    	init,running,suspended,stopped,closed
    }
    
}
