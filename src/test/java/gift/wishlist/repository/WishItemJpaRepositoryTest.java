package gift.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.wishlist.domain.WishItem;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@DisplayName("WishItemJpaRepository 테스트")
@ActiveProfiles("test")
class WishItemJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WishItemJpaRepository wishItemRepository;

    @Test
    @DisplayName("위시아이템을 저장하고 조회할 수 있다")
    void saveAndFindWishItem() {
        Member member = Member.of("lee");
        Product product = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product);

        WishItem wishItem = WishItem.of(member, product);

        WishItem savedWishItem = wishItemRepository.save(wishItem);

        assertAll(
            () -> assertThat(savedWishItem.getId()).isNotNull(),
            () -> assertThat(savedWishItem.getMember().getId()).isEqualTo(member.getId()),
            () -> assertThat(savedWishItem.getProduct().getId()).isEqualTo(product.getId())
        );
    }

    @Test
    @DisplayName("멤버ID와 상품ID로 위시아이템을 조회할 수 있다")
    void findByMemberIdAndProductId() {
        Member member = Member.of("lee");
        Product product = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product);

        WishItem wishItem = WishItem.of(member, product);
        entityManager.persistAndFlush(wishItem);

        Optional<WishItem> foundWishItem = wishItemRepository.findByMemberIdAndProductId(
            member.getId(), product.getId());

        assertAll(
            () -> assertThat(foundWishItem).isPresent(),
            () -> assertThat(foundWishItem.get().getMember().getId()).isEqualTo(member.getId()),
            () -> assertThat(foundWishItem.get().getProduct().getId()).isEqualTo(product.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 멤버ID와 상품ID로 조회시 빈 Optional을 반환한다")
    void findByMemberIdAndProductIdNotFound() {
        Optional<WishItem> foundWishItem = wishItemRepository.findByMemberIdAndProductId(999L,
            999L);
        assertThat(foundWishItem).isEmpty();
    }

    @Test
    @DisplayName("멤버ID로 상품 정보와 함께 위시아이템 목록을 페이지네이션으로 조회할 수 있다")
    void findAllWithProductByMemberId_withPaging() {
        Member member = Member.of("lee");
        Product product1 = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));
        Product product2 = Product.of("testProduct", 2000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);

        WishItem wishItem1 = WishItem.of(member, product1);
        WishItem wishItem2 = WishItem.of(member, product2);

        entityManager.persistAndFlush(wishItem1);
        entityManager.persistAndFlush(wishItem2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<WishItem> page = wishItemRepository.findAllWithProductByMemberId(member.getId(),
            pageable);
        List<WishItem> wishItems = page.getContent();

        assertAll(
            () -> assertThat(wishItems).hasSize(2),
            () -> assertThat(wishItems.get(0).getProduct().getName()).isEqualTo("testProduct"),
            () -> assertThat(wishItems.get(1).getProduct().getName()).isEqualTo("testProduct"),
            () -> assertThat(wishItems.get(0).getMember().getId()).isEqualTo(member.getId()),
            () -> assertThat(wishItems.get(1).getMember().getId()).isEqualTo(member.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 멤버ID로 조회 시 빈 페이지를 반환한다")
    void findAllWithProductByMemberIdNotFound_withPaging() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WishItem> page = wishItemRepository.findAllWithProductByMemberId(999L, pageable);

        assertAll(
            () -> assertThat(page).isNotNull(),
            () -> assertThat(page.getContent()).isEmpty(),
            () -> assertThat(page.getTotalElements()).isZero()
        );
    }


    @Test
    @DisplayName("위시아이템을 삭제할 수 있다")
    void deleteWishItem() {
        Member member = Member.of("lee");
        Product product = Product.of("testProduct", 800000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product);

        WishItem wishItem = WishItem.of(member, product);
        WishItem savedWishItem = entityManager.persistAndFlush(wishItem);

        wishItemRepository.delete(savedWishItem);
        entityManager.flush();

        Optional<WishItem> foundWishItem = wishItemRepository.findById(savedWishItem.getId());
        assertThat(foundWishItem).isEmpty();
    }

    @Test
    @DisplayName("ID로 위시아이템을 조회할 수 있다")
    void findById() {
        Member member = Member.of("lee");
        Product product = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product);

        WishItem wishItem = WishItem.of(member, product);
        WishItem savedWishItem = entityManager.persistAndFlush(wishItem);

        Optional<WishItem> foundWishItem = wishItemRepository.findById(savedWishItem.getId());

        assertAll(
            () -> assertThat(foundWishItem).isPresent(),
            () -> assertThat(foundWishItem.get().getMember().getId()).isEqualTo(member.getId()),
            () -> assertThat(foundWishItem.get().getProduct().getId()).isEqualTo(product.getId())
        );
    }

    @Test
    @DisplayName("전체 위시아이템 수를 조회할 수 있다")
    void countAllWishItems() {
        Member member1 = Member.of("lee");
        Member member2 = Member.of("kim");
        Product product1 = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));
        Product product2 = Product.of("testProduct", 2000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member1);
        entityManager.persistAndFlush(member2);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);

        WishItem wishItem1 = WishItem.of(member1, product1);
        WishItem wishItem2 = WishItem.of(member2, product2);

        entityManager.persistAndFlush(wishItem1);
        entityManager.persistAndFlush(wishItem2);

        long count = wishItemRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("위시아이템이 존재하는지 확인할 수 있다")
    void existsById() {
        Member member = Member.of("lee");
        Product product = Product.of("testProduct", 1000, "description", "image.jpg",
            List.of(ProductOption.of("testOption", 10)));

        entityManager.persistAndFlush(member);
        entityManager.persistAndFlush(product);

        WishItem wishItem = WishItem.of(member, product);
        WishItem savedWishItem = entityManager.persistAndFlush(wishItem);

        assertAll(
            () -> assertThat(wishItemRepository.existsById(savedWishItem.getId())).isTrue(),
            () -> assertThat(wishItemRepository.existsById(999L)).isFalse()
        );
    }
}
