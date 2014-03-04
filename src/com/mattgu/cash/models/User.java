package com.mattgu.cash.models;

public class User {
	public int id;
	public String username;
	public String badge_uid;
	public String email;
	public String first_name;
	public String last_name;
	public Integer solde;
	public Integer in;
	public Integer out;
    
    public User() {}
    
	public User(int id, String username, String badge_uid, String email,
			String first_name, String last_name, Integer solde, Integer in, Integer out) {
		super();
		this.id = id;
		this.username = username;
		this.badge_uid = badge_uid;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.solde = solde;
		this.in = in;
		this.out = out;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", badge_uid="
				+ badge_uid + ", email=" + email + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", solde=" + solde + ", in=" + in + ", out=" + out +"]";
	}
}
