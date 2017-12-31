import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {
                                        /*FIELDS*/

    private ArrayList<Chromosome> solutionPath;
    private InputReader myInput;
    private int startNode;
    private int goalNode;
    private double mutationRate;
    private int maxPathSize;
    private final int maxPathTry=5;

                                         /*CONSTRUCTORS*/

    public GeneticAlgorithm(int start, int end, int numberofNodes){
        this.myInput = new InputReader(numberofNodes);
        this.maxPathSize = numberofNodes;
        this.solutionPath = new ArrayList<>();
        this.startNode = start;
        this.goalNode = end;
        this.mutationRate = 0.2;
    }

                                            /*METHODS*/

    /*GENETIC ALGORITHM FUNCTIONS*/
    public void populate(int numberofPopulation){
        for(int i = 0; i < numberofPopulation; i++){
            createPath();
        }
        //printPaths();
    }
    public void crossover(int numberofCrossover){
        for(int i = 0; i < numberofCrossover; i++){
            Random rand = new Random();
            int randomChromosomeIndex1 = rand.nextInt(this.solutionPath.size());
            int randomChromosomeIndex2 = rand.nextInt(this.solutionPath.size());
            int crossoverPoint = rand.nextInt(minSize(this.solutionPath.get(randomChromosomeIndex1),
                    this.solutionPath.get(randomChromosomeIndex2))-2)+1;
            int crossoverPoint2 = findCrossoverPoint(this.solutionPath.get(randomChromosomeIndex2),
                    this.solutionPath.get(randomChromosomeIndex1).getGene(crossoverPoint));

            if (this.solutionPath.get(randomChromosomeIndex1).getSize() > 3 &&
                    this.solutionPath.get(randomChromosomeIndex2).getSize() > 3 && crossoverPoint2 != -1 &&
                    isCrossoverValid(randomChromosomeIndex1, randomChromosomeIndex2, crossoverPoint, crossoverPoint2)) {

                        Chromosome crossover1 = new Chromosome();
                        Chromosome crossover2 = new Chromosome();
                        for(int j = 0 ; j <= crossoverPoint; j++){
                            crossover1.addGenePoint(this.solutionPath.get(randomChromosomeIndex1).getGene(j));
                        }
                        for(int j = 0 ; j <= crossoverPoint2; j++){
                            crossover2.addGenePoint(this.solutionPath.get(randomChromosomeIndex2).getGene(j));
                        }
                        for(int j = crossoverPoint2+1; j < this.solutionPath.get(randomChromosomeIndex2).getSize(); j++){
                            crossover1.addGenePoint(this.solutionPath.get(randomChromosomeIndex2).getGene(j));
                        }
                        for(int j = crossoverPoint+1; j < this.solutionPath.get(randomChromosomeIndex1).getSize(); j++){//added
                            crossover2.addGenePoint(this.solutionPath.get(randomChromosomeIndex1).getGene(j));
                        }
                        double mutationVal = Math.random();
                        if(mutationVal<=this.mutationRate)
                            mutate(findFitness(crossover1,crossover2));
                        else
                            this.solutionPath.add(findFitness(crossover1, crossover2));
            }
        }
        sortSolutionPath();
    }
    private void mutate(Chromosome c){
        Random rand = new Random();
        int size = c.getSize();
        if(size>2){
            int mutationPoint = rand.nextInt(size-2) + 1;
            int mutationNode = rand.nextInt((this.myInput.getMaxNode()+1));
            c.setGene(mutationPoint, mutationNode);
            if(mutationNode!=startNode && mutationNode!=goalNode && validateMutation(c, mutationPoint)){
                updateFitness(c);
                this.solutionPath.add(c);
            }
        }
    }

    /*HELPER FUNCTIONS*/
    private void createPath(){
        Chromosome path = new Chromosome();
        Random rand = new Random();
        int index1;
        int index2 = rand.nextInt(this.maxPathSize);
        while(!this.myInput.isGeneExist(this.startNode,index2)) {
            index2 = rand.nextInt(this.maxPathSize);
        }
        int weight = this.myInput.getWeight(this.startNode,index2);
        Gene startGene = new Gene(this.startNode, index2, weight);
        path.addGene(startGene);
        int counter=0;
        while((path.getSize() <= this.maxPathSize) && ( path.getLastGene() != this.goalNode) && counter<this.maxPathTry){
            index1 = path.getLastGene();
            index2 = rand.nextInt(this.maxPathSize);
            if(this.myInput.isGeneExist(index1, index2)){
                weight = this.myInput.getWeight(index1,index2);
                Gene myGene = new Gene(index1, index2, weight);
                path.addGene(myGene);
            }
            counter++;
        }
        if(path.getLastGene()==this.goalNode)
            this.solutionPath.add(path);
    }
    private int minSize(Chromosome c1, Chromosome c2){
        if(c1.getSize() > c2.getSize())
            return c2.getSize();
        return c1.getSize();
    }
    private Chromosome findFitness(Chromosome c1, Chromosome c2){
        updateFitness(c1);
        updateFitness(c2);

        if(c1.getWeight()>c2.getWeight()){
            return c2;
        }
        return c1;
    }
    private void updateFitness(Chromosome c){
        int weight=0;
        for (int i=0; i<c.getSize()-1; i++) {
            weight += this.myInput.getWeight(c.getGene(i),c.getGene(i+1));
        }
        c.setWeight(weight);
    }
    public void printPaths(){
        for (Chromosome aSolutionPath : this.solutionPath) {
            aSolutionPath.printChromosome();
        }
    }
    private void sortSolutionPath(){
        Comparator<Chromosome> comp;
        comp = Comparator.comparingInt(Chromosome::getWeight);
        this.solutionPath.sort(comp);
    }
    int findCrossoverPoint(Chromosome c, int g){
        for(int i = 1; i<c.getSize()-1; i++){
            if(c.getGene(i) == g)
                return i;
        }
        return -1;
    }
    public void printTopResult(){
        if(!this.solutionPath.isEmpty()){
            this.solutionPath.get(0).printChromosome();
            System.out.println("------------------");
        }
    }

   /*FILTER FUNCTIONS*/
    private boolean isCrossoverValid(int r1, int r2, int cp, int cp2) {
        return (r1!=r2 && !isCrossoverExist(r1, r2, cp, cp2));
    }
    private boolean isCrossoverExist(int index1, int index2, int crossoverPoint, int crossoverPoint2){
        boolean f1=false, f2=false;
        for(int i = crossoverPoint+1; i < this.solutionPath.get(index1).getSize()-1; i++){
            if(isGeneExist(this.solutionPath.get(index2), this.solutionPath.get(index1).getGene(i), crossoverPoint2))
                f1=true;
        }
        for(int i = crossoverPoint2+1; i < this.solutionPath.get(index2).getSize()-1; i++){
            if(isGeneExist(this.solutionPath.get(index1), this.solutionPath.get(index2).getGene(i), crossoverPoint))
                f2=true;
        }
        return (f1&&f2);

    }
    private boolean isGeneExist(Chromosome c, int g,int crossoverPoint){
        for(int i = 1; i < crossoverPoint; i++){
            if(c.getGene(i) == g)
                return true;
        }
        return false;
    }
    private boolean isMutationGeneExist(Chromosome c, int mutationPoint){
        for(int i=1;i<c.getSize()-1;i++){
            if(i!=mutationPoint){
                if(c.getGene(i)==c.getGene(mutationPoint))
                    return true;
            }
        }
        return false;
    }
    private boolean validateMutation(Chromosome c, int mutationPoint){
        return this.myInput.isGeneExist(c.getGene(mutationPoint),c.getGene(mutationPoint+1)) &&
                this.myInput.isGeneExist(c.getGene(mutationPoint-1),c.getGene(mutationPoint)) &&
                !isMutationGeneExist(c,mutationPoint);
    }
}
