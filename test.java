public class test
{
	
	public static void main(String [] args)
	{
		long startTime = System.currentTimeMillis();
		System.out.println("running");
		for(int i = 0; i < 0xffffffff; i++)
		{
			if (i % 0xffff == 0)
			{
				System.out.println("Running");
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
