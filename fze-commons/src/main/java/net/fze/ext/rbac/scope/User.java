package net.fze.ext.rbac.scope;

import java.util.List;

public class User {
    private List<Role> roles;
    private long userId;

    private long departId;
    private boolean isAdministrator;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    public long getDepartId() {
        return departId;
    }

    public void setDepartId(long departId) {
        this.departId = departId;
    }
}
