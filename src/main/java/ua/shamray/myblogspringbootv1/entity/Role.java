package ua.shamray.myblogspringbootv1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role {

    @Id
    @Column(length = 16)
    private String name;


}
