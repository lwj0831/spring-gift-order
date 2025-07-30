package gift.member.domain;

import gift.wishlist.domain.WishItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private MemberType memberType;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<WishItem> wishItems = new ArrayList<>();

    protected Member() {
    }

    private Member(Long id, String name, MemberType memberType) {
        this.id = id;
        this.name = name;
        this.memberType = memberType;
    }

    public static Member of(String name, MemberType memberType) {
        return new Member(null, name, memberType);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public List<WishItem> getWishItems() {
        return wishItems;
    }

    public boolean isKakaoUser() {
        return this.memberType == MemberType.KAKAO;
    }
}
