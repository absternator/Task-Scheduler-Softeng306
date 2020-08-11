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

        Set<Node> nodesInSchedule = new HashSet<>();
        for(ScheduledTask scheduledTask : this) {
            nodesInSchedule.add(scheduledTask.get_node());
        }

        AddNode: for (Node node : _graph.get_NodeList()) {
            // If node is already in schedule
           if(nodesInSchedule.contains(node)){
               continue;
           }
           //if dependency not in schedule
            for (Node dependency : node.get_dependendicies()) {
                if(!nodesInSchedule.contains(dependency)){
                    continue AddNode;
                }
            }

            //Node can be placed on Processor now
            for (int i = 1; i < _graph.get_numOfProcessors() + 1; i++) {
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
                            continue ;
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
        for (ScheduledTask task: this) {
            if(!task.get_node().get_id().equals("end")){
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
                return current.get_scheduledTask() != null;
            }

            @Override
            public ScheduledTask next() {
                ScheduledTask thisTask = current.get_scheduledTask();
                if(current.get_parent() != null) {
                    current = current.get_parent();
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
