package gift.product.validation;

import gift.product.exception.DuplicateProductOptionNameException;
import gift.product.exception.ProductOptionNameAlreadyExistsException;
import gift.product.repository.ProductOptionJpaRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionValidator {

    private final ProductOptionJpaRepository productOptionRepository;

    public ProductOptionValidator(ProductOptionJpaRepository productOptionRepository) {
        this.productOptionRepository = productOptionRepository;
    }

    public void validateOptionNameUniqueness(Long productId, List<String> names) {
        List<String> existingNames = productOptionRepository.findExistingNamesByProductIdAndNameIn(
            productId, names);

        if (!existingNames.isEmpty()) {
            throw new ProductOptionNameAlreadyExistsException(existingNames.toString());
        }
    }

    public void validateOptionNameDuplicateInRequest(List<String> names) {
        Set<String> seen = new HashSet<>();
        names.stream()
            .filter(name -> !seen.add(name))
            .findFirst()
            .ifPresent(duplicateName -> {
                throw new DuplicateProductOptionNameException(duplicateName);
            });
    }


}
