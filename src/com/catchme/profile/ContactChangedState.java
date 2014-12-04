package com.catchme.profile;

import com.catchme.exampleObjects.ExampleItem.ContactStateType;

public interface ContactChangedState {
	public void contactChangedState(ContactStateType newState);
}
