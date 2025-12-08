package net.fze.ext.rbac.scope;

/**
 * 数据范围
 */
public enum DataScopes {
    /**
     * 全部数据权限
     */
    DATA_SCOPE_ALL(1),

    /**
     * 自定数据权限
     */
    DATA_SCOPE_CUSTOM(2),

    /**
     * 部门数据权限
     */
    DATA_SCOPE_DEPT(3),

    /**
     * 部门及以下数据权限
     */
    DATA_SCOPE_DEPT_AND_CHILD(4),

    /**
     * 仅本人数据权限
     */
    DATA_SCOPE_SELF(5);


    private final int value;

    private DataScopes(int value) {
        this.value = value;
    }

    public static DataScopes parse(String dataScope) {
       switch (dataScope) {
           case "1": case "ALL":return DATA_SCOPE_ALL;
           case "2": case "CUSTOM":return DATA_SCOPE_CUSTOM;
           case "3": case "DEPART":return DATA_SCOPE_DEPT;
           case "4": case "DEPART_AND_CHILDREN":return DATA_SCOPE_DEPT_AND_CHILD;
           case "5": case "USER_SELF":return DATA_SCOPE_SELF;
           default: return null;
       }
    }

    public int getValue() {
        return value;
    }
}
