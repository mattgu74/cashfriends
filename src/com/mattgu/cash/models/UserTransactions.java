package com.mattgu.cash.models;

import java.util.ArrayList;

public class UserTransactions {
	public ArrayList<Transaction> transactions_out;
	public ArrayList<Transaction> transactions_in;
	
	public UserTransactions() {}
	
	public UserTransactions(ArrayList<Transaction> transactions_out,
			ArrayList<Transaction> transactions_in) {
		super();
		this.transactions_out = transactions_out;
		this.transactions_in = transactions_in;
	}

	@Override
	public String toString() {
		return "UserTransactions [transactions_out=" + transactions_out
				+ ", transactions_in=" + transactions_in + "]";
	}
}
