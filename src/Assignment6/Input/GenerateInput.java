package Assignment6.Input;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

/**
 * Created by Super Arthur 8D on 26-6-14.
 */
public class GenerateInput {
    private final int NUMBER_OF_VARIABLES = 12000;
    private final int NUMBER_OF_ENTRIES = 50;
    private final int NUMBER_OF_CLUSTERS = 1;

    private Random random = new Random(8128);

    private final String fileName = "random.txt";

    public void makeFile(){
        PrintStream file = null;
        try {
            file = new PrintStream(fileName);


            file.println(NUMBER_OF_CLUSTERS);
            file.println(NUMBER_OF_ENTRIES);
            file.println(NUMBER_OF_VARIABLES);

            file.print("labels\t");
            for(int var=0;var<NUMBER_OF_VARIABLES;var++){
                if(var!=NUMBER_OF_VARIABLES-1){
                    file.printf("var%d\t",var);
                }
                else {
                    file.printf("var%d\n",var);
                }
            }

            for(int entry = 0;entry<NUMBER_OF_ENTRIES;entry++){
                file.printf("entry%d\t",entry);

                for(int var=0;var<NUMBER_OF_VARIABLES;var++){
                    if(var!=NUMBER_OF_VARIABLES-1){
                        file.printf("%.3f\t",generateDouble());
                    }
                    else {
                        file.printf("%.3f\n",generateDouble());
                    }
                }
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double generateDouble(){
        return 500*random.nextDouble()-250;
    }



    public static void main(String[] argv){
        new GenerateInput().makeFile();
    }

}
