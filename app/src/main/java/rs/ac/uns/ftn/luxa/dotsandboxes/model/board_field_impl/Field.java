package rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.BoardField;

public class Field extends BoardField {

    @Override
    public boolean isCompleted() {
        for (BoardField field : adjacent) {
            if (!field.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setStatus(Status status) {
        if (this.status == Status.FREE) {
            super.setStatus(status);
        }
    }

    public Field(int i, int j) {
        super(i, j);
    }

}
