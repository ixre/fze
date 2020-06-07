package net.fze.libs.api.client;

public class APiTst {
    public void test(){
        ApiClient cli = new ApiClient("","","",30);
        cli.useToken((k,v)->{
            return "";
        },30000);
    }
}
