package com.example.pvfrota.graalJS.proxy;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 26/08/2024
 */
public class StringProxy implements ProxyObject {
    private String value;

    public StringProxy(String value) {
        this.value = value;
    }

    @Override
    public Object getMember(String key) {
        if ("value".equals(key)) {
            return value;
        }
        return null;
    }

    @Override
    public boolean hasMember(String key) {
        return "value".equals(key);
    }

    @Override
    public void putMember(String key, Value value) {
        if ("value".equals(key)) {
            this.value = value.asString();
        }
    }

    @Override
    public boolean removeMember(String key) {
        return false;
    }

    @Override
    public Object getMemberKeys() {
        return new String[]{"value"};
    }
}
