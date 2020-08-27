package team17.Algorithm;

public class AlgorithmState{
    private PartialSolution _completeSolution;
    private boolean _isFinished;

    public AlgorithmState() {
        _isFinished=false;
    }

    public void setCompleteSolution(PartialSolution completeSolution) {
        this._completeSolution = completeSolution;
    }

    public PartialSolution getCompleteSolution() {
        return _completeSolution;
    }

    public void setFinished(boolean isFinished) {
        this._isFinished = isFinished;
    }
    public boolean getFinished(){
        return _isFinished;
    }
}
