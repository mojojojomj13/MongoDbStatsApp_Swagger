package com.sample.app.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sample.app.config.MongoConfig;
import com.sample.app.domain.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = { MongoDataAutoConfiguration.class, MongoAutoConfiguration.class })
@Import(MongoConfig.class)
public class TransactionMongoRepositoryTest {

	@Autowired
	private TransactionMongoRepository repository;
	private final Date now  = new Date();

	@Before
	public  void setUp() {
		Transaction t1 = new Transaction();
		t1.setTimestamp(now.getTime() - 5 * 1000);
		Transaction t2 = new Transaction();
		t2.setTimestamp(now.getTime() - 10 * 1000);
		Transaction t3 = new Transaction();
		t3.setTimestamp(now.getTime() - 15 * 1000);
		repository.save(t1);
		repository.save(t2);
		repository.save(t3);
	}

	
	@Test
	public void testShouldSaveTransaction() {
		Transaction t = new Transaction();
		t.setTimestamp(new Date().getTime() - 5 * 1000);
		t.setAmount(15.4523);
		repository.save(t);
		Transaction t1 = repository.findOne(t.get_id());
		assertNotNull("The transaction Object created should be found", t1);
	}
	 

	@Test
	public void testShouldGetListOfTransactions() {
		List<Transaction> t = repository.getLastMinuteTransactions(now.getTime() - 60 * 1000 * 1);
		assertNotNull("The Transaction List should be returned", t);
		assertTrue("The transaction List should not be empty", t.size() > 0);
		assertTrue("The Transaction List size should be 3", t.size() == 3);
	}
	
	@Test
	public void testShouldNotGetListOfTransactions() {
		List<Transaction> t = repository.getLastMinuteTransactions(now.getTime());
		assertNotNull("The Transaction List should be returned", t);
		assertTrue("The transaction List should not be empty", t.size() == 0);
	}
	
	
	@After
	public void tesrDown() {
		repository.deleteAll();
	}

}
