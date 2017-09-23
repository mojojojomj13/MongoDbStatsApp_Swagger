package com.sample.app.controller;

import java.util.Date;
import java.util.DoubleSummaryStatistics;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.app.domain.Transaction;
import com.sample.app.services.TransactionService;

import io.swagger.annotations.Api;

@RestController
@Api(value="transctions API", description="Transactions API for Interviews")
public class TransactionsController {

	@Autowired
	private TransactionService service  ;
	
	@RequestMapping(
			path = "/transactions",
			method = RequestMethod.POST, 
			consumes = "application/json", 
			produces = "application/json"
	)
	public ResponseEntity<?> addTransaction(@Valid @RequestBody(required = true) Transaction d) {
		return (new Date().getTime() - d.getTimestamp() > (60 * 1000)
				? new ResponseEntity<Transaction>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<Transaction>(service.saveOrUpdate(d), HttpStatus.CREATED));
				
	}
	
	@RequestMapping(
			path = "/statistics",
			method = RequestMethod.GET, 
			produces = "application/json"
	)
	public ResponseEntity<?> getDataSet(){
		return new ResponseEntity<DoubleSummaryStatistics>(service.getLastMinuteTransactions(),HttpStatus.OK);
	}
}
