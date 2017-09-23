package com.sample.app.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sample.app.domain.Transaction;
import com.sample.app.repository.TransactionMongoRepository;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionMongoRepository dataMongoRepository;
	
	public Transaction saveOrUpdate(Transaction d) {
		dataMongoRepository.save(d);
		return d;
	}

	public DoubleSummaryStatistics getLastMinuteTransactions() {
		Long now  = new Date().getTime();
		Long unixFrom = now - (60 * 1000 * 1);
		DoubleSummaryStatistics res = new DoubleSummaryStatistics();
		List<Transaction> list =  new ArrayList<Transaction>();
		list  = dataMongoRepository.getLastMinuteTransactions(unixFrom);
		res = list.stream().mapToDouble((x)-> x.getAmount()).summaryStatistics();
		return res;
	}
	
}
