package main.java.com.jelly.scanner;

class StringScanner implements ScannerImplementation {
    char[] source;
    int idx;

    StringScanner(final String source) {
        this.source = source.toCharArray();
    }

    @Override
    public char next() {
        return source[idx++];
    }

    @Override
    public boolean hasNext() {
        return idx < source.length;
    }

    @Override
    public void close() {
        source = null;
    }
}
