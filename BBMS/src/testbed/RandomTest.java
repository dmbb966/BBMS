package testbed;

public class RandomTest {
	
	public static void main(String[] args) {		
		
		String test = "WP 1 :1";
		String[] split = test.split(" :");
		
		for (int i = 0; i < split.length; i++) {
			System.out.println(i + ">" + split[i] + "<");
		}
        

	}

}
