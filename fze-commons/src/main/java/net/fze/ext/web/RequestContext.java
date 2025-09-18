package net.fze.ext.web;


/**
 * 请求上下文
 */
public class RequestContext {
    private final String tenantId;
    private final String userId;

    public RequestContext(String userId,String tenantId){
        this.tenantId = tenantId;
        this.userId = userId;
    }
    public String getTenantId(){
        return tenantId;
    }
}
