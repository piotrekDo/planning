package com.piotrdomagalski.planning.mailing;

/**
 * An enum containing information about specific mail boxes.
 */

enum Mailbox {
    MAILBOX_REGISTER("register.planning@piodom.com", "planning"),
    MAILBOX_PASSWORD("password.planning@piodom.com", "tatZyz-givgaz-xyqdo7");

    public String getAddress() {
        return address;
    }

    public String getPass() {
        return pass;
    }

    private final String address;
    private final String pass;

    Mailbox(String address, String pass) {
        this.address = address;
        this.pass = pass;
    }
}
