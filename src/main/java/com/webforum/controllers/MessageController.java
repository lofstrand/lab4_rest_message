package com.webforum.controllers;

import com.webforum.bo.dto.MessageDto;
import com.webforum.bo.entity.Message;
import com.webforum.bo.entity.User;
import com.webforum.repositories.MessageRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Sebastian Löfstrand (selo@kth.se), Jonas Lundvall (jonlundv@kth.se)
 *
 * Message controller for REST API requests
 */
@Controller
@RequestMapping(path="/api/messages")
public class MessageController {
    // Constants ----------------------------------------------------------------------------------
    public static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    public static final String REST_USERS_URI = "http://192.168.99.100:8082/rest/api/users";

    // Properties ---------------------------------------------------------------------------------
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MessageRepository messageRepository;

    // Actions ------------------------------------------------------------------------------------
    /**
     * Find all messages
     *
     * @return List of messages
     */
    @GetMapping(path="/")
    @ResponseBody
    public ResponseEntity<List<Message>> findAll() {
        List<Message> messages = (List<Message>) messageRepository.findAll();

        if (messages.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    /**
     * Create message
     *
     * @param messageDto the message to create
     * @param ucBuilder
     * @return response entity
     */
    @PostMapping(path="/")
    public ResponseEntity<?> create(@RequestBody MessageDto messageDto, UriComponentsBuilder ucBuilder) {
        Message message = convertToEntity(messageDto);

        if(message == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        logger.info("Creating Message : {}", message);

        messageRepository.save(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/messages/{id}").buildAndExpand(message.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /**
     *  Retrieve all inbox messages by user id
     *
     * @param id the user id
     * @return the response entity
     */
    @GetMapping(path="/user/{id}/inbox/")
    @ResponseBody
    public ResponseEntity<?> findInboxByUserId(@PathVariable Long id) {
        User user = new User();
        user.setId(Long.valueOf(id));
        Collection<Message> messages = (Collection<Message>) messageRepository.findByReceiver(user);
        if (messages.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Collection<Message>>(messages, HttpStatus.OK);
    }

    /**
     *  Retrieve all outbox messages by user id
     *
     * @param id the user id
     * @return the response entity
     */
    @GetMapping(path="/user/{id}/outbox/")
    @ResponseBody
    public ResponseEntity<?> findOutboxByUserId(@PathVariable Long id) {
        User user = new User();
        user.setId(Long.valueOf(id));
        Collection<Message> messages = (Collection<Message>) messageRepository.findBySender(user);
        if (messages.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Collection<Message>>(messages, HttpStatus.OK);
    }

    /**
     * Converter from message to message dto
     * @param message the message to convert
     * @return the message dto
     */
    private MessageDto convertToDto(Message message) {
        MessageDto messageDto = modelMapper.map(message, MessageDto.class);
        messageDto.setId(message.getId());

        return messageDto;
    }

    /**
     * Converter from message dto to message
     *
     * @param messageDto the message dto to convert
     * @return the message
     * @throws ParseException
     */
    private Message convertToEntity(MessageDto messageDto) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();

        Message message = modelMapper.map(messageDto, Message.class);

        // Fetch sender user
        User userSender = restTemplate.getForObject(REST_USERS_URI + "/" + messageDto.getSender(), User.class);
        if(userSender == null)
            return null;

        message.setSender(userSender);
        //message.setViewSender(userSender.getUsername());

        // Fetch receiver user
        User userReceiver = restTemplate.getForObject(REST_USERS_URI + "/" + messageDto.getReceiver(), User.class);
        if(userReceiver == null)
            return null;

        message.setReceiver(userReceiver);
        //message.setViewReceiver(userReceiver.getUsername());
        message.setRead(false);
        message.setDeleted(false);

        return message;
    }
}
