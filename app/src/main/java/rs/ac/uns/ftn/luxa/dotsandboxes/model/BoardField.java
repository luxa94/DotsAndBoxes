package rs.ac.uns.ftn.luxa.dotsandboxes.model;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;

public abstract class BoardField {

    protected int i;

    protected int j;

    protected Status status;

    protected List<BoardField> adjacent;

    public abstract boolean isCompleted();

    public void addAdjacent(BoardField boardField) {
        adjacent.add(boardField);
    }

    public BoardField(int i, int j) {
        adjacent = new ArrayList<>();
        this.i = i;
        this.j = j;
        status = Status.FREE;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Status getStatus() {
        return status;
    }

    public boolean setStatus(Status status) {
        if (status == Status.FREE) {
            this.status = status;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        switch (status) {
            case COMPUTERS:
                return "C";
            case PLAYERS:
                return "P";
            default:
                return "F";
        }
    }
}
