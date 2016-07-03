package rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl;

import rs.ac.uns.ftn.luxa.dotsandboxes.model.BoardField;

public class Dot extends BoardField {

    @Override
    public boolean isCompleted() {
        return true;
    }

    public Dot(int i, int j) {
        super(i, j);
    }

    @Override
    public String toString() {
        return "D";
    }

}
