import java.time.LocalDate;

public class BookPublishedYearFilter implements BookFilter {
    private LocalDate startDate;

    public static BookFilter After(int year) {
        BookPublishedYearFilter filter = new BookPublishedYearFilter();
        filter.startDate = LocalDate.of(year, 12, 31);
        return filter;
    }

    @Override
    public boolean apply(Book book) {
        return book.getPublishedOn().isAfter(startDate);
    }
}
