import java.util.ArrayList;

public class Chromosome {
    private ArrayList<Integer> paths;
    private int weight;

    public Chromosome(){
        this.paths = new ArrayList<>();
        this.weight = 0;
    }

    public void addGene(Gene g){
        if(this.paths.size()==0){
            this.paths.add(g.getFrom());
            this.paths.add(g.getTo());
            this.weight += g.getWeight();
        }
        else{
            this.paths.add(g.getTo());
            this.weight+=g.getWeight();
        }
    }

    public void addGenePoint(int g){
        this.paths.add(g);
    }

    public int getGene(int index){ return this.paths.get(index);}

    public void setGene(int index, int gene){ this.paths.get(index).equals(gene);}

    public int getLastGene(){
        return this.paths.get(this.getSize()-1);
    }

    public int getSize(){   return this.paths.size();}

    public int getWeight(){  return this.weight;}


    public void printChromosome(){
        System.out.print("Chromosome: ");
        System.out.print(this.paths.get(0));
        for (int i=1; i < this.paths.size(); i++){
                System.out.print(" - " + this.getGene(i));
        }
        System.out.print(" Weight:" + this.weight);
        System.out.println();
    }

    public boolean searchGene(int gene){
        for (int g : this.paths) {
            if(g==gene)
                return true;
        }
        return false;
    }

    public void setWeight(int weight){ this.weight=weight;}



}
