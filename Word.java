public class Word
{
	private String name; //actual word
	private int usage; //number of times used
	private int score; //raw sum of all scores
	private int lowest; //number of times used
	private int highest; //raw sum of all scores
	private static int SIGNIFICANCE_MEAN = 31;
	private static int SIGNIFICANCE_STD = 3;
	
	private double powerSum1;
	private double powerSum2;
	private double stdev;
	
	Word(String name) //constructor
		{
		this.name = name;
		this.usage = 0;
		this.score = 0;
		this.lowest = 50;
		this.highest = 0;
			
		this.powerSum1 = 0;
		this.powerSum2 = 0;
		this.stdev = 0;
		}
		
	Word(String name, int score) //constructor
		{
		this.name = name;
		this.usage = 1;
		this.score = score;
		this.lowest = 50;
		this.highest = 0;
			
		this.powerSum1 = score;
		this.powerSum2 = Math.pow(score, 2);
		this.stdev = Math.sqrt(usage*powerSum2 - Math.pow(powerSum1, 2))/usage;
		}
		
	public void addScore(int score) //adds a score value to the word in the event another occurance of the word is found
		{
		this.usage++;
		this.score += score;
		if(score > highest)
			{
			highest = score;
			}
		if(score < lowest)
			{
			lowest = score;
			}
			
		this.powerSum1 += score;
		this.powerSum2 += Math.pow(score, 2);
		this.stdev = Math.sqrt(usage*powerSum2 - Math.pow(powerSum1, 2))/usage;
		}
	
	public String getName() //returns the total score of this word
		{
		return this.name;
		}
		
	public int getScore() //returns the total score of this word
		{
		return this.score;
		}	
	
	public int getUsage() //returns times this word was used
		{
		return this.usage;
		}	
		
	public float getAverage()	//returns average score of the score of the word divided by the number of times the word was used
		{
		if (usage > 0)
			{
			return this.score / this.usage;
			}
		return 0;
		}
		
	public double getSTD() //returns the total score of this word
		{
		return this.stdev;
		}
	
	public boolean isSignifcant(int usage) //determines whether the word is significant or not. Removes noise words like "the" that have no positive or negative meaning
		{
		if (this.usage > usage)
			{
			if(Math.abs(SIGNIFICANCE_MEAN - this.getAverage()) > SIGNIFICANCE_STD)
				{
				if(this.stdev < 20)
					{
					return true;
					}
				}
			}
		return false;
		}
}