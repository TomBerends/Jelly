package main.java.com.jelly.tokenizer;


import main.java.com.jelly.scanner.Scanner;

import java.io.IOException;

public class Tokenizer implements AutoCloseable {
    private Scanner scanner;
    private boolean closed;

    public Tokenizer(final Scanner scanner) {
        this.scanner = scanner;
    }

    public Token next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        scanner.close();
        scanner = null;
        closed = true;
    }
}