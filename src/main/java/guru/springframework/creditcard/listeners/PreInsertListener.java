package guru.springframework.creditcard.listeners;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.stereotype.Component;

import guru.springframework.creditcard.interceptors.EncryptionInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class PreInsertListener implements PreInsertEventListener{

	private static final long serialVersionUID = 1L;
	
	private final EncryptionInterceptor encryptionInterceptor;


	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		
		log.info("Pre insert event###########");
		
		return encryptionInterceptor.onSave(event.getEntity(), event.getId(), event.getState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());

	}

}
