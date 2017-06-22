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
		String returnString = returnString.replaceAll("n't", " not");
		returnString = returnString.replaceAll("\\d{2}\\/10", "/10");
		returnString = returnString.replace(":)", "smiley-face");
		returnString = returnString.replace(":(","frowny-face");
		returnString = returnString.replaceAll("('s?|[()!?]", "");
		returnString = returnString.replaceAll("\\s?\\.\\s?", " . ");
		return returnString.toLowerCase();
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
		return review.match("\\s(�|est[ae]|una?|el|los|�)\\s");
		/*if(review.indexOf(" de ") > 0)
			{
			System.out.println(" de "+review.indexOf(" de "));
			return true;
			}*/
		// return false;	
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
