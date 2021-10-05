import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A BookShelf")
public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;
    @BeforeEach
    public void init() {
        shelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch",
                LocalDate.of(2008, Month.MAY, 8));
        codeComplete = new Book("Code Complete", "Steve McConnel",
                LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks",
                LocalDate.of(1975, Month.JANUARY, 1));
        cleanCode = new Book("Clean Code", "Robert C. Martin",
                LocalDate. of(2008, Month.AUGUST, 1));
    }

    @Test
    @DisplayName("is empty when no book added to it")
    public void shelfEmptyWhenNoBookAdded() {
        List<Book> books = shelf.books();
        assertTrue(books.isEmpty(), "BookShelf should be empty.");
    }

    @Test
    @DisplayName("bookshelf contains two books when two books are added")
    public void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
        shelf.add(effectiveJava, codeComplete);
        List<Book> books = shelf.books();
        assertEquals(2, books.size(), "Bookshelf should have two books");
    }

    @Test
    @DisplayName("empty bookshelf remain empty when add is called without books")
    public void emptyBookShelfWhenAddIsCalledWithoutBooks() {
        shelf.add();
        List<Book> books = shelf.books();
        assertTrue(books.isEmpty(), "BookShelf should be empty.");
    }

    @Test
    @DisplayName("bookshelf returns an immutable books collection to client")
    void booksReturnedFromBookShelfIsImmutableForClient() {
        shelf.add(effectiveJava, codeComplete);
        List<Book> books = shelf.books();
        assertThatThrownBy(() -> books.add(mythicalManMonth))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("bookshelf is arranged lexicographically by book title ")
    public void bookshelfArrangedByTitle() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        List<Book> books = shelf.arrange();
        assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth),
                books, "Books in a bookshelf should be arranged lexicographically by book title");
    }

    @Test
    @DisplayName("bookshelf is arranged by user provided criteria")
    public void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        shelf.arrange();
        List<Book> books = shelf.books();
        assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth),
                books, "Books in bookshelf are in insertion order");
    }

    @Test
    @DisplayName("books inside bookshelf are grouped according to user provided criteria")
    public void bookshelfArrangedByUserProvidedCriteria() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
        List<Book> books = shelf.arrange(reversed);
        assertThat(books).isSortedAccordingTo(reversed);
    }

    @Test
    @DisplayName("books inside bookshelf are grouped by publication year")
    public void groupBooksInsideBookShelfByPublicationYear() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();
        assertThat(booksByPublicationYear)
                .containsKey(Year.of(2008))
                .containsValues(Arrays.asList(effectiveJava, cleanCode));

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(2004))
                .containsValues(singletonList(codeComplete));

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(1975))
                .containsValues(singletonList(mythicalManMonth));
    }

    @Test
    @DisplayName("books inside bookshelf are grouped according to user provided criteria(group by author name)")
    void groupBooksByUserProvidedCriteria() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        Map<String, List<Book>> booksByAuthor = shelf.groupBy(Book::getAuthor);
        assertThat(booksByAuthor).containsKey("Joshua Bloch")
                .containsValues(singletonList(effectiveJava));
        assertThat(booksByAuthor).containsKey("Steve McConnel")
                .containsValues(singletonList(codeComplete));
        assertThat(booksByAuthor)
                .containsKey("Frederick Phillips Brooks")
                .containsValues(singletonList(mythicalManMonth));
        assertThat(booksByAuthor).
                containsKey("Robert C. Martin")
                .containsValues(singletonList(cleanCode));
    }
}
