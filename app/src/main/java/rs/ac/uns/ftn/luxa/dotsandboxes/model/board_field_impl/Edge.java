package rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.BoardField;

public class Edge extends BoardField {

    @Override
    public boolean isCompleted() {
        return status != Status.FREE;
    }

    public Edge(int i, int j) {
        super(i, j);
    }

}
