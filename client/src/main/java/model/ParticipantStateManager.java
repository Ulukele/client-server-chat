package model;

import common.Model;
import common.Publisher;

public class ParticipantStateManager extends Publisher implements Model<ParticipantState> {
    private ParticipantState participantState;

    public ParticipantState getParticipantState() {
        return participantState;
    }

    public void setParticipantState(ParticipantState participantState) {
        this.participantState = participantState;
        publishNotify();
    }

    @Override
    public ParticipantState getData() {
        return getParticipantState();
    }
}
