package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
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
@Table(name = "message_rooms")
public class MessageRoom extends AuditableEntity {
    @Id
    @Builder.Default private UUID roomId = UUID.randomUUID();

    private UUID sender1;
    private UUID sender2;
}


// message_room_id, sender1, sender2, created_at, updated_at