import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputReader {
    private int adjMatrix[][];
    private int numberofNodes;
    private int maxNode;

    public InputReader(int numberofNodes){
        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));

            String line;
            this.numberofNodes=numberofNodes;
            this.maxNode = numberofNodes-1;
            initializeMatrix();

            while((line=in.readLine())!= null){
                String temp[] = line.split(" ");
                this.adjMatrix[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = Integer.parseInt(temp[2]);
            }
        } catch ( IOException ex) {
            Logger.getLogger(InputReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getMaxNode(){ return this.maxNode;}
    public int getNumberofNodes(){ return this.numberofNodes;}
    public int getWeight(int start, int end){ return this.adjMatrix[start][end];}

    private void initializeMatrix(){
        this.adjMatrix = new int[this.numberofNodes][this.numberofNodes];
        for(int i = 0; i < this.numberofNodes; i++){
            for(int j = 0 ; j < this.numberofNodes; j++){
                this.adjMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
    }
    public boolean isGeneExist(int from, int to){
        return this.adjMatrix[from][to] != Integer.MAX_VALUE;
    }

    public void printMatrix(){
        for(int i = 0; i < this.numberofNodes; i++){
            for(int j = 0; j < this.numberofNodes; j++){
                System.out.print(this.adjMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
