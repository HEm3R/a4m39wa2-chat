package org.ctu.fee.a4m39wa2.chalupa.chat.utils;

import org.jboss.security.auth.spi.Util;

public final class PasswordUtils {

    public static String generatePasswordHash(String password, String salt) {
        return Util.createPasswordHash("SHA-256", "hex", null, salt, password);
    }

    private PasswordUtils() {}
}
