package guru.springframework.creditcard.configuration;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import guru.springframework.creditcard.listeners.PostLoadListener;
import guru.springframework.creditcard.listeners.PreInsertListener;
import guru.springframework.creditcard.listeners.PreUpdateListener;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Configuration
@RequiredArgsConstructor
public class EventListenerConfiguration implements BeanPostProcessor {
	
	private final PostLoadListener postLoadEventListener;
	
	private final PreInsertListener preInsertEventListener;
	
	private final PreUpdateListener preUpdateEventListener;
	
	
	@Override
	/**
	 * kroz ovu metodu (BeanPostProcessor) prolaze svi bean-ovi nakon sto se inicijalizuje Spring
	 * ali pre nego sto dodje do pravog rada aplikacije u ovom delu mozemo da ulovimo bean koji nam treba
	 * i naknadno ga inicijalizujemo sa listenerima, jer Hibernate u vreme kad hocemo da injectujemo listenere
	 * nema izgradjeno pola bean-ova i baca eror, ovako sacekamo da se hibernate skroz inicijalizuje 
	 * i onda ubacimo listenere
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		if(bean instanceof LocalContainerEntityManagerFactoryBean) {
			
			val containerManager = (LocalContainerEntityManagerFactoryBean) bean;
			
			val sessionFactory = (SessionFactoryImpl) containerManager.getNativeEntityManagerFactory();
			
			val eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
			
			eventListenerRegistry.appendListeners(EventType.POST_LOAD, postLoadEventListener);
			
			eventListenerRegistry.appendListeners(EventType.PRE_INSERT, preInsertEventListener);

			eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, preUpdateEventListener);
		}
		
		return bean;
		
	}
}
