package com.sample.app.services;

import java.util.DoubleSummaryStatistics;
import com.sample.app.domain.Transaction;

public interface TransactionService {
	
	public Transaction saveOrUpdate(Transaction d);
	
	public DoubleSummaryStatistics getLastMinuteTransactions();
	
}
