package gift.global.utils;

public class PropertyPathUtils {

    private PropertyPathUtils() {
    }

    public static String extractFieldName(String propertyPath) {
        if (propertyPath == null || propertyPath.isEmpty()) {
            return "";
        }
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
    }
}
