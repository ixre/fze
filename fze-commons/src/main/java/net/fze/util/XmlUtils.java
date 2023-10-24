package net.fze.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class XmlUtils {
    /**
     * 反序列化,适用于属性与xml一致(区分大小写)的情况使用
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeObject(String xml) {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
        decoder.close();
        return (T) decoder.readObject();
    }

    /**
     * 序列化对象,适用于属性与xml一致(区分大小写)的情况使用
     */
    public static <T> String serializeObject(T entity) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(out));
        encoder.writeObject(entity);
        encoder.close();
        return out.toString();
    }
}
