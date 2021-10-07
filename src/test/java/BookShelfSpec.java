import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("A BookShelf")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    public void init(Map<String, Book> books) {
        shelf = new BookShelf();
        this.effectiveJava = books.get("Effective Java");
        this.codeComplete = books.get("Code Complete");
        this.mythicalManMonth = books.get("The Mythical Man-Month");
        this.cleanCode = books.get("Clean Code");
    }

    @Nested
    @DisplayName("is empty")
    class Empty {

        @Test
        @DisplayName("when no book added to it")
        public void shelfEmptyWhenNoBookAdded() {
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), "BookShelf should be empty.");
        }

        @Test
        @DisplayName("when add is called without books")
        public void emptyBookShelfWhenAddIsCalledWithoutBooks() {
            shelf.add();
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), "BookShelf should be empty.");
        }
    }

    @Nested
    @DisplayName("after adding books")
    class BooksAreAdded {
        @Test
        @DisplayName("contains two books")
        public void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            assertEquals(2, books.size(), "Bookshelf should have two books");
        }

        @Test
        @DisplayName("returns an immutable books collection to client")
        void booksReturnedFromBookShelfIsImmutableForClient() {
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            assertThatThrownBy(() -> books.add(mythicalManMonth))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("is arranged")
    class Arranged {
        @Test
        @DisplayName("lexicographically by book title ")
        public void bookshelfArrangedByTitle() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange();
            assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth),
                    books, "Books in a bookshelf should be arranged lexicographically by book title");
        }

        @Test
        @DisplayName("by user provided criteria (by book title lexicographically descending order )")
        public void bookshelfArrangedByUserProvidedCriteria() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
            List<Book> books = shelf.arrange(reversed);
            assertThat(books).isSortedAccordingTo(reversed);
        }

        @Test
        @DisplayName("by user provided criteria")
        public void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            shelf.arrange();
            List<Book> books = shelf.books();
            assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth),
                    books, "Books in bookshelf are in insertion order");
        }
    }

    @Nested
    @DisplayName("books are grouped by")
    class Group {
        @Test
        @DisplayName("user provided criteria(group by author name)")
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

        @Test
        @DisplayName("publication year")
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

    }

    @Nested
    @DisplayName("search")
    class BookShelfSearchSpec {
        @BeforeEach
        void setup() {
            shelf.add(codeComplete, effectiveJava, mythicalManMonth, cleanCode);
        }

        @Test
        @DisplayName("should find books with title containing text")
        void shouldFindBooksWithTitleContainingText() {
            List<Book> books = shelf.findBooksByTitle("code");
            assertThat(books.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("should find books with title containing text and published after specified date")
        void shouldFilterSearchedBookOnPublishedDate() {
            List<Book> books = shelf.findBooksByTitle("code", b -> b.getPublishedOn().isBefore(LocalDate.of(2014, 12, 31)));
            assertThat(books.size()).isEqualTo(2);
        }
    }
}
