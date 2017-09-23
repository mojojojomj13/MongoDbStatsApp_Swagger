package com.sample.app.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sample.app.domain.Transaction;

public interface TransactionMongoRepository extends MongoRepository<Transaction, ObjectId> {
	@Query(value = "{ 'timestamp' : {$gte : ?0 }}")
	public List<Transaction> getLastMinuteTransactions(Long timestamp);
}
