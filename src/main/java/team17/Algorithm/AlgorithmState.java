package team17.Algorithm;

import java.util.Observable;

public class AlgorithmState extends Observable {
    private PartialSolution _completeSolution;

    public AlgorithmState() {
    }

    public void setCompleteSolution(PartialSolution completeSolution) {
        this._completeSolution = completeSolution;
        setChanged();
        notifyObservers();
    }

    public PartialSolution getCompleteSolution() {
        return _completeSolution;
    }
}
