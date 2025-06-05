package com.etranzact.vasgate.aedc.util;

public class SessionFactory {
    private static Session session;

    public static Session getSingleton() {
        if (session == null) {
            session = new Session();
        }

        return session;
    }
}
