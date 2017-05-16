package cn.com.fd.active;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 生产者
 * Description:
 * @author fengda
 * @date 2016年10月9日 下午4:27:37
 */
public class Sender {

	//发送消息数量
    private static final int SENDNUM = 3;

    public static void main(String[] args) {
    //public void produce(){
        //连接工厂
        ConnectionFactory connectionFactory;
        //JSM的客户端连接
        Connection connection = null;
        //一个发送或者接收的线程
        Session session;
        //消息的目的地
        Destination destination;
        //消息发送者
        MessageProducer messageProducer;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session,Boolean.TRUE表示开启事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //创建消息队列(消息存放的队列名称要和获取的消息队列名称相同)
            destination = session.createQueue("FirstQueue");
            //创建消息生产者
            messageProducer = session.createProducer(destination);
            
            // NON_PERSISTENT 非持久化 PERSISTENT 持久化,发送消息时用使用持久模式  
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            sendMessage(session, messageProducer);
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally{
            if(null != connection){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 发送消息
     * @param session
     * @param messageProducer
     */
    public static void sendMessage(Session session,MessageProducer messageProducer){
        for (int i = 0; i < SENDNUM; i++) {
            try {
                TextMessage textMessage = session.createTextMessage("ActiveMQ 发送的消息" + i);
                System.out.println("发送消息：ActiveMQ 发送的消息" + i);
                messageProducer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
       }
        System.out.println("生产完成");
    }
}
