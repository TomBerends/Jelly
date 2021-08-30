package com.jelly;

import com.jelly.lexer.Lexer;
import com.jelly.scanner.Scanner;
import org.jetbrains.annotations.Contract;

import java.io.File;

public class Main {
    @Contract(pure = true)
    public static void main(final String[] args) throws Exception {
        try(final Lexer lexer = new Lexer(new Scanner(new File("src/main/resources/source")))) {
            while(lexer.hasNext())
                System.out.println(lexer.next());
        }
    }
}
