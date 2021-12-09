package com.nwithan8.easypostdevtools.models;

import com.easypost.model.Address;

public class AddressPair {
    public Address from;
    public Address to;

    public AddressPair(Address from, Address to) {
        this.from = from;
        this.to = to;
    }
}
