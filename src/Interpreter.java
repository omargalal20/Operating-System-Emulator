import java.awt.image.Kernel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

public class Interpreter {
	static BufferedReader br;
	static Vector<Integer> numOfInstructionsForProcesses = new Vector<Integer>();
	static int indexOfLineOfCode = 49;
	static int indexOfVariable = 74;

	public static void parser() {
		Vector<String> toRemoveDuplicates = new Vector<String>();

		for (int i = 1; i <= 3; i++) {
			try {
				br = new BufferedReader(new FileReader("src/Program " + i
						+ ".txt"));
				String current = "";
				
				//Add PCB in memory
				KernelSystemCalls.Memory[0 + (100 * (i-1))] = "ID = " + i;
				KernelSystemCalls.Memory[1 + (100 * (i-1))] = "State = Not Running";
				KernelSystemCalls.Memory[2 + (100 * (i-1))] = "PC = " + 0;
				KernelSystemCalls.Memory[3 + (100 * (i-1))] = "LowerBoundary = " + (100 * (i-1));
				KernelSystemCalls.Memory[4 + (100 * (i-1))] = "UpperBoundary = " + ((100 * (i)) - 1);
				//
				
				//Add rest of variables and data
				int lineCounter = 1;
				int variableCounter  = 1;
				
				int numOfInst = 0;
				int counter = 0;
				int counter1 = 0;
				
				while ((current = br.readLine()) != null) {
					numOfInst++;
					String[] line = current.split(" ");
					String instruction  = "";
					for (int j = 0; j < line.length; j++) {
						instruction += line[j] + " ";
						if(line[j].length() == 1){
							if(!(toRemoveDuplicates.contains(line[j]))){
								KernelSystemCalls.Memory[(indexOfVariable+(counter1++))+(100 * (i-1))] = line[j];
								toRemoveDuplicates.add(line[j]);
							}
						}
					}
					KernelSystemCalls.Memory[(indexOfLineOfCode+(counter++))+(100 * (i-1))] = "Instruction" + lineCounter++ + " = " + instruction ;
				}
				//
				numOfInstructionsForProcesses.add(numOfInst);
				toRemoveDuplicates.clear();
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*System.out.println("Inside Of Memory");
		for(int j = 0 ; j < KernelSystemCalls.Memory.length ; j++){
			System.out.println(KernelSystemCalls.Memory[j]);
		}*/
	}

	public static void Scheduler(){
		Queue<Integer> scheduler = new LinkedList<Integer>();
		//scheduler.add(1);
		scheduler.add(2);
		//scheduler.add(3);
		while(!scheduler.isEmpty()){
			
			int Quanta = 0;
			
			int processId = (scheduler.peek()); 
			
			System.out.println("Process " + processId + " has been chosen");
			System.out.println("-----------------------------------------");
			
			KernelSystemCalls.Memory[1 + (100 * (processId-1))] = "State = Running";
			
			String [] pcline = ((String) KernelSystemCalls.Memory[2 + (100 * (processId-1))]).split(" ");
			int pc = Integer.parseInt(pcline[2]);
			
			String [] lowerBoundline = ((String) KernelSystemCalls.Memory[2 + (100 * (processId-1))]).split(" ");
			int lowerBound = Integer.parseInt(lowerBoundline[2]);
			
			String [] upperBoundline = ((String) KernelSystemCalls.Memory[2 + (100 * (processId-1))]).split(" ");
			int upperBound = Integer.parseInt(upperBoundline[2]);
			
			int numberOfInstructions = numOfInstructionsForProcesses.get(processId-1);
			
			
			
			for(int i = 0; i < 2; i++){
				if(pc < numberOfInstructions){
					String inst = (String) KernelSystemCalls.Memory[indexOfLineOfCode + (100 * (processId-1)) + pc];
					executeInstruction(inst.substring(15), processId);
					pc++;
					Quanta++;
				}
				else{
					continue;
				}
			}
			
			if(pc == numberOfInstructions){
				System.out.println("instruction" + scheduler.poll());
			}
			else{
				int x = scheduler.poll();
				scheduler.add(x);
			}
			
			KernelSystemCalls.Memory[0 + (100 * (processId-1))] = "ID = " + processId;
			KernelSystemCalls.Memory[1 + (100 * (processId-1))] = "State = Not Running";
			KernelSystemCalls.Memory[2 + (100 * (processId-1))] = "PC = " + pc;
			KernelSystemCalls.Memory[3 + (100 * (processId-1))] = "LowerBoundary = " + lowerBound;
			KernelSystemCalls.Memory[4 + (100 * (processId-1))] = "UpperBoundary = " + upperBound;
			
			
			System.out.println("Process " + processId + " was executed");
			System.out.println("Quanta is " + Quanta);
			
			/*System.out.println("Inside Of Memory");
			for(int j = 0 ; j < KernelSystemCalls.Memory.length ; j++){
				System.out.println(KernelSystemCalls.Memory[j]);
			}*/
			
			System.out.println("-----------------------------------------");
		}
	}
	
	public static int getIndexOfCorrectVar(int processId, String v){
		int counter = 0;
		while(KernelSystemCalls.Memory[(indexOfVariable + counter)+(100 * (processId-1))]!=null){
			String[] s = ((String) KernelSystemCalls.Memory[(indexOfVariable + counter)+(100 * (processId-1))]).split(" ");
			if(s[0].equals(v)){
				return (indexOfVariable + counter)+(100 * (processId-1));
			}
			counter++;
		}
		return 0;
	}
	
	public static void executeInstruction(String inst, int processId){
		String[] line = inst.split(" ");
		for (int j = 0; j < line.length; j++) {
			if (line[j].equals("print")) {
				if (line[1].equals("Enter_file_name")){
					KernelSystemCalls.printData(line[1]+ " without .txt");
				}
				else if (line[1].equals("Enter_file_data")){
					KernelSystemCalls.printData(line[1]);
				}
				else if (line[1].equals("Enter_first_number"))
					KernelSystemCalls.printData(line[1]);

				else if (line[1].equals("Enter_second_number"))
					KernelSystemCalls.printData(line[1]);

				else{
					int index = getIndexOfCorrectVar(processId, line[1]);
					System.out.println("Output : "+ "\n"+ KernelSystemCalls.readFromMemory(index));
				}
			} else if (line[j].equals("assign")) {
				assign(line, processId);
			} else if (line[j].equals("add")) {
				try {
					add(line, processId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (line[j].equals("writeFile")) {
				int index = getIndexOfCorrectVar(processId, line[1]);
				int index1 = getIndexOfCorrectVar(processId, line[2]);
				KernelSystemCalls.writeToFile( KernelSystemCalls.readFromMemory(index), KernelSystemCalls.readFromMemory(index1));
			}
		}
	}
	
	// 7
	public static void assign(String[] line, int processId) {
		if (line[2].equals("readFile")) {
			int index = getIndexOfCorrectVar(processId, line[3]);
			String filename = (String) KernelSystemCalls.readFromMemory(index);
			String fileData = KernelSystemCalls.readFromFile(filename);
			int index1 = getIndexOfCorrectVar(processId, line[1]);
			KernelSystemCalls.writeToMemory(index1, line[1], fileData);
			
		} else if (line[2].equals("input")) {
			String input = KernelSystemCalls.takeInput();
			int index = getIndexOfCorrectVar(processId, line[1]);
			KernelSystemCalls.writeToMemory(index, line[1], (input));
		}

	}

	// 8
	public static void add(String[] line, int processId) throws IOException {
		int index = getIndexOfCorrectVar(processId, line[1]);
		int index1 = getIndexOfCorrectVar(processId, line[2]);
		int c = Integer.parseInt((String) KernelSystemCalls.readFromMemory(index)) + Integer.parseInt((String) KernelSystemCalls.readFromMemory(index1));
		
		KernelSystemCalls.writeToMemory(index,"a", c+"");
		
		KernelSystemCalls.printData((String) KernelSystemCalls.readFromMemory(index));
	}

	
	////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws IOException {
		parser();
		Scheduler();
	}
}
