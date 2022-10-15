package onde.there.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    @Id
    @Column(name = "member_id")
    private String id;
    private String email;
    private String password;
    private String name;
}
