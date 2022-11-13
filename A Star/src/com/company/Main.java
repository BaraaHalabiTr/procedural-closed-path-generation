package com.company;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.*;

public class Main extends PApplet {
    Node[][] grid;

    final int s = 10;

    public static void main(String[] args) {
        PApplet.main("com.company.Main", args);
    }

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        reset();
    }

    public void draw() {
        background(255);
        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                grid[i][j].render(this);
    }

    void reset() {
        grid = new Node[s][s];
        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                grid[i][j] = new Node(new PVector(j, i), new PVector(width/s, height/s), true);
        generateTrack(40);
    }

    void generateTrack(int minLength) {
        List<PVector> points = new ArrayList<>();
        PVector p1 = new PVector((int)random(s), (int)random(s)), p2;
        grid[(int)p1.x][(int)p1.y].color = color(128);

        int length = 0;
        boolean shouldReset = false;

        points.add(p1);

        for(int i = 0; i < s / 3; i ++) {
            do {
                p2 = new PVector((int)random(s), (int)random(s));
            } while(points.contains(p2) || !grid[(int)p2.x][(int)p2.y].walkable);

            points.add(p2);
            grid[(int)p2.x][(int)p2.y].color = color(66, 245, 197);
            grid[(int)p2.x][(int)p2.y].number = i + 1;

            List<Node> path = findPath(p1, p2);
            length += path.size();
            if(path.size() == 0) shouldReset = true;
            setNonWalkable(path);

            p1 = p2;
        }
        grid[(int)points.get(0).x][(int)points.get(0).y].walkable = true;
        List<Node> path = findPath(p1, points.get(0));
        length += path.size();
        if(path.size() == 0) shouldReset = true;
        setNonWalkable(path);
        if(length < minLength) shouldReset = true;

        if(shouldReset) reset();
    }

    List<Node> getNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();
        if (node.pos.x + 1 < s) neighbours.add(grid[(int) node.pos.y][(int) node.pos.x + 1]);
        if (node.pos.y + 1 < s) neighbours.add(grid[(int) node.pos.y + 1][(int) node.pos.x]);
        if (node.pos.x - 1 >= 0) neighbours.add(grid[(int) node.pos.y][(int) node.pos.x - 1]);
        if (node.pos.y - 1 >= 0) neighbours.add(grid[(int) node.pos.y - 1][(int) node.pos.x]);
        return neighbours;
    }

    int getDistance(Node a, Node b) {
        return (int) abs(a.pos.x - b.pos.x) + (int) abs(a.pos.y - b.pos.y);
    }

    List<Node> findPath(PVector start, PVector end) {
        Node startNode = grid[(int) start.x][(int) start.y];
        Node endNode = grid[(int) end.x][(int) end.y];

        List<Node> openSet = new ArrayList<>();
        HashSet<Node> closedSet = new HashSet<>();
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++)
                if (openSet.get(i).fCost() < currentNode.fCost() || openSet.get(i).fCost() == currentNode.fCost() && openSet.get(i).hCost < currentNode.hCost)
                    currentNode = openSet.get(i);

            openSet.remove(currentNode);
            closedSet.add(currentNode);

            if (currentNode == endNode) return retracePath(startNode, endNode);

            for (Node neighbour : getNeighbours(currentNode)) {
                if (!neighbour.walkable || closedSet.contains(neighbour)) continue;
                int movementCost = currentNode.gCost + getDistance(currentNode, neighbour);
                if (movementCost < neighbour.gCost || !openSet.contains(neighbour)) {
                    neighbour.gCost = movementCost;
                    neighbour.hCost = getDistance(currentNode, endNode);
                    neighbour.parent = currentNode;

                    if (!openSet.contains(neighbour)) openSet.add(neighbour);
                }
            }
        }
        return new ArrayList<>();
    }

    void setNonWalkable(List<Node> path) {
        for(Node n : path) n.walkable = false;
    }

    List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        path.add(currentNode);
        return path;
    }

    public void keyPressed() {
        reset();
    }
}