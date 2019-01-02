package com.webforum.bo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sebastian Löfstrand (selo@kth.se), Jonas Lundvall (jonlundv@kth.se)
 *
 * Represents Messages between Users
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {
    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="msg_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiver;

    @Column(name="msg_subject", nullable = false)
    private String subject;

    @Column(name="msg_content", nullable = false)
    private String content;

    @Column(name="msg_read", nullable = false, columnDefinition = "boolean default false")
    private Boolean read;

    @Column(name="msg_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted;

    // Getters/setters ----------------------------------------------------------------------------
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User fromUser) { this.sender = fromUser; }

    public User getReceiver() {return receiver; }
    public void setReceiver(User toUser) { this.receiver = toUser; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean isDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public Boolean isRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }


    // Constructors -------------------------------------------------------------------------------
    public Message() { }

    public Message(User sender, User receiver, String subject, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.read = false;
        this.deleted = false;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getUsername() +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", read=" + read +
                '}';
    }
}
