package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMq {

	@Test
	public void testQueueProducer() throws Exception {
		// 1.创建一个连接工厂对象ConnectionFactory对象,需要指定mq服务的ip以及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.13.68:61616");

		// 2.使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();

		// 3.开启连接.调用Connection对象的start方法
		connection.start();
		// 4使用Connection对象创建一个Session对象

		// 第一个参数是否开启事务,一般不适用事务.保证数据的最终一致
		// 如果一个参数是true,第二个参数自动忽略,如果不开启事务,第二个参数,消息应答模式,一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用Session对象创建一个Destination对象,两种形式queue,topic

		// 消息队列的名称
		Queue queue = session.createQueue("test-queue");
		// 6.使用Session对象创建一个producer对象
		MessageProducer producer = session.createProducer(queue);

		// 7.创建一个TextMessage

		TextMessage textMessage = session.createTextMessage("hello activemq1111");
		// 8.发送消息
		producer.send(textMessage);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	@Test
	public void testQueueConsumer() throws Exception {
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.13.68:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 使用Session创建一个Destination 和发送端一致
		Queue queue = session.createQueue("test-queue");
		// 使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 向Consumer对象中设置一个MessageListener对象,用来接收消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 去消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage=(TextMessage) message;
					try {
						String text=textMessage.getText();
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				// d打印内容

			}
		});
		
		//系统等待接收消息
		/*while (true) {
			Thread.sleep(1000);
			
		}*/
		System.in.read();
		
		
		//关闭资源
		
		consumer.close();
		session.close();
		connection.close();

	}

	
	
	@Test
	public void testTopicProducer() throws Exception {
		// 1.创建一个连接工厂对象ConnectionFactory对象,需要指定mq服务的ip以及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.13.68:61616");

		// 2.使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();

		// 3.开启连接.调用Connection对象的start方法
		connection.start();
		// 4使用Connection对象创建一个Session对象

		// 第一个参数是否开启事务,一般不适用事务.保证数据的最终一致
		// 如果一个参数是true,第二个参数自动忽略,如果不开启事务,第二个参数,消息应答模式,一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用Session对象创建一个Destination对象,两种形式queue,topic

		
		//创建一个topic名称
		  Topic topic = session.createTopic("test-topic");
		// 6.使用Session对象创建一个producer对象
		MessageProducer producer = session.createProducer(topic);

		// 7.创建一个TextMessage

		TextMessage textMessage = session.createTextMessage("hello activemq topic1的点点滴滴多");
		// 8.发送消息
		producer.send(textMessage);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	
	
	@Test
	public void testTopicConsumer() throws Exception {
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.13.68:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 使用Session创建一个Destination 和发送端一致
		 Topic topic = session.createTopic("test-topic");
		// 使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 向Consumer对象中设置一个MessageListener对象,用来接收消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 去消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage=(TextMessage) message;
					try {
						String text=textMessage.getText();
						System.out.println("消费者3:"+text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				// d打印内容

			}
		});
		
		//系统等待接收消息
		/*while (true) {
			Thread.sleep(1000);
			
		}*/
		System.in.read();
		
		
		//关闭资源
		
		consumer.close();
		session.close();
		connection.close();

	}

	
}
