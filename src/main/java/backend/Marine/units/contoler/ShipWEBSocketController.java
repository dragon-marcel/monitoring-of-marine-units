package backend.Marine.units.contoler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ShipWEBSocketController {
	@Autowired
	SimpMessagingTemplate template;

	@MessageMapping("/sendMessage")
	@SendTo("/topic/message")
	public String getMessage() {
		System.out.println("TAK");
		return "WIADOMOSC";
	}

	@SendTo("/topic/ships")
	public String geShips() {
		System.out.println("TAK");
		return "WIADOMOSC";
	}

}
