public class Main {

    public static void main(String[] args) {
                long startTime = System.currentTimeMillis();
                GeneticAlgorithm myPool = new GeneticAlgorithm(0, 9, 10);
                myPool.populate(100);
                myPool.crossover(100);
                myPool.printTopResult();
                long endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime) + " milliseconds");
                System.out.println("----------------End of turn----------------------\n");
    }
}
