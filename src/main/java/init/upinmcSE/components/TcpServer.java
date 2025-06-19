package init.upinmcSE.components;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TcpServer {
    @Autowired
    private RespSerializer respSerializer;

    public void startServer(){
        respSerializer.printWorking();
    }
}

