package com.devfest.android.binding;

import android.net.Uri;
import android.provider.BaseColumns;

public class Profile{
	
	private int id=-1;
	private String firstName="";
	private String lastName="";
	private String email="";
	private String address="";
	
	public Profile(){
		
	}
	
	public Profile(int id,String firstName,String lastName,String email,String address){
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.address=address;
		this.id=id;
	}
	
	public Profile(Profile profile){
		this.id=profile.id;
		this.firstName=profile.firstName;
		this.lastName=profile.lastName;
		this.email=profile.email;
		this.address=profile.address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firtName) {
		this.firstName = firtName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public static class Profiles implements BaseColumns{
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ ProfileContentProvider.AUTHORITY + "/profiles");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.profiles";
		
		public static final String PROFILE_ID=_ID;

		public static final String FIRST_NAME = "firstname";

		public static final String LAST_NAME ="lastname";
		
		public static final String EMAIL="email";
		
		public static final String ADDRESS="address";
	}
	
}
