public class Progress {
    private final int completed;
    private final int toRead;
    private final int inProgress;

    public Progress(int completed, int toRead, int inProgress) {
        this.completed = completed;
        this.toRead = toRead;
        this.inProgress = inProgress;
    }

    public int completed() {
        return completed;
    }

    public int toRead() {
        return toRead;
    }

    public int inProgress() {
        return inProgress;
    }
}
