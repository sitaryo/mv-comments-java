package com.touschfish.mvcommentsjava;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class RemoveComments {

    public static String codePath = "path to your code dir";

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            codePath = args[0];
        }
        try (Stream<Path> paths = Files.walk(Paths.get(codePath))) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    String content = String.join("\n", Files.readAllLines(file));
                    writeStringToFile(file.toFile(), doAction(content), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doAction(String content) {
        JavaParser javaParser = createJavaParser();
        return javaParser.parse(content)
                .getResult()
                .map(CompilationUnit::toString)
                .orElse("");
    }

    private static JavaParser createJavaParser() {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLexicalPreservationEnabled(true);
        parserConfiguration.setAttributeComments(false);
        return new JavaParser(parserConfiguration);
    }
}