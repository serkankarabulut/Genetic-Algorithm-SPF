import java.util.ArrayList;

public class Chromosome {
    private ArrayList<Gene> paths;

    public Chromosome(){    this.paths = new ArrayList<>();}

    public void addGene(Gene g){    this.paths.add(g);}

    public Gene getGene(int index){ return paths.get(index);}

    public int getSize(){   return this.paths.size();}

    public int getFitness(){
        int fitness=0;
        for(int i=0; i<this.getSize(); i++){
            fitness+=this.getGene(i).getWeight();
        }
        return fitness;
    }

}
