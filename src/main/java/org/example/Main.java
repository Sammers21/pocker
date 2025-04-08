package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// img descri
public class Main {

    record Coordinate(int x, int y) {
    }

    static int CARD_WIDTH = 63, CARD_HEIGHT = 87, X_OFFSET = 7, FIRST_CARD_X = 143, FIRST_CARD_Y = 586;
    static int SUIT_X = 26, SUIT_Y = 48, SUIT_WIDTH = 32, SUIT_HEIGHT = 32;

    record Card(Coordinate upperLeft, BufferedImage image) {
        public String recognizeName() throws IOException {
            var suit = recognizeSuit();
            var rank = recognizeRank();
            if (suit == null || rank == null) {
                System.out.println("Error recognizing card: " + this);
                return "unknown";
            }
            return rank + suit;
        }

        private String recognizeSuit() throws IOException {
            int suitX = upperLeft.x + SUIT_X;
            int suitY = upperLeft.y + SUIT_Y;
            var suit = image.getSubimage(suitX, suitY, SUIT_WIDTH, SUIT_HEIGHT);
            ImageIO.write(suit, "png", new File(this.upperLeft.x + "_suit.png"));
            int centerX = SUIT_WIDTH / 2;
            int centerY = SUIT_HEIGHT / 2;
            var suitColor = suit.getRGB(centerX, centerY);
            var color = new Color(suitColor);
            var red = color.getRed();
            var green = color.getGreen();
            var blue = color.getBlue();
//            if red > 90 {
//                // its red: diamonds or hearts
//
//            } else {
//                // its black: clubs or spades
//
//            }
            System.out.println("Suit color: " + suitColor + " red: " + red + ", green: " + green + ", blue: " + blue);
            return null;
        }

        private String recognizeRank() {
            return null;
        }

        public void saveTo(String path) {
            try {
                System.out.println("Getting subimage: of coordinates: x" + upperLeft.x + ", y " + upperLeft.y);
                var img = image.getSubimage(upperLeft.x, upperLeft.y, CARD_WIDTH, CARD_HEIGHT);
                ImageIO.write(img, "png", new File(path));
            } catch (Exception e) {
                System.out.println("Error saving image: " + this);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File twoC3dAh = new File("./imgs_marked/10cKhKd7h.png");
        System.out.println("Recognizing text from image: " + recognizeText(twoC3dAh));
    }

    public static String recognizeText(File imageFile) throws IOException {
        var img = ImageIO.read(imageFile);
        var cards = splitIntoCards(img);
        var res = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            var name = card.recognizeName();
            card.saveTo("./card_" + (i + 1)+ ".png");
            System.out.println("Recognizing name from card: " + name);
            res.append(name);
            if (i != cards.size() - 1) {
                res.append(", ");
            }
        }
        return res.toString();
    }

    public static List<Card> splitIntoCards(BufferedImage img) {
        var res = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            int x = FIRST_CARD_X + i * (CARD_WIDTH + X_OFFSET);
            int y = FIRST_CARD_Y;
            var card = new Card(new Coordinate(x, y), img);
            res.add(card);
        }
        return res;
    }
}
