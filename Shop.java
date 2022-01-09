import java.util.*;
import java.io.*;

public class Shop{

	private static double totalShopSale=0;
	private static String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
	private static Map<Integer,Double> monthSales = new HashMap<Integer,Double>();
	private static ArrayList<HashMap<String,Integer>> totalQtyEachProd = new ArrayList<HashMap<String,Integer>> ();
	private static ArrayList<HashMap<String,Double>> totalSaleOfProd = new ArrayList<HashMap<String,Double>> ();
	private static ArrayList<HashMap<String,ArrayList<Integer>>> prodExtAvgData= new ArrayList<HashMap<String,ArrayList<Integer>>>();
	private static ArrayList<ArrayList<String>>popularProdsMonth=new ArrayList<ArrayList<String>>();

	private static void initializing(){
		for(int i=0;i<12;i++) //Initializing the MultiDimension DataStructure
		{
			totalQtyEachProd.add(new HashMap<String,Integer>());
			totalSaleOfProd.add(new HashMap<String,Double>());
			prodExtAvgData.add(new HashMap<String,ArrayList<Integer>>());
			popularProdsMonth.add(new ArrayList<String>());
		}

	}
	private static void readFile(){
		try{
			FileInputStream file = new FileInputStream("Assignment.txt");
			Scanner sc= new Scanner(file,"UTF-8");

			sc.nextLine(); //omitting the header line in the file.

			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				String splitLine[] = line.split(","); //splitting on delimitter ","

			   //Lead Question 1 : To calculate total sales in the shop 
				totalShopSale+=calculatetotalShopSale(splitLine);

				
				Integer mon= findMonth(splitLine);
				Double total =findSales(splitLine);
				Integer qty =findQuantity(splitLine);
				String name =findIceCream(splitLine);

				//Lead Question 2: To create a map between Month and its total sale
				Double checkSale =monthSales.get(mon);
				monthSales.put(mon, checkSale==null ? total : checkSale+total);

				//Lead Question 3: To create a 2-D Arraylist of month and map{ product : total quanity sold in that month}
			   Integer checkQty =totalQtyEachProd.get(mon-1).get(name);
				totalQtyEachProd.get(mon-1).put(name, checkQty==null ? qty : checkQty+qty);

				//Lead Question 4: To create a 2-D Arraylist of month and map{ product : total sale  in that month}
				Double checkSaleRevenue =totalSaleOfProd.get(mon-1).get(name);
				totalSaleOfProd.get(mon-1).put(name, checkSaleRevenue==null ? total : checkSaleRevenue+total);

				//Lead Question 5: To create a 3-D Arraylist of month and map{product : [min,max,total,count]}
				HashMap<String,ArrayList<Integer>> monthData = prodExtAvgData.get(mon-1);
				if(monthData==null)
				{
					ArrayList<Integer> minMaxTotalSale = new ArrayList<Integer>();
					minMaxTotalSale.add(qty);  
					minMaxTotalSale.add(qty);
					minMaxTotalSale.add(qty);
					minMaxTotalSale.add(1);
					prodExtAvgData.get(mon-1).put(name,minMaxTotalSale); // [min, max, total, count]

				}
				else
				{
					ArrayList<Integer> minMaxTotalSale = prodExtAvgData.get(mon-1).get(name);
					if(minMaxTotalSale==null)
					{
					minMaxTotalSale = new ArrayList<Integer>();
					minMaxTotalSale.add(qty);
					minMaxTotalSale.add(qty);
					minMaxTotalSale.add(qty);
					minMaxTotalSale.add(1);
					prodExtAvgData.get(mon-1).put(name,minMaxTotalSale); // [min, max, avg, count]
					}
					else
					{
						int min =prodExtAvgData.get(mon-1).get(name).get(0);
						if(min > qty)
						prodExtAvgData.get(mon-1).get(name).set(0,qty);

						int max =prodExtAvgData.get(mon-1).get(name).get(1);
						if(max < qty)
						prodExtAvgData.get(mon-1).get(name).set(1,qty);

						int existing =prodExtAvgData.get(mon-1).get(name).get(2);
						int count =prodExtAvgData.get(mon-1).get(name).get(3);
						
						Integer totalQty=existing+qty;
						prodExtAvgData.get(mon-1).get(name).set(2,totalQty);
						prodExtAvgData.get(mon-1).get(name).set(3,count+1);


					}

				}

			}
		}	
		catch(Exception e)
		{	
			System.out.println(e);
		}
	}
	private static double calculatetotalShopSale(String data[])
	{
		return Double.parseDouble(data[4]);
	}

	private static int findMonth(String data[])
	{
		return Integer.parseInt(data[0].substring(5,7));
	}
	private static Double findSales(String data[])
	{
		return Double.parseDouble(data[4]);
	}

	private static String findIceCream(String data[])
	{
		return (data[1]);
	}

	private static Integer findQuantity(String data[])
	{
		return Integer.parseInt(data[3]);
	}

	private static void totalSales()
	{
		// Printing Answer 1 
			System.out.println("Total sales in the shop is :- Rs. "+ totalShopSale);
	}

	private static void monthlySales()
	{
		// Printing Answer 2
		for(Map.Entry<Integer,Double> entry: monthSales.entrySet() )
		{
			System.out.println("Total sales in "+ months[entry.getKey()-1] + " --- Rs. "+ entry.getValue());
		}

	}

	private static void popularProductOfMonth()
	{
		//Printing Answer 3
		for(int i=0;i<totalQtyEachProd.size();i++)
		{
			int max=1;
			ArrayList<String> popular = new ArrayList<String>();
			for(Map.Entry<String, Integer> entry : totalQtyEachProd.get(i).entrySet())
			{
			   if(entry.getValue()>=max) //Getting the name of product with maximum quanity in the month. 
			   {
					max=entry.getValue();
			   }
			}
			for(Map.Entry<String, Integer> entry : totalQtyEachProd.get(i).entrySet())
			{
				if(entry.getValue()==max)
				{	
					popular.add(entry.getKey());
					popularProdsMonth.get(i).add(entry.getKey());
				}	

		  	 }

			if(popular.size()!=0)
			System.out.println("Most Popular item in "+ months[i] + " is " +  popular + " "+ totalQtyEachProd.get(i).get(popular.get(0)) );
		}

	}
	private static void itemGeneratingMostRevenue() {
		//Printing Answer 4
		for(int i=0;i<totalSaleOfProd.size();i++)
		{
			double max=1;;
			ArrayList<String> revenue = new ArrayList<String>();
			for(Map.Entry<String, Double> entry : totalSaleOfProd.get(i).entrySet())
			{
			   if(entry.getValue()>=max) //Getting the name of product with maximum total sale in the month. 
			   {
				max=entry.getValue();
			   }
			}
			for(Map.Entry<String, Double> entry : totalSaleOfProd.get(i).entrySet())
			{
				if(entry.getValue()==max)
					revenue.add(entry.getKey());
			}

			if(revenue.size()!=0)
			System.out.println("Item which generated maximum revenue in "+ months[i] + " is " +  revenue + "  Rs. "+ totalSaleOfProd.get(i).get(revenue.get(0)) );

		}

	}
	private static void minMaxAverage(){
		//Printing Answer 5
			
		for(int i=0;i<12;i++)
		{  
			String popularProduct="";
			if(popularProdsMonth.get(i).size()!=0) 
		   	popularProduct=popularProdsMonth.get(i).get(0); //only the first popular product.
			if(popularProduct.length()!=0)
			{
				ArrayList<Integer> result=prodExtAvgData.get(i).get(popularProduct);
				Integer average = result.get(2)/result.get(3); //calculating average
				result.set(2,average); 
				result.remove(3); //removing count from result
				System.out.println("Min, Max, Avg orders of "+ popularProduct + "  in month of " + months[i] + " is" + result);
			}
		}
	}


	public static void main(String args[]){
			
		initializing();
		readFile(); 
		totalSales();
		monthlySales();
		popularProductOfMonth();
		itemGeneratingMostRevenue();
		minMaxAverage();

	}
}

