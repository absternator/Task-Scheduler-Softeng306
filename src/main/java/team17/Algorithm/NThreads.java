package team17.Algorithm;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class NThreads extends Thread{
    AlgorithmAStar _aStar;
    List<ScheduledTask> _fullSchedule;

    public NThreads(AlgorithmAStar aStar) {
        _aStar = aStar;
    }

    public List<ScheduledTask> getFullSchedule() {
        return _fullSchedule;
    }

    @Override
    public void run() {
        while(true) {
            if(_aStar.getNextPartialSolution()==null) {
                break;
            } else {
                PartialSolution partialSolution = _aStar.getNextPartialSolution();
                if (partialSolution.isCompleteSchedule()) {
                    _fullSchedule = partialSolution.fullSchedule();
                    break;
                }
                Set<PartialSolution> children = partialSolution.expandSearch();
                _aStar.openAddChildren(children);
            }
        }
    }
}
