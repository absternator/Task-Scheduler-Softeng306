import org.junit.Test;
import team17.Algorithm.KnapsackScheduler;

public class testKnapsack {
    @Test
    public void testCalculateKnapsack(){
        int[] wt = new int[] { 5, 10, 10, 30 };
        System.out.println(KnapsackScheduler.knapSack(26, wt, wt.length));
    }
}
