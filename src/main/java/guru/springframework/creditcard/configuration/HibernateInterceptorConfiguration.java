package guru.springframework.creditcard.configuration;

import java.util.Map;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import guru.springframework.creditcard.interceptors.EncryptionInterceptor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class HibernateInterceptorConfiguration implements HibernatePropertiesCustomizer {
	
	private final EncryptionInterceptor encryptionInterceptor;

	@Override
	public void customize(Map<String, Object> hibernateProperties) {

		//hibernateProperties.put("hibernate.session_factory.interceptor", encryptionInterceptor);
	}

}
