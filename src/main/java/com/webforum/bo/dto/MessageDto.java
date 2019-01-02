package com.webforum.bo.dto;

/**
 * @author Sebastian Löfstrand (selo@kth.se), Jonas Lundvall (jonlundv@kth.se)
 *
 * Data transfer object for Messages
 */
public class MessageDto {

    // Properties ---------------------------------------------------------------------------------
    private Long id;
    private Long sender;
    private Long receiver;
    private String subject;
    private String content;

    // Getters/setters ----------------------------------------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return sender;
    }
    public void setSender(Long senderId) {
        this.sender = senderId;
    }

    public Long getReceiver() {
        return receiver;
    }
    public void setReceiver(Long receiverId) {
        this.receiver = receiverId;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    // Constructors -------------------------------------------------------------------------------
    public MessageDto() {
    }

    public MessageDto(Long id, Long senderId, Long receiverId, String subject, String content) {
        this.id = id;
        this.sender = senderId;
        this.receiver = receiverId;
        this.subject = subject;
        this.content = content;
    }

}
