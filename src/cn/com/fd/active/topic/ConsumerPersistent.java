package cn.com.fd.active.topic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Description:发布订阅模型消费者
 * @author fengda
 * @date 2016年10月14日 上午11:39:53
 */
public class ConsumerPersistent {

	public static void main(String[] args) throws JMSException {  
        String clientId = "client_id";  
          
        // 连接到ActiveMQ服务器  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.121:61616");  
        Connection connection = factory.createConnection();  
        //客户端ID,持久订阅需要设置  
        connection.setClientID(clientId);  
        connection.start();  
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);  
        // 创建主题  
        Topic topic = session.createTopic("slimsmart.topic.test");  
        // 创建持久订阅,指定客户端ID。  
        MessageConsumer consumer = session.createDurableSubscriber(topic,clientId);  
        consumer.setMessageListener(new MessageListener() {  
            // 订阅接收方法  
            public void onMessage(Message message) {  
                TextMessage tm = (TextMessage) message;  
                try {  
                    System.out.println("Received message: " + tm.getText()+":"+tm.getStringProperty("property"));  
                } catch (JMSException e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
  
    }  
}
