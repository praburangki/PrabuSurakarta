package Applet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

public class Board {

    public Image img;
    int boardDimension, squareDimension, band;
    Piece pieces;
    Point[] corners;
    Point topLeft;
    final int tl = 0, tr = 1, bl = 2, br = 3;

    public Board(int boardDimension, Image img) {
        this.img = img;
        this.boardDimension = boardDimension;
        redraw(null);
    }

    void redraw(Piece pieces) {
        this.squareDimension = (this.boardDimension / 10);
        this.band = (this.boardDimension / 30);
        Graphics g = this.img.getGraphics();

        g.setColor(Color.blue);
        g.fillRect(0, 0, this.boardDimension, this.boardDimension);

        this.corners = new Point[4];
        this.corners[0] = new Point(this.squareDimension * 5 / 2, this.squareDimension * 5 / 2);
        this.corners[3] = new Point(this.corners[0].x + this.squareDimension * 5, this.corners[0].y + this.squareDimension * 5);
        this.corners[2] = new Point(this.corners[0].x, this.corners[3].y);
        this.corners[1] = new Point(this.corners[3].x, this.corners[0].y);
        this.topLeft = this.corners[0];

        g.setColor(Color.yellow);
        int i = 0;
        do {
            g.fillOval(this.corners[i].x - this.squareDimension * 2 - this.band / 2, this.corners[i].y - this.squareDimension * 2 - this.band / 2, this.squareDimension * 4 + this.band, this.squareDimension * 4 + this.band);
            i++;
        } while (i < 4);

        g.setColor(Color.blue);
        i = 0;
        do {
            g.fillOval(this.corners[i].x - this.squareDimension * 2 + this.band / 2, this.corners[i].y - this.squareDimension * 2 + this.band / 2, this.squareDimension * 4 - this.band, this.squareDimension * 4 - this.band);
            i++;
        } while (i < 4);

        g.setColor(Color.red);
        i = 0;
        do {
            g.fillOval(this.corners[i].x - this.squareDimension - this.band / 2, this.corners[i].y - this.squareDimension - this.band / 2, this.squareDimension * 2 + this.band, this.squareDimension * 2 + this.band);
            i++;
        } while (i < 4);

        g.setColor(Color.blue);
        i = 0;
        do {
            g.fillOval(this.corners[i].x - this.squareDimension + this.band / 2, this.corners[i].y - this.squareDimension + this.band / 2, this.squareDimension * 2 - this.band, this.squareDimension * 2 - this.band);
            i++;
        } while (i < 4);

        Point p = new Point(this.topLeft.x, this.topLeft.y);
        g.setColor(Color.blue);
        g.fillRect(p.x, p.y, this.squareDimension * 5, this.squareDimension * 5);

        g.setColor(Color.gray);
        p.translate(-this.band / 2, -this.band / 2);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);
        g.setColor(Color.red);
        p.translate(0, this.squareDimension);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);
        g.setColor(Color.yellow);
        p.translate(0, this.squareDimension);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);
        p.translate(0, this.squareDimension);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);
        g.setColor(Color.red);
        p.translate(0, this.squareDimension);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);
        g.setColor(Color.gray);
        p.translate(0, this.squareDimension);
        g.fillRect(p.x, p.y, this.squareDimension * 5 + this.band, this.band);

        p.move(this.topLeft.x - this.band / 2, this.topLeft.y - this.band / 2);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);
        g.setColor(Color.red);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);
        g.setColor(Color.yellow);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);
        g.setColor(Color.red);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);
        g.setColor(Color.gray);
        g.fillRect(p.x, p.y, this.band, this.squareDimension * 5 + this.band);
        p.translate(this.squareDimension, 0);

        if (pieces != null) {
            i = 0;
            do {
                int j = 0;
                do {
                    if (pieces.isBlack(i, j)) {
                        g.setColor(Color.black);
                        g.fillOval(this.topLeft.x - this.band + j * this.squareDimension, this.topLeft.y - this.band + i * this.squareDimension, this.band * 2, this.band * 2);
                        g.setColor(Color.white);
                        g.drawOval(this.topLeft.x - this.band + j * this.squareDimension, this.topLeft.y - this.band + i * this.squareDimension, this.band * 2, this.band * 2);
                    } else if (pieces.isWhite(i, j)) {
                        g.setColor(Color.white);
                        g.fillOval(this.topLeft.x - this.band + j * this.squareDimension, this.topLeft.y - this.band + i * this.squareDimension, this.band * 2, this.band * 2);
                        g.setColor(Color.black);
                        g.drawOval(this.topLeft.x - this.band + j * this.squareDimension, this.topLeft.y - this.band + i * this.squareDimension, this.band * 2, this.band * 2);
                    }
                    j++;
                } while (j < 6);
                i++;
            } while (i < 6);
        }
    }

    public void drawLegalMoves(Vector v) {
        Graphics g = this.img.getGraphics();
        Enumeration e = v.elements();
        g.setColor(Color.red);

        if (e.hasMoreElements()) {
            do {
                Move move = (Move) e.nextElement();
                g.drawOval(this.topLeft.x - this.squareDimension / 2 + this.squareDimension * move.endCol, this.topLeft.y - this.squareDimension / 2 + this.squareDimension * move.endRow, this.squareDimension, this.squareDimension);
            } while (e.hasMoreElements());
        }
    }

    public void drawSinglePiece(Point p, Color c) {
        Graphics g = this.img.getGraphics();

        g.setColor(c);
        g.fillOval(this.topLeft.x - this.band + p.x * this.squareDimension, this.topLeft.y - this.band + p.y * this.squareDimension, this.band * 2, this.band * 2);
    }

    public Point getSquare(Point p) {
        if ((p.x < this.topLeft.x - this.band / 2) || (p.y < this.topLeft.y - this.band / 2) || (p.x > this.topLeft.x + this.squareDimension * 5 + this.band / 2) ||  (p.y > this.topLeft.y + this.squareDimension * 5 + this.band / 2))
            return null;
        
        return new Point((p.x - this.topLeft.x + this.band * 3 / 2) / this.squareDimension, (p.y - this.topLeft.y + this.band * 3 / 2) / this.squareDimension);
    }
}