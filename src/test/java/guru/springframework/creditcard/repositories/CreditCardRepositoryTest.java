package guru.springframework.creditcard.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import guru.springframework.creditcard.domain.CreditCard;
import guru.springframework.creditcard.services.EncriptionService;
import jakarta.transaction.Transactional;
import lombok.val;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class CreditCardRepositoryTest {
	
	final String CREDIT_CARD_NUMBER = "123456789012345648";
	
	@Autowired
	CreditCardRepository creditCardRepository;


	@Autowired
	EncriptionService encriptionService;
	
	@Autowired
	JdbcTemplate jdbcTemplate; //ubacen iz razloga sto moramo da dohvatimo nesto direktno iz baze
	                           //a da ne aktiviramo hibernate interceptor

    @Test
    void testSaveCreditcard() {
    	
    	val creditCard = CreditCard.builder()
    						.creditCardNumber(CREDIT_CARD_NUMBER)
    						.cvv("222")
    						.expirationDate("12/2028")
    						.build();
    	
    	val encryptedCreditCardNumber = encriptionService.encrypt(creditCard.getCreditCardNumber());
    	    			
    	creditCardRepository.saveAndFlush(creditCard);
    	
    	val dbRow = jdbcTemplate.queryForMap("select * from credit_card where id = ?", creditCard.getId());
    	
    	assertThat(creditCard.getId()).isNotNull();
    	
    	assertThat(dbRow.get("credit_card_number")).isEqualTo(encryptedCreditCardNumber);
    }


}
