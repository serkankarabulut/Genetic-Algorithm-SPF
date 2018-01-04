public class Main {

    public static void main(String[] args) {
                long startTime = System.currentTimeMillis();
                GeneticAlgorithm myPool = new GeneticAlgorithm(0, 3999, 4000);
                myPool.populate(50);
                myPool.crossover(100);
                myPool.printTopResult();
                long endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime) + " milliseconds");
                System.out.println("----------------End of turn----------------------\n");
    }
}
