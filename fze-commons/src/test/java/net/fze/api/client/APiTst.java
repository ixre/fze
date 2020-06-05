package net.fze.api.client;

public class APiTst {
    public void test(){
        IAccessToken ac = (k,v)->{
            return "";
        };
        ApiClient cli = new ApiClient("","","",ac,30,30);
    }
}
