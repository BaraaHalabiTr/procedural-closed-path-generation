package com.company;

import processing.core.PApplet;
import processing.core.PVector;

public class Node {
    PVector pos, dim;
    int gCost, hCost;
    boolean walkable;
    int color;
    int number;

    Node parent;

    Node(PVector pos, PVector dim, boolean walkable) {
        this.pos = pos;
        this.dim = dim;
        this.walkable = walkable;

        this.color = 0xFFFFFF;
        this.number = 0;
    }

    void render(PApplet applet) {
        applet.fill(color);
        applet.strokeWeight(1);
        applet.stroke(0);
        applet.rect(pos.x * dim.x, pos.y * dim.y, dim.x, dim.y);
        if(!walkable) {
            applet.strokeWeight(2);
            applet.stroke(255, 0, 0);
            applet.line(pos.x * dim.x, pos.y * dim.y, pos.x * dim.x + dim.x, pos.y * dim.y + dim.y);
            applet.line(pos.x * dim.x + dim.x, pos.y * dim.y, pos.x * dim.x, pos.y * dim.y + dim.y);
        }
        if(number != 0) {
            applet.fill(0);
            applet.textSize(32);
            applet.text("" + number, pos.x * dim.x + 20, pos.y * dim.y + 35);
        }
    }

    int fCost() {
        return gCost + hCost;
    }

    @Override
    public String toString() {
        return "[" + pos.x + ", " + pos.y + "] ";
    }
}
