import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("A BookShelf")
public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;

    @BeforeEach
    public void init() {
        shelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch",
                LocalDate.of(2008, Month.MAY, 8));
        codeComplete = new Book("Code Complete", "Steve McConnel",
                LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks",
                LocalDate.of(1975, Month.JANUARY, 1));
    }

    @Test
    @DisplayName("is empty when no book added")
    public void shelfEmptyWhenNoBookAdded() {
        List<Book> books = shelf.books();
        assertTrue(books.isEmpty(), "BookShelf should be empty.");
    }

    @Test
    public void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
        shelf.add(effectiveJava, codeComplete);
        List<Book> books = shelf.books();
        assertEquals(2, books.size(), "Bookshelf should have two books");
    }

    @Test
    public void emptyBookShelfWhenAddIsCalledWithoutBooks() {
        shelf.add();
        List<Book> books = shelf.books();
        assertTrue(books.isEmpty(), "BookShelf should be empty.");
    }

    @Test
    public void bookshelfArrangedByTitle() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        List<Book> books = shelf.arrange();
        assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth),
                books, "Books in a bookshelf should be arranged lexicographically by book title");
    }

    @Test
    public void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        shelf.arrange();
        List<Book> books = shelf.books();
        assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth),
                books, "Books in bookshelf are in insertion order");
    }

    @Test
    public void bookshelfArrangedByUserProvidedCriteria() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
        List<Book> books = shelf.arrange(reversed);
        assertThat(books).isSortedAccordingTo(reversed);
    }
}
