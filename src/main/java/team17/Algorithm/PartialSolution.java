package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PartialSolution  implements Iterable<ScheduledTask> {
    private PartialSolution _parent;
    private Graph _graph;
    private ScheduledTask _scheduledTask;

    public PartialSolution(PartialSolution parent, Graph graph, ScheduledTask scheduledTask) {
        _parent = _parent;
        _graph = graph;
        _scheduledTask = scheduledTask;


    }

    /**
     * This method gets this instances parent
     * @return  Parent partial Solution
     */
    public PartialSolution get_parent() {
        return _parent;
    }

    public Graph get_graph() {
        return _graph;
    }

    public ScheduledTask get_scheduledTask() {
        return _scheduledTask;
    }

    /**
     * This method returns the underestimate cost to finish schedule from a partial schedule
     * @return The underestimate cost to finish the schedule
     */
    public int getCostUnderestimate(){
        int cosUnderestimate = 0;
        for (ScheduledTask scheduledTask: this) {
            cosUnderestimate = Math.max(cosUnderestimate, scheduledTask.get_startTime() + scheduledTask.get_node().get_bottomLevel());

        }
        return cosUnderestimate;
    }

    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    public Set<PartialSolution> expandSearch(){
        Set<PartialSolution> children = new HashSet<>();

        AddNode: for (Node node : _graph.get_NodeList()) {
            // This is if node is already in schedule or all dependencies have not been added
            for (ScheduledTask task : this) {
                if (task.get_node().equals(node) || !node.get_dependendicies().contains(task.get_node())) {
                    continue AddNode;
                }
            }
            //Node can be placed on Processor now
            for (int i = 0; i < _graph.get_numOfProcessors(); i++) {
                int eligibleStartime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask: this) {
                    if(scheduledTask.get_processorNum() == i){
                        eligibleStartime = scheduledTask.getFinishTime();
                        break;
                    }
                }
                //Start time based on dependencies on  OtherProcessors
                for (ScheduledTask scheduledTask : this) {
                    if(scheduledTask.get_processorNum() != i){
                        boolean dependantFound = false;
                        int communicationTime = 0;
                        for (Node edge: node.get_incomingEdges().keySet()) {
                            if(edge.equals(scheduledTask.get_node())){
                                dependantFound = true;
                                communicationTime = node.get_incomingEdges().get(edge);
                            }
                        }
                        if (!dependantFound){
                            break ;
                        }
                        eligibleStartime = Math.max(eligibleStartime,scheduledTask.getFinishTime() + communicationTime);
                    }
                }
                children.add(new PartialSolution(this,_graph,new ScheduledTask(i,node, eligibleStartime)));
            }
        }
        return children;
    }

    /**
     * This method checks if a complete schedule is return
     * @return Returns true if full schedule, else false
     */
    public boolean isCompleteSchedule(){
        if(this.get_scheduledTask() != null) {
            return this.get_scheduledTask().get_node().get_id().equals("end");
        }
        else return false;
    }

    /**
     * This constructs the full schedule
     * @return Returns the full schedule
     */
    public List<ScheduledTask> fullSchedule(){
        List<ScheduledTask> scheduledTaskList = new LinkedList<>();
        this.forEach(scheduledTaskList::add);
        return scheduledTaskList;
    }

    /**
     * This iterator iterates from the current partial solution to the root partial solution to get all tasks
     * @return This returns new Iterator instance.
     */
    @Override
    public Iterator<ScheduledTask> iterator() {
        Iterator<ScheduledTask> it = new Iterator<>() {
            PartialSolution current = PartialSolution.this;

            @Override
            public boolean hasNext() {
                return current.get_scheduledTask() != null;
            }

            @Override
            public ScheduledTask next() {
                ScheduledTask thisTask = current.get_scheduledTask();
                current = current.get_parent();
                return thisTask;
            }
        };
        return it;
    }

}
