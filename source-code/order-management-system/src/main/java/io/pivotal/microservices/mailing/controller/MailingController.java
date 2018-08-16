package io.pivotal.microservices.mailing.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.pivotal.microservices.mailing.EmailSender;
import io.pivotal.microservices.mailing.model.Mail;

/**
 * Client controller, used to send mail for mailing-service
 * 
 * @author msreekak
 */
@Controller
public class MailingController {

	@Autowired
	private EmailSender emailSender;

	protected Logger logger = Logger.getLogger(MailingController.class.getName());

	/**
	 * Rest endpoint to send email
	 * @param mail
	 * @return Mail
	 */
	@RequestMapping(value = "/mailing/v1/sendMail", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Mail sendEmail(@RequestBody Mail mail) {
		logger.info("billing-service generateBill() invoked: ");
		emailSender.sendMail(mail.getTo(), mail.getSubject(), mail.getEmailBody());
		System.out.println("EMail Sent Successfully");
		return mail;
	}
}
