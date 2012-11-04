package com.devfest.android.binding;

import com.devfest.android.binding.Profile.Profiles;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;

public class ProfileController {

	private Context mContext;

	public ProfileController(Context context) {
		this.mContext = context;
	}

	public Profile retrieveProfile() {
		ContentResolver cr = mContext.getContentResolver();
		String[] projection = { Profiles.PROFILE_ID, Profiles.FIRST_NAME,
				Profiles.LAST_NAME, Profiles.EMAIL, Profiles.ADDRESS };
		Cursor cursor = cr.query(Profiles.CONTENT_URI, projection, null, null,
				null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			String firstName = cursor.getString(1);
			String lastName = cursor.getString(2);
			String email = cursor.getString(3);
			String address = cursor.getString(4);
			cursor.close();
			return new Profile(id, firstName, lastName, email, address);
		} else {
			return new Profile();
		}
	}

	public void saveProfile(Profile profile) {
		ContentResolver cr = mContext.getContentResolver();
		ContentValues values=new ContentValues();
		values.put(Profiles.FIRST_NAME, profile.getFirstName());
		values.put(Profiles.LAST_NAME,profile.getLastName());
		values.put(Profiles.EMAIL,profile.getEmail());
		values.put(Profiles.ADDRESS,profile.getAddress());
		if(profile.getId()>0){
			cr.update(Profiles.CONTENT_URI, values, Profiles.PROFILE_ID+"=?", new String[]{String.valueOf(profile.getId())});
		}else{
			Uri createProfileUri=cr.insert(Profiles.CONTENT_URI, values);
			profile.setId(Integer.valueOf(createProfileUri.getLastPathSegment()));
		}
	}
}
