package com.carcomehome.utility;

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
public class MailConstructor {

	@Autowired
	private Environment env;

	@Autowired
	private TemplateEngine templateEngine;
	
	 
    @Autowired
    private ApplicationContext applicationContext;

 
    @Autowired
    private ConversionService mvcConversionService;

	public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user,
			String password) {

		String url = contextPath + "/newUser?token=" + token;
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"
				+ password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("ComeHomeCar Welcomes Joining User");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
	

	public MimeMessagePreparator constructOrderConfirmationEmail(Order order, User user, User carOwner, String appUrl,
			String appUrlCancel, Date pickUpDate, Date returnBackDate, String orderConfirmationTemplate,
			Locale locale) {
		Context context = new Context();
		
		final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, mvcConversionService);
        context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);      
		
		context.setVariable("order", order);
		context.setVariable("user", user);
		String ownerName = carOwner.getFirstName();
		context.setVariable("appUrl", appUrl);
		context.setVariable("appUrlCancel", appUrlCancel);
		context.setVariable("ownerName", ownerName);
		context.setVariable("pickUpDate", pickUpDate);
		context.setVariable("returnBackDate", returnBackDate);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process(orderConfirmationTemplate, context);

		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(carOwner.getEmail());
			//	email.setBcc(user.getEmail());
				email.setSubject("Order Confirmation Needed - " + order.getId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("test777.test77@gmail.com"));
			}
		};

		return messagePreparator;
	}
}
