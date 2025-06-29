package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
import com.thelastcodebenders.epsy_backend.models.types.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "messages")
public class Message extends AuditableEntity {
    @Id
    @Builder.Default private UUID messageId = UUID.randomUUID();

    private UUID roomId;

    private String content;
    private MessageType messageType;
}

// message_id, content, messageType, room_id
