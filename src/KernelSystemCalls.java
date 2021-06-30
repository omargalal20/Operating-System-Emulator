import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class KernelSystemCalls {
	static Object [] Memory = new Object[300];
	
	//1
	public static String readFromFile(String filePath) {
		File fileToBeModified = new File("src//" + filePath + ".txt");
		String Content = "";

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));

			// Reading all the lines of input text file into Content

			String line = reader.readLine();

			while (line != null) {
				Content += line + "\n";
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("File Not Created");
		}

		return Content;
	}
	
    //2
	public static void writeToFile(Object file, Object data){
		    try {
		    	File myObj = new File("src//" + file + ".txt");
		    	if (myObj.createNewFile()){
		    		System.out.println("File created: " + myObj.getName());
		    		FileWriter myWriter = new FileWriter("src//" + myObj.getName(), true);
					BufferedWriter bw = new BufferedWriter(myWriter);
					if(data.getClass().getSimpleName().equals("Integer")){
						bw.write((int) data);
					}
					else{
						bw.write((String) data);
					}
					bw.newLine();
					bw.close();
		    	}
		    	else{
					System.out.println("File already exists and can add new data");
					FileWriter myWriter = new FileWriter("src//" + myObj.getName(), true);
					if(data.getClass().getSimpleName().equals("Integer")){
						myWriter.write("\n" + (int) data);
					}
					else{
						myWriter.write("\n" + (String) data);
					}
					myWriter.close();
		    	}
			} catch (Exception i1) {
				System.out.println("An error occurred.");
			}
	}
	
	//3
	public static void printData(Object data){
		System.out.println(data);
	}
	
	//4
	@SuppressWarnings("resource")
	public static String takeInput() {
		Scanner sc=new Scanner(System.in);
		return sc.nextLine();
	}
	
	//5
	public static Object readFromMemory(int index){
		String[] line = ((String) Memory[index]).split(" ");
		String data = line[2];
		return data;
	}	
	
	//6
	public static void writeToMemory(int index, String var, String input){
		System.out.println(var + " = " + input);
		System.out.println("Index is " + index);
		Memory[index] = var + " = " + input;
	}
}
