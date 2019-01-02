package com.webforum.repositories;

import com.webforum.bo.entity.Message;
import com.webforum.bo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * @author Sebastian Löfstrand (selo@kth.se), Jonas Lundvall (jonlundv@kth.se)
 *
 * Message repository auto implemented by Spring into a Bean called messageRepository
 * CRUD refers Create, Read, Update, Delete
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
    Collection<Message> findByReceiver(User receiver);
    Collection<Message> findBySender(User sender);
}