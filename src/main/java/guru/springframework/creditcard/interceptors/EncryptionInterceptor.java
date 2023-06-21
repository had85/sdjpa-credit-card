package guru.springframework.creditcard.interceptors;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import guru.springframework.creditcard.services.EncriptionService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EncryptionInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	
	private final EncriptionService encriptionService;

	@Override //kad se flush-uje izmena u bacu
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		log.info("On flushDirty...");
		val annotatedFields = getAnnotationFields(entity);
		
		for(int i = 0; i < annotatedFields.size(); i++) {
			if(annotatedFields.get(i).equalsIgnoreCase(propertyNames[i])) {
				currentState[i] = encriptionService.encrypt(currentState[i].toString());
			}
		}
		
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override //zove se kad pre nego sto iscitamo iz baze
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		
		log.info("On onLoad...");

		val annotatedFields = getAnnotationFields(entity);
		
		for(int i = 0; i < annotatedFields.size(); i++) {
			if(annotatedFields.get(i).equalsIgnoreCase(propertyNames[i])) {
				state[i] = encriptionService.decrypt(state[i].toString());
			}
		}
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override //pre nego sto perzistiramo
	//interceptor sme da modifikuje state niz, koji predstavlja niz kolona koje ce biti perzistirane u bazu
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.info("On onSave...");
		val annotatedFields = getAnnotationFields(entity);
		
		for(int i = 0; i < annotatedFields.size(); i++) {
			if(annotatedFields.get(i).equalsIgnoreCase(propertyNames[i])) {
				state[i] = encriptionService.encrypt(state[i].toString()); //hibernate prepoznaje promenu
				                                                           //i aktivira onFlushDirty 
				                                                           //kad odradimo promenu entiteta
				                                                           //koji je u transakciji
				                                                           //a nismo pozvali eksplicitno save na 
				                                                           //na repository interfejsu
			}
		}
		
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
	private List<String> getAnnotationFields(Object entity){
		return Arrays.stream(entity.getClass().getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(EncryptedString.class))
				.map(Field::getName)
				.collect(Collectors.toList());
	}

}
