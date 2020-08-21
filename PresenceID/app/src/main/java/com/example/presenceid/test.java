import java.io.*;
import java.util.*;
public class test{
	public static void main(String[] args)
	{

		
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter("../../../../assets/attendees.txt",true));
                    out.write("\n"+"myname");
                    out.close();
                } 
                catch (IOException e) {
                }
            
    }
}