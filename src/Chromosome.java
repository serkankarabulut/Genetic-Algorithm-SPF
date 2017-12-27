import java.util.ArrayList;

public class Chromosome {
    private ArrayList<Gene> paths;

    public Chromosome(){    this.paths = new ArrayList<>();}

    public void addGene(Gene g){    this.paths.add(g);}

    public Gene getGene(int index){ return paths.get(index);}

    public int getSize(){   return this.paths.size();}


}
