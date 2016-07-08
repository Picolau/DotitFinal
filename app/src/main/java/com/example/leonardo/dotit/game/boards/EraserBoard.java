package com.example.leonardo.dotit.game.boards;

import com.example.leonardo.dotit.game.Connection;
import com.example.leonardo.dotit.game.Dot;

import java.util.ArrayList;

/**
 * Created by Leonardo on 21/06/2016.
 */
public class EraserBoard extends Board {

    public static final String[][] levels = { {"01", "300100001011012222122201120211112" }, // Z
            {"02", "31222212201110001001010110212110220112021" }, // Z ligado com 2 traços
            {"03", "3010202121020211001122021" }, // T
            {"04", "3112011211221122221222021102010111112" }, // F
            {"05", "300100001101110202021122221220102021211211112011101102112" }, // F BUGADO
            {"06", "31121010200010111" }, // A
            {"07", "312211222202121221120111202120001010200101020" }, // W24
            {"08", "30110000100101221122221221020112002110212" }, // W38
            {"09", "301020001001011120210" }, // W19
            {"10", "31112011111022011112110112122122200010010"}, //A DAORINHA QUE EU FIZ
            {"11", "3112010111020122111020111100100100001010212222122" }, // W16
            {"12", "3000100100212010221221222202112211020211012010110" }, // W4
            {"13", "30010000101111222101121122122"}, // W51
            {"14", "302121021221121221011000112000102" }, // W47
            {"15", "3011111120212010200010010202111202211212210111121" }, // W49
            {"16", "31222212210012112201100010010" }, // W48
            {"17", "310011112112121221222" }, // W54
            {"18", "31011011111121121" }, // W53
            {"19", "300100111111202120102" },  // W20 - BEE 1
            {"20", "31020000100101011"}, // W26 - BEE 2
            {"21", "3102000120001010202122021212210220010110022111222"}, // W41 - BEE 3
            {"22", "3112101020212122211120111"}, // W41 - BEE 3
            {"23", "310202021001010110001"}, // W41 - BEE 3
            {"24", "311121121011102121222"}, // W41 - BEE 3
            {"25", "31020001010012110"}, // W41 - BEE 3
            {"26", "31120110022111102"}, // W41 - BEE 3
            {"27", "3110011210211"}, // W41 - BEE 3
            {"28", "300110111021222112201102011212100"}, // W41 - BEE 3
            {"29", "301111020202100010102112112222122"}, // W41 - BEE 3
            {"30", "312000212000101021020202121221022101111121222"}, // W41 - BEE 3
            {"31", "3112010110110211211120211"},
            {"32", "300101011202112221121011101021112" } }; // W25

    public EraserBoard(int level, int screenWidth, int screenHeight) {
        super(levels[level], screenWidth, screenHeight);
        this.level = level;

    }

    @Override
    public void connect(ArrayList<Dot> dotsToConnect) {
        for (int i = 0; i + 1 < dotsToConnect.size(); i++) {
            Dot dot1 = dotsToConnect.get(i);
            Dot dot2 = dotsToConnect.get(i + 1);

            ArrayList<Connection> intersectConnections = new ArrayList<>();

            for (Connection c : connectionsPlayer) {
                if (intersect(dot1,dot2,c.dot1,c.dot2)) {
                    intersectConnections.add(c);
                }
            }

            if (intersectConnections.size() != 0) { // CONNECTION ALREADY EXISTS
                /*dot1.unuse();
                dot2.unuse();*/
                for (Connection c : intersectConnections) {
                    this.connectionsPlayer.remove(c);
                }
            } else {
                int strokeWidth = (int) ((LINE_WIDTH_PER_SCREEN_SIZE * (screenWidth + screenHeight)) /
                        Math.pow(DOTS_AND_LINES_RATIO_DIVISOR, dots.length - 3));
                Connection c = new Connection(dot1, dot2, strokeWidth);
                /*dot1.setUsed();
                dot2.setUsed();*/
                connectionsPlayer.add(c);
            }
        }
    }

    @Override
    public void setXYLine(int x, int y) {
        this.xLine = x;
        this.yLine = y;
        boolean found = false;
        Dot newLastSelectedDot = null;

        for (int i = 0; i < dots.length && !found; i++){
            for (int j = 0; j < dots.length && !found; j++) {
                Dot d = dots[i][j];

                if (xLine <= d.getX() + (2*d.getSize()) && xLine >= d.getX() - (2*d.getSize()) &&
                        yLine <= d.getY() + (2*d.getSize()) && yLine >= d.getY() - (2*d.getSize())) {
                    newLastSelectedDot = d;
                    found = true;
                }
            }
        }

        if (newLastSelectedDot != null) {
            if (lastSelectedDot != null && lastSelectedDot != newLastSelectedDot) {
                this.connect(lastSelectedDot, newLastSelectedDot);
            }
            lastSelectedDot = newLastSelectedDot;
        }
    }

    public static int orientation(Dot p, Dot q, Dot r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0.0)
            return 0;
        return (val > 0) ? 1 : 2;
    }

    public static boolean intersect(Dot p1, Dot q1, Dot p2, Dot q2) {
        if ((p1 == p2 && q1 == q2) || (p1 == q2 && q1 == p2)) {
            return true;
        } else if ((p1 == p2 && q1 != q2) || (p1 == q2 && q1 != p2) || (q1 == q2 && p1 != p2) || (q1 == p2 && p1 != q2)) {
            return false;
        }

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        return false;
    }

    @Override
    public String[] getCurrentLevel() {
        if (level < levels.length) {
            return levels[level];
        } else {
            return new String[2];
        }
    }
}
