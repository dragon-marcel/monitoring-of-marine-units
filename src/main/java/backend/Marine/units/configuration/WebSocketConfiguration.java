package backend.Marine.units.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-message").setAllowedOrigins("http://localhost:3000/",
				"http://localhost:3000/info?t=1673643039016", "http://localhost:3000/login").withSockJS();
		registry.addEndpoint("/ws-ships").setAllowedOrigins("http://localhost:3000/",
				"http://localhost:3000/info?t=1673643039016", "http://localhost:3000/login").withSockJS();
	}

}