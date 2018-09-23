package hellozin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OutputComparator {
	public static void main(String[] args) throws IOException {
		File file = new File("C://Users//paul5//Downloads//myOutput.txt");
	    File file2 = new File("C://Users//paul5//Downloads//output.txt");

	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    BufferedReader reader2 = new BufferedReader(new FileReader(file2));

	    String data = null;
	    String data2 = null;
	    boolean hasAnyDifferLine = false;
	    
	    while((data = reader.readLine()) != null) { // 읽을게 없으면 null 리턴
	        data2 = reader2.readLine();
	        if (data2 == null)
	            break;
	        
	        if (!data.equals(data2)) {
	        	hasAnyDifferLine = true;
	            System.out.println("정답: "+data);
	            System.out.println("\t오답: "+data2);
	        }
	    }
	    if (hasAnyDifferLine) 
	        System.out.println("오답입니다.");
	    else 
	    	System.out.println("정답입니다.");
	    
	    reader.close();
	    reader2.close();
	}
}
