package guru.springframework.creditcard.listeners;

import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.stereotype.Component;

import guru.springframework.creditcard.interceptors.EncryptionInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreUpdateListener implements PreUpdateEventListener {

	private static final long serialVersionUID = 1L;
	
	private final EncryptionInterceptor encryptionInterceptor;

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {

		log.info("Pre update event###########");

		return encryptionInterceptor.onSave(event.getEntity(), event.getId(), event.getState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());

	}

}
