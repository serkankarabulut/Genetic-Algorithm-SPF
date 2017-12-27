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
    private ArrayList<Chromosome> results;

                                         /*CONSTRUCTORS*/

    public GeneticAlgorithm(int start, int end){
        this.myInput = new InputReader();
        this.solutionPath = new ArrayList<>();
        this.results = new ArrayList<>();
        this.startNode = start;
        this.goalNode = end;
        this.mutationRate = 0.2;
        initialize();
    }

                                            /*METHODS*/

    /*GENETIC ALGORITHMS*/
    private void initialize(){
        for(int i = 0; i < myInput.getNumberofNodes(); i++){
            for(int j = 0; j < myInput.getNumberofNodes(); j++){
                if(isValid(i,j)){
                    Gene myGene = new Gene(i,j, this.myInput.getWeight(i, j));
                    Chromosome myChromosome = new Chromosome();
                    myChromosome.addGene(myGene);
                    this.solutionPath.add(myChromosome);
                }
            }

        }
    }

    public void populate(int numberofPopulation){
        for(int i = 0; i < numberofPopulation; i++){
            Random rand = new Random();
            int randomChromosomeIndex1 = rand.nextInt(this.solutionPath.size());
            int randomChromosomeIndex2 = rand.nextInt(this.solutionPath.size());
            if(randomChromosomeIndex1 != randomChromosomeIndex2){
                if(validPopulation(randomChromosomeIndex1, randomChromosomeIndex2)){
                    Chromosome temp = new Chromosome();
                    for(int j = 0 ; j < this.solutionPath.get(randomChromosomeIndex1).getSize(); j++){
                        temp.addGene(this.solutionPath.get(randomChromosomeIndex1).getGene(j));
                    }
                    for(int j = 0; j < this.solutionPath.get(randomChromosomeIndex2).getSize(); j++){
                        temp.addGene(this.solutionPath.get(randomChromosomeIndex2).getGene(j));
                    }
                    this.solutionPath.add(temp);
                }
            }
        }
    }

    public void crossover(int numberofCrossover){
        for(int i = 0; i < numberofCrossover; i++){
            Random rand = new Random();
            int randomChromosomeIndex1 = rand.nextInt(this.solutionPath.size());
            int randomChromosomeIndex2 = rand.nextInt(this.solutionPath.size());
            int crossoverPoint = rand.nextInt(minSize(this.solutionPath.get(randomChromosomeIndex1), this.solutionPath.get(randomChromosomeIndex2)));
            if(isCrossoverValid(randomChromosomeIndex1, randomChromosomeIndex2, crossoverPoint)){
                if(validCrossover(randomChromosomeIndex1, randomChromosomeIndex2, crossoverPoint)){
                    Chromosome temp = new Chromosome();
                    for(int j = 0 ; j <= crossoverPoint; j++){
                        temp.addGene(this.solutionPath.get(randomChromosomeIndex1).getGene(j));
                    }
                    for(int j = crossoverPoint+1; j < this.solutionPath.get(randomChromosomeIndex2).getSize(); j++){
                        temp.addGene(this.solutionPath.get(randomChromosomeIndex2).getGene(j));
                    }

                    double mutationVal = Math.random();
                    if(mutationVal<=this.mutationRate)
                        mutate(temp);
                    else
                        this.solutionPath.add(temp);

                    this.solutionPath.add(temp);
                }
            }
        }
    }

    private void mutate(Chromosome c){
        Random rand = new Random();
        int size = c.getSize();
        System.out.println("size: " + size);
        if(size>2){
            int mutationPoint = rand.nextInt(size-2) + 1;
            int mutationNode = rand.nextInt((this.myInput.getMaxNode()+1));
            c.getGene(mutationPoint).setTo(mutationNode);
            c.getGene(mutationPoint+1).setFrom(mutationNode);
            if(validateMutation(c, mutationPoint)){
                c.getGene(mutationPoint).setWeight(this.myInput.getWeight(c.getGene(mutationPoint).getFrom(), c.getGene(mutationPoint).getTo()));
                c.getGene(mutationPoint+1).setWeight(this.myInput.getWeight(c.getGene(mutationPoint+1).getFrom(), c.getGene(mutationPoint+1).getTo()));
                this.solutionPath.add(c);
            }
        }
    }

    /*HELPER FUCNTIONS*/
    private int minSize(Chromosome c1, Chromosome c2){
        if(c1.getSize() > c2.getSize())
            return c2.getSize();
        return c1.getSize();
    }

    public void filterSolutionPath(){
        for (Chromosome mySolutionPath : this.solutionPath) {
            int to = (mySolutionPath.getSize()) - 1;
            if (mySolutionPath.getGene(0).getFrom() == this.startNode &&
                    mySolutionPath.getGene(to).getTo() == this.goalNode) {
                this.results.add(mySolutionPath);
            }
        }
        sortSolutionPath();
    }

    private void sortSolutionPath(){
        Comparator<Chromosome> comp;
        comp = (Chromosome c1, Chromosome c2) -> {
            int total1=0, total2=0;
            for(int i = 0 ; i < c1.getSize(); i++)
                total1+=c1.getGene(i).getWeight();
            for(int i = 0 ; i < c2.getSize(); i++)
                total2+=c2.getGene(i).getWeight();
            return total1-total2;
        };
        this.results.sort(comp);
    }

    public void printTopResult(){
        if(!this.results.isEmpty()){
            int totalWeight = 0;
            for(int j = 0; j < this.results.get(0).getSize(); j++){
                System.out.println("From: " + this.results.get(0).getGene(j).getFrom() +
                        "\tTo: " + this.results.get(0).getGene(j).getTo() +
                        "\tWeight: " + this.results.get(0).getGene(j).getWeight());
                totalWeight += this.results.get(0).getGene(j).getWeight();
            }
            System.out.println("Total Weight: " + totalWeight);
            System.out.println("------------------");
            //}
        }
    }


    /*FILTER FUNCTIONS*/
    private boolean isValid(int source, int dest){
        return this.myInput.getWeight(source, dest) != Integer.MAX_VALUE;
    }

    private boolean isCrossoverReverse(int index1, int index2, int crossoverPoint){
        for(int i = crossoverPoint+1; i < this.solutionPath.get(index2).getSize(); i++){
            if(isCrossoverReverseExist(this.solutionPath.get(index1), this.solutionPath.get(index2).getGene(i), crossoverPoint))
                return true;
        }
        return false;
    }

    private boolean isReverse(int index1, int index2){
        for(int i = 0; i < this.solutionPath.get(index2).getSize(); i++){
            if(isReverseExist(this.solutionPath.get(index1), this.solutionPath.get(index2).getGene(i)))
                return true;
        }
        return false;
    }

    private boolean isExist(int index1, int index2){
        for(int i = 0; i < this.solutionPath.get(index2).getSize(); i++){
            if(isGeneExist(this.solutionPath.get(index1), this.solutionPath.get(index2).getGene(i)))
                return true;
        }
        return false;
    }

    private boolean isCrossoverExist(int index1, int index2, int crossoverPoint){
        for(int i = 0; i < this.solutionPath.get(index2).getSize()- crossoverPoint; i++){
            if(isCrossoverGeneExist(this.solutionPath.get(index1), this.solutionPath.get(index2).getGene(crossoverPoint+i), crossoverPoint)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGeneExist(Chromosome c,Gene g){
        for(int i = 0; i < c.getSize(); i++){
            if(c.getGene(i).getFrom()== g.getFrom() && c.getGene(i).getTo() == g.getTo())
                return true;
        }
        return false;
    }

    private boolean isCrossoverGeneExist(Chromosome c,Gene g, int crossoverPoint){
        for(int i = 0; i <= crossoverPoint; i++){
            if(c.getGene(i).getFrom()== g.getFrom() && c.getGene(i).getTo() == g.getTo())
                return true;
        }
        return false;
    }

    private boolean isReverseExist(Chromosome c, Gene g){
        for(int i = 0; i < c.getSize(); i++){
            if(c.getGene(i).getFrom()== g.getTo() && c.getGene(i).getTo() == g.getFrom())
                return true;
        }
        return false;
    }

    private boolean isCrossoverReverseExist(Chromosome c, Gene g,int crossoverPoint){
        for(int i = 0; i <= crossoverPoint; i++){
            if(c.getGene(i).getFrom()== g.getTo() && c.getGene(i).getTo() == g.getFrom())
                return true;
        }
        return false;
    }

    private boolean validPopulation(int index1, int index2){
        return this.solutionPath.get(index1).getGene(this.solutionPath.get(index1).getSize()-1).getTo()==
                this.solutionPath.get(index2).getGene(0).getFrom() && !isReverse(index1, index2) && !isExist(index1,index2) &&
                this.solutionPath.get(index1).getGene(this.solutionPath.get(index1).getSize()-1).getTo() != this.goalNode;
    }

    private boolean validCrossover(int index1, int index2, int crossoverPoint){
        return this.solutionPath.get(index1).getGene(crossoverPoint).getTo()==
                this.solutionPath.get(index2).getGene(crossoverPoint+1).getFrom() &&
                !isCrossoverReverse(index1, index2, crossoverPoint) && !isCrossoverExist(index1,index2, crossoverPoint);
    }

    private boolean isCrossoverValid(int r1, int r2, int cp){
        return (r1!=r2 && cp != (this.solutionPath.get(r1).getSize()-1) && cp != (this.solutionPath.get(r2).getSize()-1));
    }

    private boolean validateMutation(Chromosome c, int mutationPoint){
        return this.myInput.isGeneExist(c.getGene(mutationPoint)) &&
                this.myInput.isGeneExist(c.getGene(mutationPoint+1));
    }
}
