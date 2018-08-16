package io.pivotal.microservices.mailing.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/**
 * Model class to hold mail variables
 * @author msreekak
 *
 */
public class Mail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Long nextId = 1L;

	@Id
	private Long id;

	private String to;
	private String subject;
	private String emailBody;
	
	public Mail(){
		id = getNextId();
	}

	public Mail(final String to, final String subject, final String emailBody) {
		id = getNextId();
		this.to = to;
		this.subject = subject;
		this.emailBody = emailBody;
	}
	
	/**
	 * This is a very simple, and non-scalable solution to generating unique
	 * ids. Not recommended for a real application. Consider using the
	 * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
	 * 
	 * @return The next available id.
	 */
	protected static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	@Override
	public String toString() {
		return "Mail [id=" + id + ", to=" + to + ", subject=" + subject + ", emailBody=" + emailBody + "]";
	}

}
