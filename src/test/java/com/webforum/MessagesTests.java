package com.webforum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforum.bo.dto.MessageDto;
import com.webforum.bo.entity.Message;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

public class MessagesTests {

    public static final String REST_USERS_URI = "http://192.168.99.100:8080/api/users";
    public static final String REST_MESSAGES_URI = "http://192.168.99.100:8082/api/messages";

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SuppressWarnings("unchecked")
    public void getAllMessages() {
        ResponseEntity<Collection<Message>> response = restTemplate.exchange(REST_MESSAGES_URI + "/", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Message>>() { });
        Collection<Message> messages = (Collection<Message>) response.getBody();

        if(messages.isEmpty()) {
            System.out.println("No messages");
        } else {
          for(Message msg : messages)
              System.out.println(msg.getContent());
        }
    }


    @Test
    public void saveMessage() {

        MessageDto messageDto = new MessageDto();
        messageDto.setReceiver(1L);
        messageDto.setSender(2L);
        messageDto.setSubject("My testsubject");
        messageDto.setContent("My test content for the message");

        try {
            URI uri = restTemplate.postForLocation(REST_MESSAGES_URI + "/", messageDto, MessageDto.class);
            System.out.println("Location : " + uri.toASCIIString());
        } catch(
                HttpClientErrorException.Conflict e) {
            try {
                CustomErrorType cet = objectMapper.readValue(e.getResponseBodyAsString(), CustomErrorType.class);
                System.out.println(cet.getErrorMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Test
    @SuppressWarnings("unchecked")
    public void getInboxMessagesForUser() {
        ResponseEntity<Collection<Message>> response = restTemplate.exchange(REST_MESSAGES_URI + "/user/2/inbox/", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Message>>() { });
        Collection<Message> messages = (Collection<Message>) response.getBody();

        if(messages.isEmpty()) {
            System.out.println("No messages");
        } else {
            for(Message msg : messages)
                System.out.println("Sender: " + msg.getSender().getUsername() + ", Receiver: " + msg.getReceiver().getUsername() + ", [" + msg.getContent() + "]");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getOutboxMessagesForUser() {
        ResponseEntity<Collection<Message>> response = restTemplate.exchange(REST_MESSAGES_URI + "/user/2/outbox/", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Message>>() { });
        Collection<Message> messages = (Collection<Message>) response.getBody();

        if(messages.isEmpty()) {
            System.out.println("No messages");
        } else {
            for(Message msg : messages)
                System.out.println("Sender: " + msg.getSender().getUsername() + ", Receiver: " + msg.getReceiver().getUsername() + ", [" + msg.getContent() + "]");
        }
    }
}
