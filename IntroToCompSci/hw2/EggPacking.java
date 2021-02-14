public class EggPacking
{
	public static void main(String[] args)
	{
		int eggs = Integer.parseInt(args[0]);
		int boxes = (int) eggs/12;
		int leftover = eggs%12;
		double price = boxes*(0.31);

		System.out.println(eggs + " eggs were packed into " + boxes + " cartons");
		System.out.println("There were " + leftover + " leftover eggs");
		System.out.println("The total amount spent on egg cartons: $" + price);
	}
}