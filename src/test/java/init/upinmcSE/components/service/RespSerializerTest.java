package init.upinmcSE.components.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RespSerializerTest {
    private final RespSerializer respSerializer = new RespSerializer();

    @Test
    public void testDeserializePing(){
        String ping = "*1\r\n$4\r\nPING\r\n";
        List<String[]> commands = respSerializer.deseralize(ping.getBytes(StandardCharsets.UTF_8));

        for(String[] command : commands){
            System.out.println("========");
            for (String s : command) {
                System.out.println(s + " ");
            }
        }

        assertEquals(1, commands.size());
        assertEquals(1, commands.get(0).length);
        assertEquals("PING", commands.get(0)[0]);
    }

    @Test
    public void testMultipleCommands(){
        String multipleCommands = "*2\r\n*3\r\n$3\r\nset\r\n$5\r\nupin1\r\n$6\r\nthanh1\r\n*3\r\n$3\r\nset\r\n$5\r\nupin2\r\n$6\r\nthanh2\u0000";
        List<String[]> commands = respSerializer.deseralize(multipleCommands.getBytes(StandardCharsets.UTF_8));
        System.out.println(commands.size());
        log.info("Received commands parsed");
        for(String[] s : commands){
            for(String ss: s){
                System.out.print(ss+" ");
            }
        }
        assertEquals(2, commands.size());
        assertEquals(3, commands.get(0).length);
        assertEquals(3, commands.get(1).length);

        assertEquals("set", commands.get(0)[0]);
        assertEquals("upin1", commands.get(0)[1]);
        assertEquals("thanh1", commands.get(0)[2]);

        assertEquals("set", commands.get(1)[0]);
        assertEquals("upin2", commands.get(1)[1]);
        assertEquals("thanh2", commands.get(1)[2]);
    }
}