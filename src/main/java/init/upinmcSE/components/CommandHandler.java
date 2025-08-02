package init.upinmcSE.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {
    @Autowired
    public RespSerializer respSerializer;

    public String ping(String[] command){
        return "+PONG\r\n";
    }

    public String echo(String[] command){
        return respSerializer.serializeBulkString(command[1]);
    }

    public String set(String[] command){
        return "";
    }

    public String get(String[] command){
        return "";
    }
}
