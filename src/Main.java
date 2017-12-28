public class Main {

    public static void main(String[] args) {
            for(int i = 0; i<100; i++) {
                System.out.println(i+1 + "th turn");
                long startTime = System.currentTimeMillis();
                GeneticAlgorithm myPool = new GeneticAlgorithm(0, 9);
                myPool.populate(4000);
                myPool.filterSolutionPath();
                myPool.crossover(3000);
                myPool.printTopResult();
                long endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime) + " milliseconds");
                System.out.println("----------------End of turn----------------------\n");
            }
    }
}
