package gift.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Member() {
    }

    private Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Member of(String name) {
        return new Member(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
