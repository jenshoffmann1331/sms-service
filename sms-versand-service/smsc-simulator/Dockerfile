FROM amazoncorretto:17

RUN yum install -y wget && yum clean all

WORKDIR /app

# Download JSMPP library
RUN wget https://repo1.maven.org/maven2/org/jsmpp/jsmpp/3.0.0/jsmpp-3.0.0.jar

# Create simple SMSC simulator
RUN echo 'import org.jsmpp.bean.*; \
import org.jsmpp.session.*; \
import org.jsmpp.util.MessageIDGenerator; \
import org.jsmpp.util.MessageId; \
import java.io.IOException; \
\
public class SimpleSmscSimulator { \
    public static void main(String[] args) throws Exception { \
        int port = Integer.parseInt(System.getenv().getOrDefault("SMPP_PORT", "2775")); \
        String systemId = System.getenv().getOrDefault("SYSTEM_ID", "smppclient1"); \
        String password = System.getenv().getOrDefault("PASSWORD", "password"); \
        \
        SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port); \
        System.out.println("SMSC Simulator listening on port " + port); \
        \
        while (true) { \
            SMPPServerSession serverSession = sessionListener.accept(); \
            System.out.println("Accepting connection"); \
            \
            serverSession.setMessageReceiverListener(new MessageReceiverListener() { \
                public void onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession source) throws ProcessRequestException { \
                    MessageId messageId = new RandomMessageIDGenerator().newMessageId(); \
                    System.out.println("SMS received - From: " + submitSm.getSourceAddr() + ", To: " + submitSm.getDestAddress() + ", Message: " + new String(submitSm.getShortMessage())); \
                    try { \
                        source.sendSubmitSmResponse(messageId.getValue(), submitSm.getSequenceNumber()); \
                    } catch (Exception e) { \
                        throw new ProcessRequestException(e.getMessage(), SMPPConstant.STAT_ESME_RSYSERR); \
                    } \
                } \
                \
                public void onAcceptAlertNotification(AlertNotification alertNotification) {} \
                public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException { return null; } \
                public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {} \
                public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession source) throws ProcessRequestException { return null; } \
                public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source) throws ProcessRequestException {} \
                public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source) throws ProcessRequestException {} \
            }); \
            \
            serverSession.setBindRequestReceiver(new BindRequestReceiver() { \
                public BindResponse onAcceptBindRequest(BindRequest bindRequest, SMPPServerSession session) { \
                    System.out.println("Bind request received - SystemId: " + bindRequest.getSystemId()); \
                    return new BindResponse(bindRequest.getSystemId()); \
                } \
            }); \
        } \
    } \
}' > SimpleSmscSimulator.java

RUN javac -cp jsmpp-3.0.0.jar SimpleSmscSimulator.java

CMD ["java", "-cp", ".:jsmpp-3.0.0.jar", "SimpleSmscSimulator"]
