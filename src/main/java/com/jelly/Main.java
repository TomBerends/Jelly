package com.jelly;

import com.jelly.scanner.Scanner;
import org.jetbrains.annotations.Contract;

import java.io.File;

public class Main {
    @Contract(pure = true)
    public static void main(final String[] args) throws Exception {
        try(final Scanner sc = new Scanner(new File("src/main/resources/source"))) {
            while (sc.hasNext())
                System.out.print(sc.next());
        }
    }
}
