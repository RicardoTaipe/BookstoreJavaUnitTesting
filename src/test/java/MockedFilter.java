public class MockedFilter implements BookFilter {
    boolean returnValue;
    boolean invoked;

    public MockedFilter(boolean returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public boolean apply(Book book) {
        invoked=true;
        return returnValue;
    }
}
