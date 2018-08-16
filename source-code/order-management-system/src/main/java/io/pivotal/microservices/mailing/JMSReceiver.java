package io.pivotal.microservices.mailing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import io.pivotal.microservices.mailing.model.Mail;

/**
 * JMS Receiver for Active MQ
 * @author msreekak
 *
 */
@Component
public class JMSReceiver {

	private int count = 1;
	
	@Autowired
    private EmailSender emailSender;

	@JmsListener(destination = "OrderTransactionQueue", containerFactory = "myFactory")
	public void receiveMessage(Mail mail) {
		System.out.println("<" + count + "> Received <" + mail + ">");
		count++;
		emailSender.sendMail(mail.getTo(), mail.getSubject(), mail.getEmailBody());
		System.out.println("EMail Sent Successfully");
		// throw new RuntimeException();
		// transactionRepository.save(transaction);
	}
	
}
