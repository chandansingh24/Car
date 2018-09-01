package com.carcomehome.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;

import com.carcomehome.domain.Order;
import com.carcomehome.domain.User;

@Component
public class OrderConfirmUtils {	
	
	@Autowired
	private Environment env;

	@Autowired
	private TemplateEngine templateEngine;
	
	 
    @Autowired
    private ApplicationContext applicationContext;

 
    @Autowired
    private ConversionService mvcConversionService;
	
	
	public Date convertDateTimeToDate() {		
		Date now = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
		ldt = ldt.minusDays(1);		
		Date yestDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());			
				
		System.out.println(yestDate);
		
		return yestDate;		
	}
		
	
	public SimpleMailMessage carOwnerConfirmationEmail(User user, User carOwner, String subject, String bodyMessage, Locale locale) {

		String message = "\nHello -" + bodyMessage;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setCc(carOwner.getEmail());
		email.setSubject(subject);
		email.setText(message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
	
	
	public MimeMessagePreparator sendCustomerConfirmationEmail(Order order, User user, User carOwner,
							Date pickUpDate, Date returnBackDate, String sendCustomerConfirmationTemplate,
			Locale locale) {
		Context context = new Context();
		
		final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, mvcConversionService);
        context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);      
		
		context.setVariable("order", order);
		context.setVariable("user", user);
		String ownerName = carOwner.getFirstName();		
		context.setVariable("ownerName", ownerName);
		String contactPhoneNo = carOwner.getPhone();
		context.setVariable("contactPhoneNo", contactPhoneNo);
		context.setVariable("pickUpDate", pickUpDate);
		context.setVariable("returnBackDate", returnBackDate);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process(sendCustomerConfirmationTemplate, context);

		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());			
				email.setSubject("ComeHomeCar Order Detail - " + order.getId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("test777.test77@gmail.com"));
			}
		};

		return messagePreparator;
	}

	
	
}
