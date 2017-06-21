import java.util.*;


public class Analyzer
{
	private HashMap<String,Word> nameToWordPointer; //mapping Word names to pointers
	private static int SIGNIFICANCE_COUNT = 0;
	
	Analyzer() //constructor
		{
		this.nameToWordPointer = new HashMap<String,Word>();
		}
	
	private String cleanReview(String review)	//filters the initial review string
		{
		String returnString = review.replace("n't"," not");	//remove n't
		returnString = returnString.replace("'s","");	//remove 's
		returnString = returnString.replace("'",""); //remove apostrophes
		returnString = returnString.replace(".0/10","/10");
		returnString = returnString.replace(".5/10","/10");
		returnString = returnString.replace(":)","smiley-face");
		returnString = returnString.replace(":(","frowny-face");
		returnString = returnString.replace(",","");
		returnString = returnString.replace(")"," ");
		returnString = returnString.replace("("," ");
		returnString = returnString.replace("!",".");
		returnString = returnString.replace("?",".");
		returnString = returnString.replace(" .",".");
		returnString = returnString.replace(". ",".");	
		returnString = returnString.replace("."," . ");
		returnString = returnString.toLowerCase();//
		return returnString;
		}
	
	public void addReviewData(String review, int reviewScore)
		{
		String[] sentences = cleanReview(review).split(" . ");
		for (int i = 0; i < sentences.length; i++)
			{
			if(sentences[i].length() > 0)
				{
				String[] words = sentences[i].split(" ");
				for (int k = 0; k < words.length; k++)
					{
					addWord(words[k],reviewScore);
					}
				}
			}
		}
	
	//public String goodOrBad(	
		
	public static double sigmoid(float x) 
		{
		return (1/( 1 + Math.pow(Math.E,(-1*x))));	
		}
		
	public boolean filterSpanish(String review)	//filters out common spanish substrings to scrub data
		{
		if(review.indexOf("è") > 0)
			{
			//System.out.println(review.indexOf("è"));
			return true;
			}
		if(review.indexOf(" esta ") > 0)
			{
			//System.out.println("esta"+review.indexOf("esta"));
			return true;
			}
		if(review.indexOf(" este ") > 0)
			{
			//System.out.println("este"+review.indexOf("este"));
			return true;
			}
		if(review.indexOf(" un ") > 0)
			{
			//System.out.println(" un "+review.indexOf(" un "));
			return true;
			}
		if(review.indexOf(" una ") > 0)
			{
			//System.out.println(" una "+review.indexOf(" una "));
			return true;
			}
		if(review.indexOf(" el ") > 0)
			{
			//System.out.println(" el "+review.indexOf(" el "));
			return true;
			}
		if(review.indexOf(" los ") > 0)
			{
			//System.out.println(" los "+review.indexOf(" los "));
			return true;
			}
		if(review.indexOf("ñ") > 0)
			{
			//System.out.println("ñ"+review.indexOf("ñ"));
			return true;
			}
		/*if(review.indexOf(" de ") > 0)
			{
			System.out.println(" de "+review.indexOf(" de "));
			return true;
			}*/
		return false;	
		}
		
	public float predictReviewScoreModified(String review)
		{
		int significantCount = 0; //number of significant words in review
		float significantTotal = 0; //total of all significant words
			
		int sigWordCount = 0; //number of significant words in sentence
		float sentenceTotal = 0; //total of all significant words in sentence
			
		float predictedScore = -10000; //final returned value. Will be average of sigtotal / sigcount. -10000 means there were no significant words
		
		/*if(filterSpanish(review))
			{
			//System.out.println("Spanish??: "+review);
			return predictedScore;
			}*/
			
		String[] sentences = cleanReview(review).split(" . ");
		for (int i = 0; i < sentences.length; i++)
			{
			if(sentences[i].length() > 0)
				{
				sigWordCount = 0; //number of significant words in review
				sentenceTotal = 0; //total of all significant words	
				String[] words = sentences[i].split(" ");
				for (int k = 0; k < words.length; k++)
					{
					Word word = nameToWordPointer.get(words[k]);
					if (word != null)
						{
						if(word.isSignifcant(SIGNIFICANCE_COUNT))
							{
							sigWordCount++;		//was a significant word
							sentenceTotal += (sigmoid(word.getAverage() - 33) * 42)+5; //get average of that word
							//sentenceTotal += word.getAverage();		//get average of that word
							}
						}
					}
				if(sigWordCount > 0)	//check if the sentence had any significance at all
					{
					significantCount++;	//was a significant sentence
					significantTotal += (sentenceTotal / sigWordCount);	//add average of sentence to the main total
					}
				}
			
			}
		
		if(significantCount > 0)
			{
			predictedScore = significantTotal / significantCount;
			}
		return predictedScore;
		}	
		
		
		
	public float predictReviewScore(String review)	//version 1 of algorithm. Gets all significant words and averages their values in the review. Returns negative value if no words exist
		{
		int significantCount = 0; //number of significant words in review
		float significantTotal = 0; //total of all significant words
		float predictedScore = -10000; //final returned value. Will be average of sigtotal / sigcount. -10000 means there were no significant words
		
		/*if(filterSpanish(review))
			{
			//System.out.println("Spanish??: "+review);
			return predictedScore;
			}*/
			
		String[] sentences = cleanReview(review).split(" . ");
		for (int i = 0; i < sentences.length; i++)
			{
			if(sentences[i].length() > 0)
				{
				String[] words = sentences[i].split(" ");
				for (int k = 0; k < words.length; k++)
					{
					Word word = nameToWordPointer.get(words[k]);
					if (word != null)
						{
						if(word.isSignifcant(SIGNIFICANCE_COUNT))
							{
							significantCount++;
							significantTotal += word.getAverage();
								
							}
						}
					}
				}
			} 
		
		if(significantCount > 0)
			{
			predictedScore = significantTotal / significantCount;
			}
		return predictedScore;
		}	
		
	public void addWord(String name,int reviewScore) //adds a Word, updates score if it exists
		{
		if (!nameToWordPointer.containsKey(name)) //check if Word isn't already added to list of Words
			{
			Word newWord = new Word(name, reviewScore);
			nameToWordPointer.put(name,newWord); //adds the Word to list
			}
			else
			{
			Word word = this.nameToWordPointer.get(name);
			word.addScore(reviewScore);
			}
		}
		
	public void listSignificantWords(int threshhold) //Lists all significant words
		{
		String[] wordsList = nameToWordPointer.keySet().toArray(new String[0]); //returns an array of all names in nameToWordPointer	
		for (int i = 0; i < wordsList.length; i++)
			{
			Word word = nameToWordPointer.get(wordsList[i]);
			if(word.isSignifcant(threshhold))
				{
				System.out.println(wordsList[i] + "	avg : " +word.getAverage() + "	usage : " + word.getUsage()+ "	std : " + word.getSTD());
				}
			}
		}
	
}