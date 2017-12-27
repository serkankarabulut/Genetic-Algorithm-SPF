public class Gene {
    private int from;
    private int to;
    private int weight;

    public Gene(int source, int dest, int weight){
        this.from = source;
        this.to = dest;
        this.weight = weight;
    }

    public int getFrom(){return this.from;}
    public int getTo(){return this.to;}
    public int getWeight(){return this.weight;}
    public void setTo(int toVal){ this.to = toVal;}
    public void setFrom(int fromVal){ this.from = fromVal;}
    public void setWeight(int weightVal){ this.weight = weightVal;}

}
