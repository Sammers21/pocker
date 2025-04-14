package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    enum CardColor {
        WHITE, RED, BLACK, ANY;

        public static CardColor pix(Color pix) {
            int blackDiff = 20;
            if (pix.getRed() >= 90 && (pix.getRed() - pix.getGreen()) > 40
                    && (pix.getRed() - pix.getBlue()) > 40) {
                return RED;
            } else if (pix.getRed() >= 250 && pix.getGreen() >= 250
                    && pix.getBlue() >= 250 || (pix.getRed() == 120 && pix.getGreen() == 120 && pix.getBlue() == 120)) {
                return WHITE;
            } else if (Math.abs(pix.getRed() - pix.getGreen()) <= blackDiff
                    && Math.abs(pix.getRed() - pix.getBlue()) <= blackDiff
                    && Math.abs(pix.getRed() - pix.getBlue()) <= blackDiff && pix.getRed() < 170) {
                return BLACK;
            } else {
                return ANY;
            }
        }
    }

    record XYNColors(int x, int y, Set<CardColor> colors) {
    }

    record AreaRecognizer(String symbol, XYNColors[] coordinates) {
        public boolean recognize(BufferedImage img) throws IOException {
            for (XYNColors coordinate : coordinates) {
                int x = coordinate.x;
                int y = coordinate.y;
                if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) {
                    return false;
                }
                Color pix = new Color(img.getRGB(x, y));
                var p = CardColor.pix(pix);
                if (!coordinate.colors.contains(p)) {
                    return false;
                }
            }
            return true;
        }
    }

    static int CARD_WIDTH = 63, CARD_HEIGHT = 87, X_OFFSET = 8, FIRST_CARD_X = 143, FIRST_CARD_Y = 586;
    static int SUIT_X = 26, SUIT_Y = 48, SUIT_WIDTH = 32, SUIT_HEIGHT = 32;
    static int RANK_X = 9, RANK_Y = 6, RANK_WIDTH = 24, RANK_HEIGHT = 24;
    static Set<CardColor> BLACK_N_RED = Set.of(CardColor.BLACK, CardColor.RED);

    static AreaRecognizer VALID_CARD = new AreaRecognizer("CARD",
            new XYNColors[] { new XYNColors(48, 13, Set.of(CardColor.WHITE)), });

    static AreaRecognizer[] CARD_NUMBERS = {
            new AreaRecognizer("2",
                    new XYNColors[] {
                            new XYNColors(3, 20, BLACK_N_RED),
                            new XYNColors(13, 20, BLACK_N_RED),
                            new XYNColors(3, 7, Set.of(CardColor.WHITE)),
                            new XYNColors(14, 0, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("3",
                    new XYNColors[] {
                            new XYNColors(3, 2, BLACK_N_RED),
                            new XYNColors(7, 10, BLACK_N_RED),
                            new XYNColors(13, 2, BLACK_N_RED),
                            new XYNColors(14, 16, BLACK_N_RED),
                            new XYNColors(2, 7, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("4",
                    new XYNColors[] {
                            new XYNColors(3, 16, BLACK_N_RED),
                            new XYNColors(7, 7, BLACK_N_RED),
                            new XYNColors(13, 21, BLACK_N_RED),
                            new XYNColors(13, 3, BLACK_N_RED),
                            new XYNColors(13, 7, BLACK_N_RED),
                    }),
            new AreaRecognizer("5",
                    new XYNColors[] {
                            new XYNColors(3, 2, BLACK_N_RED),
                            new XYNColors(7, 10, BLACK_N_RED),
                            new XYNColors(13, 2, BLACK_N_RED),
                            new XYNColors(14, 16, BLACK_N_RED),
                            new XYNColors(3, 6, BLACK_N_RED),
                            new XYNColors(2, 15, Set.of(CardColor.WHITE)),
                            new XYNColors(13, 6, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("6",
                    new XYNColors[] {
                            new XYNColors(4, 5, BLACK_N_RED),
                            new XYNColors(10, 2, BLACK_N_RED),
                            new XYNColors(14, 15, BLACK_N_RED),
                            new XYNColors(9, 10, BLACK_N_RED),
                            new XYNColors(14, 7, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("7",
                    new XYNColors[] {
                            new XYNColors(2, 2, BLACK_N_RED),
                            new XYNColors(14, 2, BLACK_N_RED),
                            new XYNColors(5, 21, BLACK_N_RED),
                            new XYNColors(2, 7, Set.of(CardColor.WHITE)),
                            new XYNColors(2, 16, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("8",
                    new XYNColors[] {
                            new XYNColors(8, 2, BLACK_N_RED),
                            new XYNColors(3, 19, BLACK_N_RED),
                            new XYNColors(12, 19, BLACK_N_RED),
                            new XYNColors(2, 7, BLACK_N_RED),
                            new XYNColors(2, 15, BLACK_N_RED),
                            new XYNColors(0, 10, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("9",
                    new XYNColors[] {
                            new XYNColors(3, 15, Set.of(CardColor.WHITE)),
                            new XYNColors(2, 8, BLACK_N_RED),
                            new XYNColors(8, 12, BLACK_N_RED),
                            new XYNColors(7, 21, BLACK_N_RED),
                    }),
            new AreaRecognizer("10",
                    new XYNColors[] {
                            new XYNColors(3, 2, BLACK_N_RED),
                            new XYNColors(14, 1, BLACK_N_RED),
                            new XYNColors(15, 20, BLACK_N_RED),
                            new XYNColors(3, 20, BLACK_N_RED),
                            new XYNColors(13, 14, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("J",
                    new XYNColors[] {
                            new XYNColors(11, 3, BLACK_N_RED),
                            new XYNColors(11, 17, BLACK_N_RED),
                            new XYNColors(6, 21, BLACK_N_RED),
                            new XYNColors(3, 6, Set.of(CardColor.WHITE)),
                    }),
            new AreaRecognizer("Q",
                    new XYNColors[] {
                            new XYNColors(20, 20, BLACK_N_RED),
                            new XYNColors(14, 15, BLACK_N_RED),
                    }),
            new AreaRecognizer("K",
                    new XYNColors[] {
                            new XYNColors(2, 1, BLACK_N_RED),
                            new XYNColors(6, 13, BLACK_N_RED),
                            new XYNColors(12, 13, BLACK_N_RED),
                            new XYNColors(16, 2, BLACK_N_RED),
                    }),
            new AreaRecognizer("A",
                    new XYNColors[] {
                            new XYNColors(8, 15, BLACK_N_RED),
                    }),
    };

    public static void main(String[] args) throws IOException {
        File f = new File("./imgs_marked/4sJdKdKc.png");
        System.out.println("Recognizing text from image: " + recognizeText(f));
    }

    static AreaRecognizer[] CARD_SUITS = {
            new AreaRecognizer("h",
                    new XYNColors[] {
                            new XYNColors(16, 19, Set.of(CardColor.RED)),
                            new XYNColors(16, 3, Set.of(CardColor.WHITE))
                    }),
            new AreaRecognizer("d", new XYNColors[] { new XYNColors(15, 2, Set.of(CardColor.RED)), }),
            new AreaRecognizer("c", new XYNColors[] { new XYNColors(12, 2, Set.of(CardColor.BLACK)), }),
            new AreaRecognizer("s", new XYNColors[] { new XYNColors(9, 12, Set.of(CardColor.BLACK)), }),
    };

    record Card(BufferedImage image) {
        public String recognizeName() throws IOException {
            var suit = recognizeSuit();
            var rank = recognizeRank();
            return rank + suit;
        }

        public Boolean valid() throws IOException {
            return VALID_CARD.recognize(image);
        }

        private String recognizeSuit() throws IOException {
            var suit = image.getSubimage(SUIT_X, SUIT_Y, SUIT_WIDTH, SUIT_HEIGHT);
            for (var cardSuit : CARD_SUITS) {
                if (cardSuit.recognize(suit)) {
                    return cardSuit.symbol;
                }
            }
            return "n";
        }

        private String recognizeRank() throws IOException {
            var rank = image.getSubimage(RANK_X, RANK_Y, RANK_WIDTH, RANK_HEIGHT);
            for (var cardNumber : CARD_NUMBERS) {
                if (cardNumber.recognize(rank)) {
                    return cardNumber.symbol;
                }
            }
            return "N";
        }
    }

    public static String recognizeText(File imageFile) throws IOException {
        var img = ImageIO.read(imageFile);
        var cards = splitIntoCards(img);
        var res = new StringBuilder("");
        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            var name = card.recognizeName();
            res.append(name);
            if (i != cards.size() - 1) {
                res.append("");
            }
        }
        return res.toString();
    }

    public static List<Card> splitIntoCards(BufferedImage img) throws IOException {
        var res = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            int x = FIRST_CARD_X + i * (CARD_WIDTH + X_OFFSET);
            int y = FIRST_CARD_Y;
            BufferedImage cardImg = img.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
            if (i > 0) {
                int iterations = 0;
                Color p;
                CardColor dColor;
                do {
                    p = new Color(cardImg.getRGB(0, CARD_HEIGHT / 2));
                    dColor = CardColor.pix(p);
                    if (dColor == CardColor.BLACK) {
                        x++;
                        iterations++;
                        cardImg = img.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                    }
                } while (dColor == CardColor.BLACK && iterations < 20);
            }
            var card = new Card(cardImg);
            if (card.valid()) {
                res.add(card);
            }
        }
        return res;
    }
}
