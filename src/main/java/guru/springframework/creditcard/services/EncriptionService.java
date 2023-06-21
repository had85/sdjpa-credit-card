package guru.springframework.creditcard.services;

public interface EncriptionService {

	String encrypt(String text);
	
	String decrypt(String text);
}
