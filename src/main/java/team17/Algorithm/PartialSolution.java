package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.*;

public class PartialSolution  implements Iterable<ScheduledTask>,Comparable<PartialSolution> {
    private final PartialSolution _parent;
    private final Graph _graph;
    private final ScheduledTask _scheduledTask;

    public PartialSolution(PartialSolution parent, Graph graph, ScheduledTask scheduledTask) {
        _parent = parent;
        _graph = graph;
        _scheduledTask = scheduledTask;

    }

    /**
     * This method gets this instances parent
     * @return  Parent partial Solution
     */
    public PartialSolution getParent() {
        return _parent;
    }

    public Graph getGraph() {
        return _graph;
    }

    public ScheduledTask getScheduledTask() {
        return _scheduledTask;
    }

    /**
     * This method returns the underestimate cost to finish schedule from a partial schedule
     * @return The underestimate cost to finish the schedule
     */
    public int getCostUnderestimate(){
        int costUnderestimate = 0;
        for (ScheduledTask scheduledTask: this) {
            costUnderestimate = Math.max(costUnderestimate, scheduledTask.getStartTime() + scheduledTask.getNode().getBottomLevel());
        }
        return costUnderestimate;
    }

    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    public Set<PartialSolution> expandSearch(){
        Set<PartialSolution> children = new HashSet<>();

        Set<Node> nodesInSchedule = new HashSet<>();
        for(ScheduledTask scheduledTask : this) {
            nodesInSchedule.add(scheduledTask.getNode());
        }

        AddNode: for (Node node : _graph.getNodeList()) {
            // If node is already in schedule
           if(nodesInSchedule.contains(node)){
               continue;
           }
           //if dependency not in schedule
            for (Node dependency : node.getDependencies()) {
                if(!nodesInSchedule.contains(dependency)){
                    continue AddNode;
                }
            }

            //Node can be placed on Processor now
            for (int i = 1; i < _graph.getNumOfProcessors() + 1; i++) {
                int eligibleStartTime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask: this) {
                    if(scheduledTask.getProcessorNum() == i){
                        eligibleStartTime = scheduledTask.getFinishTime();
                        break;
                    }
                }
                //Start time based on dependencies on  OtherProcessors
                for (ScheduledTask scheduledTask : this) {
                    if(scheduledTask.getProcessorNum() != i){
                        boolean dependantFound = false;
                        int communicationTime = 0;
                        for (Node edge: node.getIncomingEdges().keySet()) {
                            if(edge.equals(scheduledTask.getNode())){
                                dependantFound = true;
                                communicationTime = node.getIncomingEdges().get(edge);
                            }
                        }
                        if (!dependantFound){
                            continue ;
                        }
                        eligibleStartTime = Math.max(eligibleStartTime,scheduledTask.getFinishTime() + communicationTime);
                    }
                }
                children.add(new PartialSolution(this,_graph,new ScheduledTask(i,node, eligibleStartTime)));
            }
        }
        return children;
    }

    /**
     * This method checks if a complete schedule is return
     * @return Returns true if full schedule, else false
     */
    public boolean isCompleteSchedule(){
        if(this.getScheduledTask() != null) {
            return this.getScheduledTask().getNode().getId().equals("end");
        }
        else return false;
    }

    /**
     * This constructs the full schedule
     * @return Returns the full schedule
     */
    public List<ScheduledTask> fullSchedule(){
        List<ScheduledTask> scheduledTaskList = new LinkedList<>();
        for (ScheduledTask task: this) {
            if(!task.getNode().getId().equals("end")){
                scheduledTaskList.add(task);
            }
        }
        return scheduledTaskList;
    }
    /**
     * This iterator iterates from the current partial solution to the root partial solution to get all tasks
     * @return This returns new Iterator instance.
     */
    @Override
    public Iterator<ScheduledTask> iterator() {
        return new Iterator<>() {
            private PartialSolution current = PartialSolution.this;

            @Override
            public boolean hasNext() {
                return current.getScheduledTask() != null;
            }

            @Override
            public ScheduledTask next() {
                ScheduledTask thisTask = current.getScheduledTask();
                if(current.getParent() != null) {
                    current = current.getParent();
                }
                return thisTask;
            }
        };
    }

    @Override
    public int compareTo(PartialSolution other) {
        return this.getCostUnderestimate() - other.getCostUnderestimate();
    }
// TODO: 12/08/20 come back and do equals method & hashcode !!!! how?

//    @Override
//    public boolean equals(Object other) {
//        for (ScheduledTask scheduledTask : this) {
//            if(!scheduledTask.equals(other)){
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(_parent, _graph, _scheduledTask);
//    }
}
