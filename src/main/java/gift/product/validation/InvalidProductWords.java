package gift.product.validation;

import java.util.List;

public class InvalidProductWords {

    private static final List<String> invalidWordList = List.of("카카오");

    public static boolean contains(String productName) {
        return invalidWordList.stream()
            .anyMatch(productName::contains);
    }

    public static List<String> findMatches(String productName) {
        return invalidWordList.stream()
            .filter(productName::contains)
            .toList();
    }

}
