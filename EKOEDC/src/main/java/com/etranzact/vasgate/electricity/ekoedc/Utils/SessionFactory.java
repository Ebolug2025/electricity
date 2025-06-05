package com.etranzact.vasgate.electricity.ekoedc.Utils;

public class SessionFactory {
    private static Session session;

    public static Session getSingleton() {
        if (session == null) {
            session = new Session();
        }

        return session;
    }
}
