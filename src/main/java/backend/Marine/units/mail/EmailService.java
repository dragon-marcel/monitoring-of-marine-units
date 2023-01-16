package backend.Marine.units.mail;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.User;

@Component
public class EmailService {
	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private SpringTemplateEngine templateEngine;

	private final String TEMPLATE = "ship_email_template";

	public void sendSimpleMessage(User user, Ship ship, String subject, String text) {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Context context = new Context();
			context.setVariable("name_ship", ship.getName());
			context.setVariable("name_mmsi", ship.getMmsi());
			context.setVariable("coordinate_x", ship.getCurrentPoint().getXCoordinate());
			context.setVariable("coordinate_y", ship.getCurrentPoint().getYCoordinate());
			context.setVariable("image_ship", ship.getImg());
			context.setVariable("type_ship", ship.getType().getDescription());
			context.setVariable("data_time", ship.getCurrentPoint().getCreateDateTime());
			context.setVariable("destination_ship", ship.getDestination());
			context.setVariable("username", user.getUsername());

			String html = templateEngine.process(TEMPLATE, context);
			helper.setTo(user.getEmail());
			helper.setText(html, true);
			helper.setSubject(subject);
			helper.setFrom("dragon@o2.pl");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		emailSender.send(message);

	}
}
