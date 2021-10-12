import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookFilterSpec {
    private Book cleanCode;
    private Book codeComplete;

    @BeforeEach
    void init() {
        cleanCode = new Book("Clean Code", "Robert C. Martin",
                LocalDate.of(2008, Month.AUGUST, 1));
        codeComplete = new Book("Code Complete", "Steve McConnel",
                LocalDate.of(2004, Month.JUNE, 9));
    }

    @Nested
    @DisplayName("book published date")
    class BookPublishedFilterSpec {
        @Test
        @DisplayName("is after specified year")
        void validateBookPublishedDatePostAskedYear() {
            BookFilter filter = BookPublishedYearFilter.After(2007);
            assertTrue(filter.apply(cleanCode));
            assertFalse(filter.apply(codeComplete));
        }

        @Test
        @DisplayName("Composite criteria is based on multiple filters")
        void shouldFilterOnMultiplesCriteria() {
            CompositeFilter compositeFilter = new CompositeFilter();
            compositeFilter.addFilter(b -> false);
            assertFalse(compositeFilter.apply(cleanCode));
        }
        @Test
        @DisplayName("Composite criteria does not invoke after first failure")
        void shouldNotInvokeAfterFirstFailure() {
            CompositeFilter compositeFilter = new CompositeFilter();
            compositeFilter.addFilter(b -> false);
            compositeFilter.addFilter(b -> true);
            assertFalse(compositeFilter.apply(cleanCode));
        }

        @Test
        @DisplayName("Composite criteria invokes all filters")
        void shouldInvokeAllFilters() {
            CompositeFilter compositeFilter = new CompositeFilter();
            compositeFilter.addFilter(b -> true);
            compositeFilter.addFilter(b -> true);
            assertTrue(compositeFilter.apply(cleanCode));
        }
    }
}
