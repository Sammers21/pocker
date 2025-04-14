package org.example;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class CardTests {

    @Test
    public void firstCardIsCorrect() throws IOException {
        testCardCorrection(this::testFirstCardCorrect);
    }

    @Test
    public void allCardsAreCorrect() throws IOException {
        testCardCorrection(this::testAllCardsCorrect);
    }

    private void testCardCorrection(Function<File, Boolean> f) throws IOException {
        Path directory = Paths.get("imgs_marked");
        List<File> imgs = new ArrayList<>();
        Files.list(directory)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(path -> {
                    imgs.add(path.toFile());
                });
        assertTrue(imgs.size() > 0, "Empty image set");
        List<Boolean> testReuslts = imgs.stream().map(f).toList();
        int falses = 0;
        for (Boolean testReuslt : testReuslts) {
            if (!testReuslt) {
                falses++;
            }
        }
        assertTrue(falses == 0, "There are '" + falses + "' tests with wrong answer and '"
                + (testReuslts.size() - falses) + "' with right answer");
    }

    public Boolean testAllCardsCorrect(File file) {
        String name = file.getName();
        System.out.println("Testing file: " + name);
        String cards = name.split("\\.")[0];
        String recognizedName = "";
        try {
            recognizedName = Main.recognizeText(file);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Failed with testing file " + name);
        }
        boolean equals = recognizedName.equals(cards);
        if (!equals) {
            System.out.println("Expected: " + cards + ", but got: " + recognizedName + " for file: " + name);
        }
        return equals;
    }

    public Boolean testFirstCardCorrect(File file) {
        String name = file.getName();
        int end = 2;
        if (name.startsWith("1")) {
            end = 3;
        }
        String firstCard = name.substring(0, end);
        String recognizedName = "";
        try {
            recognizedName = Main.recognizeText(file).substring(0, end);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Failed with testing file " + name);
        }
        boolean equals = recognizedName.equals(firstCard);
        if (!equals) {
            System.out.println("Expected: " + firstCard + ", but got: " + recognizedName + " for file: " + name);
        }
        return equals;
    }
}