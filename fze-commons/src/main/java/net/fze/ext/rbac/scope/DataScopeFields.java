/**
 * Copyright (C) 2007-2026 56X.NET,All rights reserved.
 * <p>
 * name : DataScopeFields.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2026-02-01 11:10
 * description :
 * history :
 */
package net.fze.ext.rbac.scope;

/**
 * @author jarrysix
 */
public class DataScopeFields {
    private final String departField;
    private final String userField;

    public DataScopeFields(String departField, String userField) {
        this.departField = departField;
        this.userField = userField;
    }

    public String getDepartField() {
        return departField;
    }
    public String getUserField() {
        return userField;
    }
}