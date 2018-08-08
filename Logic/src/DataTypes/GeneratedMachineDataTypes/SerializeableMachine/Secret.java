package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;
import java.util.List;

public class Secret implements Serializable {
    private final List<Integer> selectedRotorsInOrder;
    private final List<Integer> selectedRotorsPositions;
    private final int selectedReflector;

    public Secret(List<Integer> selectedRotorsInOrder, List<Integer> selectedRotorsPositions, int selectedReflector) {
        this.selectedRotorsInOrder = selectedRotorsInOrder;
        this.selectedRotorsPositions = selectedRotorsPositions;
        this.selectedReflector = selectedReflector;
    }

    public List<Integer> getSelectedRotorsInOrder() {
        return this.selectedRotorsInOrder;
    }

    public List<Integer> getSelectedRotorsPositions() {
        return this.selectedRotorsPositions;
    }

    public int getSelectedReflector() {
        return this.selectedReflector;
    }

    public Secret setInitialPosition(List<Integer> newPosition) {
        return new Secret(this.selectedRotorsInOrder, newPosition, this.selectedReflector);
    }
}
