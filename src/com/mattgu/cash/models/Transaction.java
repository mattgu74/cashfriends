package com.mattgu.cash.models;

public class Transaction {
	public String id;
	public String src;
	public String dst;
	public int amount;
	
	public Transaction() {}

	public Transaction(String id, String src, String dst, int amount) {
		super();
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", src=" + src + ", dst=" + dst
				+ ", amount=" + amount + "]";
	}
}
