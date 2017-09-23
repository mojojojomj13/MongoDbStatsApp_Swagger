package com.sample.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sample.app.domain.Transaction;
import com.sample.app.repository.TransactionMongoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionServiceTest {

	private final Date NOW = new Date();
	private final ObjectId ID_TO_FIND = new ObjectId();

	@TestConfiguration
	static class TransactionServiceImplTestContextConfiguration {
		@Bean
		public TransactionService employeeService() {
			return new TransactionServiceImpl();
		}
	}

	@Autowired
	private TransactionService transactionService;

	@MockBean
	private TransactionMongoRepository repository;

	private Transaction t = new Transaction();
	List<Transaction> list = new ArrayList<Transaction>();

	@Before
	public void setUp() {
		t.setTimestamp(NOW.getTime() - 10 * 1000);
		t.setAmount(2.662);
		when(repository.save(t)).thenAnswer(new Answer<Transaction>() {
			public Transaction answer(InvocationOnMock invocation) throws Throwable {
				Transaction tx = ((Transaction) invocation.getArguments()[0]);
				if (t.getTimestamp() == NOW.getTime() - 10 * 1000 && t.getAmount() == 2.662)
					tx.set_id(ID_TO_FIND);
				return tx;
			}
		});
		addTransactionToList(1.000, NOW.getTime() - 10 * 1000);
		addTransactionToList(2.000, NOW.getTime() - 15 * 1000);
		addTransactionToList(3.000, NOW.getTime() - 20 * 1000);
		addTransactionToList(6.000, NOW.getTime() - 25 * 1000);
		when(repository.getLastMinuteTransactions(anyLong())).thenReturn(list);
	}

	private void addTransactionToList(double amount, long timestamp) {
		Transaction tx = new Transaction();
		tx.setAmount(amount);
		tx.setTimestamp(timestamp);
		list.add(tx);
	}

	@Test
	public void testShouldGetTransactionId() {
		Transaction tx = transactionService.saveOrUpdate(t);
		assertNotNull("The Id of the transaction should not be null", tx.get_id());
		assertEquals("The Id returned for Tranasction should be 1001'", tx.get_id(), ID_TO_FIND);
	}

	@Test
	public void testShouldNotGetTransactionId() {
		t.setAmount(5.621);
		Transaction tx = transactionService.saveOrUpdate(t);
		assertNull("The Id of the transaction should not be null", tx.get_id());
	}

	@Test
	public void testShouldGetStats() {
		DoubleSummaryStatistics stats = transactionService.getLastMinuteTransactions();
		DoubleSummaryStatistics statsExpected = list.stream().mapToDouble((x) -> x.getAmount()).summaryStatistics();
		assertThat(stats.getAverage()).isEqualTo(statsExpected.getAverage())
				.withFailMessage("Both the stats average should be equal", new Object[] {});
		assertThat(stats.getSum()).isEqualTo(statsExpected.getSum())
				.withFailMessage("Both the stats sum should be equal", new Object[] {});
		assertThat(stats.getMin()).isEqualTo(statsExpected.getMin())
				.withFailMessage("Both the stats minimum value should be equal", new Object[] {});
		assertThat(stats.getMax()).isEqualTo(statsExpected.getMax())
				.withFailMessage("Both the stats maximum value should be equal", new Object[] {});

	}

}
