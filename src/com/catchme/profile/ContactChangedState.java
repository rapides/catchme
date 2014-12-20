package com.catchme.profile;

import com.catchme.model.ExampleItem.ContactStateType;

public interface ContactChangedState {
	public void contactChangedState(ContactStateType newState);
}
