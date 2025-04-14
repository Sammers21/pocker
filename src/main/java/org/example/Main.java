package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    enum CardColor {
        WHITE,
        RED,
        BLACK,
        ANY;

        public static CardColor pix(Color pix) {
            if (pix.getRed() >= 120 && pix.getGreen() >= 120
                    && pix.getBlue() >= 120) {
                return WHITE;
            } else if (pix.getRed() >= 190 && pix.getGreen() < 100
                    && pix.getBlue() < 100) {
                return RED;
            } else if (pix.getRed() < 100 && pix.getGreen() < 100
                    && pix.getBlue() < 100) {
                return BLACK;
            } else {
                return ANY;
            }
        }
    }

    record Coordinate(int x, int y) {
    }

    record AreaRecognizer(String symbol, Coordinate[] coordinates, CardColor color) {
        public boolean recognize(BufferedImage img) {
            for (Coordinate coordinate : coordinates) {
                int x = coordinate.x;
                int y = coordinate.y;
                if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) {
                    System.out.println("Coordinate out of bounds: x=" + x + ", y=" + y + " for symbol: " + symbol);
                    return false;
                }
                Color pix = new Color(img.getRGB(x, y));
                System.out.println("Pixel color: " + pix + " for symbol=" + symbol + " at coordinate: " + coordinate);
                var p = CardColor.pix(pix);
                if (p != color) {
                    System.out.println("Failed to recognize symbol: " + symbol + " at coordinate: " + coordinate);
                    return false;
                }
            }
            return true;
        }
    }

    static int CARD_WIDTH = 63, CARD_HEIGHT = 87, X_OFFSET = 7, FIRST_CARD_X = 143, FIRST_CARD_Y = 586;
    static int SUIT_X = 26, SUIT_Y = 48, SUIT_WIDTH = 32, SUIT_HEIGHT = 32;
    static int RANK_X = 26, RANK_Y = 48, RANK_WIDTH = 32, RANK_HEIGHT = 32;

    static AreaRecognizer VALID_CARD = new AreaRecognizer("CARD",
            new Coordinate[] { new Coordinate(48, 13), }, CardColor.WHITE);

    static AreaRecognizer[] CARD_NUMBERS = {
            new AreaRecognizer("2", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("3", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("4", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("5", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("6", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("7", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("8", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("9", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("10", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("J", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("Q", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("K", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
            new AreaRecognizer("A", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.ANY),
    };

    static AreaRecognizer[] CARD_SUITS = {
            new AreaRecognizer("h", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.RED),
            new AreaRecognizer("d", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.RED),
            new AreaRecognizer("c", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.BLACK),
            new AreaRecognizer("s", new Coordinate[] { new Coordinate(0, 0), new Coordinate(0, 1) }, CardColor.BLACK)
    };

    record Card(BufferedImage image) {
        public String recognizeName() throws IOException {
            var suit = recognizeSuit();
            var rank = recognizeRank();
            return rank + suit;
        }

        public Boolean valid() {
            return VALID_CARD.recognize(image);
        }

        private String recognizeSuit() throws IOException {
            var suit = image.getSubimage(SUIT_X, SUIT_Y, SUIT_WIDTH, SUIT_HEIGHT);
            var normalized = normalize(suit, CardColor.WHITE);
            for (var cardSuit : CARD_SUITS) {
                if (cardSuit.recognize(normalized)) {
                    return cardSuit.symbol;
                }
            }
            return "N";
        }

        private String recognizeRank() {
            var rank = image.getSubimage(RANK_X, RANK_Y, RANK_WIDTH, RANK_HEIGHT);
            var normalized = normalize(rank, CardColor.WHITE);
            for (var cardNumber : CARD_NUMBERS) {
                if (cardNumber.recognize(normalized)) {
                    return cardNumber.symbol;
                }
            }
            return "N";
        }
    }

    public static void main(String[] args) throws IOException {
        File twoC3dAh = new File("./imgs_marked/2c3dAh.png");
        System.out.println("Recognizing text from image: " + recognizeText(twoC3dAh));
    }

    public static String recognizeText(File imageFile) throws IOException {
        var img = ImageIO.read(imageFile);
        var cards = splitIntoCards(img);
        if (cards.size() == 0) {
            System.out.println("No cards found in image.");
            return "";
        } else {
            System.out.println("Found " + cards.size() + " cards in image.");
        }
        var res = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            var name = card.recognizeName();
            System.out.println("Recognizing name from card: " + name);
            res.append(name);
            if (i != cards.size() - 1) {
                res.append("");
            }
        }
        return res.toString();
    }

    public static BufferedImage normalize(BufferedImage image, CardColor color) {
        var top = normalize0(image, color, 0, -1);
        var bottom = normalize0(top, color, 0, 1);
        var left = normalize0(bottom, color, -1, 0);
        var right = normalize0(left, color, 1, 0);
        return right;
    }

    // Removes the full lines of color from the image in the given direction
    public static BufferedImage normalize0(BufferedImage image, CardColor color, int dx, int dy) {
        return image;
    }

    public static List<Card> splitIntoCards(BufferedImage img) throws IOException {
        var res = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            int cardNum = i + 1;
            int x = FIRST_CARD_X + i * (CARD_WIDTH + X_OFFSET);
            int y = FIRST_CARD_Y;
            System.out.println("Splitting card #" + (i + 1) + " at coordinates: x=" + x + ", y=" + y);
            BufferedImage cardImg = img.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
            var card = new Card(cardImg);
            if (card.valid()) {
                saveImg(cardImg, "card_" + cardNum + ".png");
                System.out.println("Valid card: #" + cardNum);
                cardImg = normalize(cardImg, CardColor.BLACK);
                cardImg = normalize(cardImg, CardColor.WHITE);
                res.add(new Card(cardImg));
            } else {
                System.out.println("Invalid card: #" + cardNum);
            }
        }
        return res;
    }

    public static void saveImg(BufferedImage img, String name) throws IOException {
        File outputfile = new File(name);
        ImageIO.write(img, "png", outputfile);
    }
}
