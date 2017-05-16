package cn.com.fd.active.topic;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * Description:发布订阅模型生产者
 * @author fengda
 * @date 2016年10月14日 上午11:38:07
 */
public class Producer {

	public static void main(String[] args) throws JMSException {  
        // 连接到ActiveMQ服务器  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.121:61616");  
        Connection connection = factory.createConnection();  
        connection.start(); 
        
        /**
         * 不带事务的session
 				不带事务的session的签收方式，取决于session的配置。
		   Activemq支持一下三种模式：
  				Session.AUTO_ACKNOWLEDGE  消息自动签收
  				Session.CLIENT_ACKNOWLEDGE  客户端调用acknowledge方法手动签收
  				Session.DUPS_OK_ACKNOWLEDGE 不必必须签收，消息可能会重复发送。
  					在第二次重新传递消息的时候，消息头的JmsDelivered会被置为true标示当前消息已经传送过一次，客户端需要进行消息的重复处理控制。  
         */
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);  
        
        // 创建主题  
        Topic topic = session.createTopic("slimsmart.topic.test");  
        MessageProducer producer = session.createProducer(topic);  
        // NON_PERSISTENT 非持久化 PERSISTENT 持久化,发送消息时用使用持久模式  
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);  
        TextMessage message = session.createTextMessage();  
        message.setText("topic 消息。");  
        message.setStringProperty("property", "消息Property"); 
        // 发布主题消息  
        producer.send(message);  
        System.out.println("Sent message: " + message.getText());  
        session.commit();  
        session.close();  
        connection.close();  
    }  
}
