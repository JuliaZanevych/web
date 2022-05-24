package org.lpnu.messenger;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

// The chat message-handling Controller
@org.springframework.stereotype.Controller
public class Controller {
    // mapped to handle chat messages to the /sendmsg destination
    @MessageMapping("/sendmsg")
    // the return value is broadcast to all subscribers of /chat/messages
    @SendTo("/chat/messages")
    public Message chat(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Message(message.getText(), message.getUsername(), message.getAvatar());
    }
}
