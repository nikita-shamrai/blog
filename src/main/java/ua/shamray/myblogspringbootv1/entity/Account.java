package ua.shamray.myblogspringbootv1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Post> postList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    private Set<Role> roles = new HashSet<>();

}
