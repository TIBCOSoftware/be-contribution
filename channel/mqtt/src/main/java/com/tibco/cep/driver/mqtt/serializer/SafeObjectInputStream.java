/*
 * Copyright (c) 2026. Cloud Software Group, Inc. All Rights Reserved. Confidential & Proprietary.
 */

package com.tibco.cep.driver.mqtt.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for ObjectInputStreams hardened with a JEP-290 allowlist filter, to
 * prevent deserialization of untrusted/gadget classes (CWE-502). Only classes
 * whose name starts with one of the allowed prefixes are permitted; every other
 * class is rejected. Extra prefixes may be added at runtime via the system
 * property "be.contribution.deserialization.allowlist" (comma-separated).
 */
public final class SafeObjectInputStream {

    private SafeObjectInputStream() {
    }

    public static ObjectInputStream create(InputStream in, String... allowedPrefixes) throws IOException {
        final List<String> allowed = new ArrayList<>();
        for (String p : allowedPrefixes) {
            allowed.add(p);
        }
        final String extra = System.getProperty("be.contribution.deserialization.allowlist", "");
        for (String p : extra.split(",")) {
            p = p.trim();
            if (!p.isEmpty()) {
                allowed.add(p);
            }
        }

        final ObjectInputStream ois = new ObjectInputStream(in);
        ois.setObjectInputFilter(info -> {
            final Class<?> clazz = info.serialClass();
            if (clazz == null) {
                return ObjectInputFilter.Status.UNDECIDED;
            }
            String name = clazz.getName();
            int dims = 0;
            while (dims < name.length() && name.charAt(dims) == '[') {
                dims++;
            }
            if (dims > 0) {
                if (name.charAt(dims) != 'L') {
                    return ObjectInputFilter.Status.ALLOWED; // primitive array e.g. [B
                }
                name = name.substring(dims + 1, name.endsWith(";") ? name.length() - 1 : name.length());
            }
            for (String prefix : allowed) {
                if (name.equals(prefix) || name.startsWith(prefix)) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
            }
            return ObjectInputFilter.Status.REJECTED;
        });
        return ois;
    }

    public static Object deserialize(byte[] data, String... allowedPrefixes) {
        try (ObjectInputStream ois = create(new ByteArrayInputStream(data), allowedPrefixes)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Rejected or invalid serialized object: " + e.getMessage(), e);
        }
    }
}
