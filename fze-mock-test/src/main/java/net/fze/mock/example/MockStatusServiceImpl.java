package net.fze.mock.example;

import com.google.inject.Inject;
import net.fze.common.Context;

class MockStatusServiceImpl implements StatusService {
    @Inject
    private Context ctx;

    // @Inject private MemberRepo memberRepo;
    public String Hello(String word) {
        return "server return "
                + word
                + " database_host ="
                + this.ctx.registry().getString("database.host");
    }

    public String Ping() {
        return "pong";
    }

    public String TestDb() {
        return "no user";
    }
}
