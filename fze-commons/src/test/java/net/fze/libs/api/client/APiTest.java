package net.fze.libs.api.client;

public class APiTest {
    public void test(){
        ApiClient cli = new ApiClient("","","",30);
        cli.useToken((k,v)->{
            return "";
        },30000);
    }
}
