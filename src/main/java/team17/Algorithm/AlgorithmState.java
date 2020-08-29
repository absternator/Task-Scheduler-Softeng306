package team17.Algorithm;

/**
 * This class stores the state of the running algorithm
 * this state is used for visualisation
 */
public class AlgorithmState {
    private PartialSolution _completeSolution; // current best complete schedule
    private boolean _isFinished = false;
    private int _numExpandedPartialSolutions = 0; // size of closed
    private int _numUnexpandedPartialSolutions = 0; // size of open + closed
    private int _numCompleteSolutionsFound = 0; // number of complete solutions checked
    private int _numPruned = 0; // number of pruned of solutions

    public AlgorithmState() {}

    public void setCompleteSolution(PartialSolution completeSolution) {
        this._completeSolution = completeSolution;
    }

    public PartialSolution getCompleteSolution() {
        return _completeSolution;
    }

    public void setFinished(boolean isFinished) {
        this._isFinished = isFinished;
    }

    public boolean getFinished() {
        return _isFinished;
    }

    public int getNumExpandedPartialSolutions() {
        return _numExpandedPartialSolutions;
    }

    public void updateNumExpandedPartialSolutions(int numExpandedPartialSolutions) {
        _numExpandedPartialSolutions += numExpandedPartialSolutions;
    }

    public int getNumUnexpandedPartialSolutions() {
        return _numUnexpandedPartialSolutions - _numExpandedPartialSolutions;
    }

    public void updateNumUnexpandedPartialSolutions(int numUnexpandedPartialSolutions) {
        _numUnexpandedPartialSolutions += numUnexpandedPartialSolutions;
    }

    public int getNumCompleteSolutions() {
        return _numCompleteSolutionsFound;
    }

    public void updateNumCompleteSolutions(int numCompleteSolutions) {
        _numCompleteSolutionsFound += numCompleteSolutions;
    }

    public int getNumPruned() {
        return _numPruned;
    }

    public void updateNumPruned(int pruned) {
        _numPruned += pruned;
    }

}
