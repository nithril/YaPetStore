package io.github.nithril.yapetstore;

import io.github.nithril.yapetstore.repository.PetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nlabrot on 21/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetstoreApplicationTests {

	@Autowired
	private PetRepository petRepository;


	@Test
	public void contextLoads() {
		petRepository.findAll();
	}

}
