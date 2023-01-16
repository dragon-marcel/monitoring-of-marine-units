package backend.Marine.units.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;



@Configuration
public class SwaggerConfig {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

	private final ServletContext servletContext;

	    @Autowired
	    public SwaggerConfig(ServletContext servletContext) {
	        this.servletContext = servletContext;
	    }
	@Bean
	public Docket get() {

		return new Docket(DocumentationType.SWAGGER_2)
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey())).select()
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.apiInfo(getApiInfo());
	}
	

	@SuppressWarnings("unchecked")
	private ApiInfo getApiInfo() {
		return new ApiInfo("Monitoring of marine units"
				,"REST API"
				,"1.0"
				,"https://momu.pl"
				,new Contact("Marcel Dargon", "https://momu.pl", "dragon.marcel@o2.pl")
				,"license"
				,"https://momu.pl"
				,Collections.EMPTY_LIST);
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
}
