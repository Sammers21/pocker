package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        for (var p : Files.list(Paths.get(args[0])).filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".png")).toList()) System.out.println(p.getFileName() + " - " + recognizeText(p.toFile()));
    }

    enum CardColor {
        WHITE, RED, BLACK, ANY;

        public static CardColor pix(Color pix) {
            if (pix.getRed() >= 90 && (pix.getRed() - pix.getGreen()) > 40 && (pix.getRed() - pix.getBlue()) > 40)
                return RED;
            else if (pix.getRed() >= 250 && pix.getGreen() >= 250 && pix.getBlue() >= 250
                    || (pix.getRed() == 120 && pix.getGreen() == 120 && pix.getBlue() == 120))
                return WHITE;
            else if (Math.abs(pix.getRed() - pix.getGreen()) <= 20 && Math.abs(pix.getRed() - pix.getBlue()) <= 20
                    && Math.abs(pix.getBlue() - pix.getGreen()) <= 20 && pix.getRed() < 170)
                return BLACK;
            return ANY;
        }
    }

    record XYNColors(int x, int y, Set<CardColor> colors) {
    }

    record AreaRecognizer(String symbol, XYNColors[] coordinates) {
        public boolean recognize(BufferedImage img) {
            for (XYNColors c : coordinates)
                if (c.x < 0 || c.y < 0 || c.x >= img.getWidth() || c.y >= img.getHeight()
                        || !c.colors.contains(CardColor.pix(new Color(img.getRGB(c.x, c.y)))))
                    return false;
            return true;
        }
    }

    static int CARD_WIDTH = 63, CARD_HEIGHT = 87, X_OFFSET = 8, FIRST_CARD_X = 143, FIRST_CARD_Y = 586, SUIT_X = 26,
            SUIT_Y = 48, SUIT_WIDTH = 32, SUIT_HEIGHT = 32, RANK_X = 9, RANK_Y = 6, RANK_WIDTH = 24, RANK_HEIGHT = 24;
    static Set<CardColor> BLACK_N_RED = Set.of(CardColor.BLACK, CardColor.RED);
    static AreaRecognizer VALID_CARD = new AreaRecognizer("CARD",
            new XYNColors[] { new XYNColors(48, 13, Set.of(CardColor.WHITE)) });

    static AreaRecognizer[] CARD_NUMBERS = {
            new AreaRecognizer("2",
                    new XYNColors[] { new XYNColors(3, 20, BLACK_N_RED), new XYNColors(13, 20, BLACK_N_RED),
                            new XYNColors(3, 7, Set.of(CardColor.WHITE)),
                            new XYNColors(14, 0, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("3",
                    new XYNColors[] { new XYNColors(3, 2, BLACK_N_RED), new XYNColors(7, 10, BLACK_N_RED),
                            new XYNColors(13, 2, BLACK_N_RED), new XYNColors(14, 16, BLACK_N_RED),
                            new XYNColors(2, 7, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("4",
                    new XYNColors[] { new XYNColors(3, 16, BLACK_N_RED), new XYNColors(7, 7, BLACK_N_RED),
                            new XYNColors(13, 21, BLACK_N_RED), new XYNColors(13, 3, BLACK_N_RED),
                            new XYNColors(13, 7, BLACK_N_RED) }),
            new AreaRecognizer("5",
                    new XYNColors[] { new XYNColors(3, 2, BLACK_N_RED), new XYNColors(7, 10, BLACK_N_RED),
                            new XYNColors(13, 2, BLACK_N_RED), new XYNColors(14, 16, BLACK_N_RED),
                            new XYNColors(3, 6, BLACK_N_RED), new XYNColors(2, 15, Set.of(CardColor.WHITE)),
                            new XYNColors(13, 6, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("6",
                    new XYNColors[] { new XYNColors(4, 5, BLACK_N_RED), new XYNColors(10, 2, BLACK_N_RED),
                            new XYNColors(14, 15, BLACK_N_RED), new XYNColors(9, 10, BLACK_N_RED),
                            new XYNColors(14, 7, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("7",
                    new XYNColors[] { new XYNColors(2, 2, BLACK_N_RED), new XYNColors(14, 2, BLACK_N_RED),
                            new XYNColors(5, 21, BLACK_N_RED), new XYNColors(2, 7, Set.of(CardColor.WHITE)),
                            new XYNColors(2, 16, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("8",
                    new XYNColors[] { new XYNColors(8, 2, BLACK_N_RED), new XYNColors(3, 19, BLACK_N_RED),
                            new XYNColors(12, 19, BLACK_N_RED), new XYNColors(2, 7, BLACK_N_RED),
                            new XYNColors(2, 15, BLACK_N_RED), new XYNColors(0, 10, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("9",
                    new XYNColors[] { new XYNColors(3, 15, Set.of(CardColor.WHITE)), new XYNColors(2, 8, BLACK_N_RED),
                            new XYNColors(8, 12, BLACK_N_RED), new XYNColors(7, 21, BLACK_N_RED) }),
            new AreaRecognizer("10",
                    new XYNColors[] { new XYNColors(3, 2, BLACK_N_RED), new XYNColors(14, 1, BLACK_N_RED),
                            new XYNColors(15, 20, BLACK_N_RED), new XYNColors(3, 20, BLACK_N_RED),
                            new XYNColors(13, 14, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("J",
                    new XYNColors[] { new XYNColors(11, 3, BLACK_N_RED), new XYNColors(11, 17, BLACK_N_RED),
                            new XYNColors(6, 21, BLACK_N_RED), new XYNColors(3, 6, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("Q",
                    new XYNColors[] { new XYNColors(20, 20, BLACK_N_RED), new XYNColors(14, 15, BLACK_N_RED) }),
            new AreaRecognizer("K",
                    new XYNColors[] { new XYNColors(2, 1, BLACK_N_RED), new XYNColors(6, 13, BLACK_N_RED),
                            new XYNColors(12, 13, BLACK_N_RED), new XYNColors(16, 2, BLACK_N_RED) }),
            new AreaRecognizer("A", new XYNColors[] { new XYNColors(8, 15, BLACK_N_RED) })
    };

    static AreaRecognizer[] CARD_SUITS = {
            new AreaRecognizer("h",
                    new XYNColors[] { new XYNColors(16, 19, Set.of(CardColor.RED)),
                            new XYNColors(16, 3, Set.of(CardColor.WHITE)) }),
            new AreaRecognizer("d", new XYNColors[] { new XYNColors(15, 2, Set.of(CardColor.RED)) }),
            new AreaRecognizer("c", new XYNColors[] { new XYNColors(12, 2, Set.of(CardColor.BLACK)) }),
            new AreaRecognizer("s", new XYNColors[] { new XYNColors(9, 12, Set.of(CardColor.BLACK)) })
    };

    record Card(BufferedImage image) {
        public String recognizeName() {
            return recognizeRank() + recognizeSuit();
        }

        public Boolean valid() {
            return VALID_CARD.recognize(image);
        }

        private String recognizeSuit() {
            var suit = image.getSubimage(SUIT_X, SUIT_Y, SUIT_WIDTH, SUIT_HEIGHT);
            for (var cardSuit : CARD_SUITS)
                if (cardSuit.recognize(suit))
                    return cardSuit.symbol;
            return "n";
        }

        private String recognizeRank() {
            var rank = image.getSubimage(RANK_X, RANK_Y, RANK_WIDTH, RANK_HEIGHT);
            for (var cardNumber : CARD_NUMBERS)
                if (cardNumber.recognize(rank))
                    return cardNumber.symbol;
            return "N";
        }
    }

    public static String recognizeText(File imageFile) throws IOException {
        return splitIntoCards(ImageIO.read(imageFile)).stream().map(card -> card.recognizeName()).reduce("",
                String::concat);
    }

    public static List<Card> splitIntoCards(BufferedImage img) throws IOException {
        var res = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            int x = FIRST_CARD_X + i * (CARD_WIDTH + X_OFFSET), y = FIRST_CARD_Y;
            BufferedImage cardImg = img.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
            if (i > 0) {
                int iterations = 0, dColor;
                do {
                    dColor = CardColor.pix(new Color(cardImg.getRGB(0, CARD_HEIGHT / 2))).ordinal();
                    if (dColor == CardColor.BLACK.ordinal()) {
                        x++;
                        iterations++;
                        cardImg = img.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                    }
                } while (dColor == CardColor.BLACK.ordinal() && iterations < 20);
            }
            var card = new Card(cardImg);
            if (card.valid())
                res.add(card);
        }
        return res;
    }
}