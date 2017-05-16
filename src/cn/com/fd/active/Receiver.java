package cn.com.fd.active;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消费者
 * Description:
 * @author fengda
 * @date 2016年10月9日 下午4:28:05
 */
public class Receiver {

	 public static void main(String[] args) {
	//public void Customer(){ 
	        //连接工厂
	        ConnectionFactory connectionFactory;
	        //JSM的客户端连接
	        Connection connection = null;
	        //一个发送或者接收的线程
	        Session session;
	        //消息获取的获取者
	        Destination destination;
	        //消息接收者
	        MessageConsumer messageConsumer;
	        //实例化连接工厂
	        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);

	        //通过连接工厂获取连接
	        try {
	            connection = connectionFactory.createConnection();
	            //启动连接
	            connection.start();
	            //创建session
	            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
	            //创建消息队列
	            destination = session.createQueue("FirstQueue");
	            //创建消息接收者
	            messageConsumer = session.createConsumer(destination);
	            while(true){
	                TextMessage  textMessage = (TextMessage) messageConsumer.receive(100000);
	                if(null != textMessage){
	                    System.out.println(textMessage.getText());
	                }else{
	                    break;
	                }
	            }
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }finally {
	            if(null != connection){
	                try {
	                    connection.close();
	                } catch (JMSException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
}
