import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
            BookFilter mockedFilter = mock(BookFilter.class);
            when(mockedFilter.apply(cleanCode)).thenReturn(true);
            CompositeFilter compositeFilter = new CompositeFilter();
            compositeFilter.addFilter(mockedFilter);
            compositeFilter.apply(cleanCode);
            verify(mockedFilter).apply(cleanCode);
        }

        @Test
        @DisplayName("Composite criteria  invokes all incase of failure")
        void shouldInvokeAllInFailure() {
            CompositeFilter compositeFilter = new CompositeFilter();

            BookFilter invokedMockedFilter = mock(BookFilter.class);
            when(invokedMockedFilter.apply(cleanCode)).thenReturn(false);
            compositeFilter.addFilter(invokedMockedFilter);

            BookFilter secondInvokedMockedFilter = mock(BookFilter.class);
            when(secondInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(secondInvokedMockedFilter);

            assertFalse(compositeFilter.apply(cleanCode));
            verify(invokedMockedFilter).apply(cleanCode);
            verify(secondInvokedMockedFilter).apply(cleanCode);
        }

        @Test
        @DisplayName("Composite criteria invokes all filters")
        void shouldInvokeAllFilters() {
            CompositeFilter compositeFilter = new CompositeFilter();
            BookFilter firstInvokedMockedFilter = mock(BookFilter.class);
            when(firstInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(firstInvokedMockedFilter);
            BookFilter secondInvokedMockedFilter = mock(BookFilter.class);
            when(secondInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(secondInvokedMockedFilter);
            assertTrue(compositeFilter.apply(cleanCode));
            verify(firstInvokedMockedFilter).apply(cleanCode);
            verify(secondInvokedMockedFilter).apply(cleanCode);
        }
    }
}
