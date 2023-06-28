package guru.springframework.creditcard.listeners;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.springframework.stereotype.Component;

import guru.springframework.creditcard.interceptors.EncryptionInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor //koristiti interceptor posto ne radi lepo postload event
public class PostLoadListener implements PostLoadEventListener {

	private static final long serialVersionUID = 1L;
	
	private final EncryptionInterceptor encryptionInterceptor;

	@Override
	public void onPostLoad(PostLoadEvent event) {
		log.info("Post load event###########");

		encryptionInterceptor.onLoad(event.getEntity());
		
	}

}
