package com.weibo.hackathon.deadline.engine.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.weibo.hackathon.deadline.engine.model.AscIIImage;
import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Candy;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.GameString;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Size;
import com.weibo.hackathon.deadline.engine.utils.Util;

public class ShapeRender implements Render<char[][]> {
    public ShapeRender() {
        gameOver = loadAscIIImageResource("./src/main/resources/gameover.img");
        deadline = loadAscIIImageResource("./src/main/resources/deadline.img");
        success = loadAscIIImageResource("./src/main/resources/success.img");
        cancell = loadAscIIImageResource("./src/main/resources/cancell.img");
    }

    private char[][] loadAscIIImageResource(String filePath) {
        File file = new File(filePath);
        List<char[]> charList = new ArrayList<char[]>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                charList.add(tempString.toCharArray());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        char[][] shape = new char[charList.size()][];
        for (int i = 0; i < charList.size(); i++) {
            shape[i] = charList.get(i);
        }
        return shape;
    }

    @Override
    public char[][] render(GameObject obj) {
        Element element = obj.element;
        char[][] shape = null;
        if (element instanceof Block) {
            shape = renderingBlock((Block) element);
        } else if (element instanceof Candy) {
            shape = renderingCandy((Candy) element);
        } else if (element instanceof Player) {
            shape = renderingPlayer((Player) element);
        } else if (element instanceof GameString) {
            shape = renderingString((GameString) element);
        } else if (element instanceof AscIIImage) {
            shape = readeringAscIIImage((AscIIImage) element);
        } else {

        }
        return shape;
    }

    private char[][] renderingBlock(Block block) {
        Size size = block.size;
        char[][] shape = new char[size.height][size.width];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                boolean isHeightMatch = i == 0 || i == shape.length - 1;
                boolean isWidthMatch = j == 0 || j == shape[0].length - 1;
                if (isHeightMatch || isWidthMatch) {
                    shape[i][j] = '*';
                } else {
                    shape[i][j] = ' ';
                }
            }
        }
        return shape;
    }

    private char[][] renderingString(GameString gameString) {
        Size size = gameString.size;
        char[] content = gameString.content.toCharArray();
        char[][] shape = new char[size.height][size.width];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                int positon = i * j + j;
                if (positon < content.length) {
                    shape[i][j] = content[positon];
                } else {
                    shape[i][j] = ' ';
                }
            }
        }
        return shape;
    }


    private char[][] renderingCandy(Candy candy) {
        char[][] shape = new char[4][2];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                shape[i][j] = '8';
            }
        }
        return shape;
    }

    private char[][] renderingPlayer(Player player) {
        char[][] shape = new char[9][8];
        shape[0] = "   ...  ".toCharArray();
        shape[1] = "   ...  ".toCharArray();
        shape[2] = "    .   ".toCharArray();
        shape[3] = "   ...  ".toCharArray();
        shape[4] = "  . . . ".toCharArray();
        shape[5] = "    .   ".toCharArray();
        shape[6] = "    .   ".toCharArray();
        shape[7] = "   . .  ".toCharArray();
        shape[8] = "  .   . ".toCharArray();
        return shape;
    }

    private char[][] readeringAscIIImage(AscIIImage image) {
        switch ("") {
            case "deadline":
                return deadline;
            case "gameover":
                return gameOver;
            case "success":
                return success;
            case "cancell":
                return cancell;
            default:
                return null;
        }
    }

    private char[][] gameOver;
    private char[][] deadline;
    private char[][] success;
    private char[][] cancell;

    public static void main(String[] args) {
        Util.printArray(new ShapeRender().gameOver);
    }
}
