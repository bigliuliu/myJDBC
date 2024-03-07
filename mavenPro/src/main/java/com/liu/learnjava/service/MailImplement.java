package com.liu.learnjava.service;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(OnSmtpEnvCondition.class)
public class MailImplement  implements Mail{
	@Override
	public void sendMail(String address, String subject, String body){
		System.out.println("Send mail to " + address + " using SMTP:");
		System.out.println("Subject: " + subject);
		System.out.println("Body: " + body);
	}
}
