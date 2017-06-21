//import java.io.PrintWriter;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.*;
import java.lang.Object;
public class Driver
{
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
	{ 
	
	//Data file variables
		
	String datafilename = "rottenMoviesReviews.txt";
		
	Analyzer A = new Analyzer();
	
	//Read the CSV	
	ArrayList<String> Lines = FileReader.getFileLines(datafilename); //get all lines of data from all reviews
	
	//Teach the Analyzer using the data from the file
	float scoreTotal = 0; //value indicating the total score of all reviews 
	for (int i = 1; i < Lines.size(); i++) //iterate through all reviews
		{
		//System.out.println(Lines.get(i));
		String[] reviewSections = Lines.get(i).split("\",\"");  //split up the CSV to get the data
		if(reviewSections.length > 2)
			{
			//System.out.println("test");
			String reviewText = reviewSections[1];	//get the review text
			int reviewScore = Integer.parseInt(reviewSections[2]);	//return the review score as an int
			A.addReviewData(reviewText,reviewScore);		//tell the analyzer to run through the review data and ascociate words with scores
				
			scoreTotal += reviewScore;
			}
		}
	//System.out.println("Average score of all reviews: " + (scoreTotal / Lines.size()));	
	
		
	
		
		
	//Assess the accuracy of score predictor algorithm	
	float scoreDivergence = 0;		//difference between actual review score and predicted review score
	int reviewNumber = 10000;	//number of reviews to predict
	int failedReviews = 0;	//number of reviews that couldn't be predicted with algorithm
	int successfulReviews = 0;
	int tolerance = 2;
	int booleanSuccess = 0;
		
	for (int i = 1; i < Lines.size(); i++)
		{
		//int currentreview = (int) (Math.random() * 10000);	//pick a random review
		int currentreview = i;
		String[] reviewSections = Lines.get(currentreview).split("\",\"");	//get the text
		float reviewScore = Float.parseFloat(reviewSections[2]);	//get the actual score
		
		//float predictedScore = A.predictReviewScore(reviewSections[1]);	//predict the score
		float predictedScore = A.predictReviewScoreModified(reviewSections[1]);	//predict the score
		//float predictedScore = (float)Math.random() * 50;	//a random prediction. Used to see how accurate a random prediction is
		
		//System.out.println("Actual: "+reviewScore + "	Predicted:	"+predictedScore);
		if(predictedScore > 0)	//if the prediction didn't fail
			{
			scoreDivergence += Math.abs(reviewScore - predictedScore);	//get the score divergence between actual and predicted
			}
		else
			{
			failedReviews++;	//count number of failed reviews
			//System.out.println(reviewScore + ":" + reviewSections[1]);
			} 
		
		if (Math.abs(reviewScore - predictedScore) < tolerance)
			{
			successfulReviews++;
			}
			
		if (Math.abs(reviewScore - predictedScore) > 8)
			{
			//System.out.println(reviewScore+"	" +predictedScore+"	"+reviewSections[1]);
			}
		

		if (((reviewScore > 31) && (predictedScore > 31)) || ((reviewScore < 33) && (predictedScore < 33)))
			{
			booleanSuccess++;
			}
		else
			{
			if(predictedScore > 0)	//if the prediction didn't fail
				{
				System.out.println(reviewScore + ":" +predictedScore+"	:	" + reviewSections[1]);	
				}
			}
		}
	
	System.out.println("tolerance: "+tolerance + "	rreviewcount	"+(Lines.size()-1));
	System.out.println("Score Divergence: " + (scoreDivergence / (reviewNumber - failedReviews))+"	Successful Reviews: "+successfulReviews +"	Failed Reviews: "+failedReviews); 
	System.out.println("boolean successes: "+booleanSuccess);
	
		
	System.out.println(A.predictReviewScoreModified("This is a movie that derives most of its suspense on whether a piece of paper will be signed, not a strong basis for dramatic tension."));
	

	//System.out.println(A.predictReviewScoreModified("If you can look past the sitcom humor, that late-in-the-game melodrama and the usual cliche about the mature child in a family of immature adults, 3 Generations does a few important things."));
	//System.out.println(A.predictReviewScoreModified("This rather clever, breakneck-paced cartoon gives fans exactly what they want: Like the new nemesis voiced by Trey Parker, it shoots mulitple machine-gun bursts of bubblegum at the audience, asking them to chew and enjoy."));
	//System.out.println(A.predictReviewScoreModified("Season 3 wisely incorporated a few story elements that allowed the show's final run to not feel quite as much of a trudge as some parts of Season 2."));
	//System.out.println(A.predictReviewScoreModified("In the end, the family at the center of this crime drama commits the worst sin of all, at least when it comes to television: Their repetitive, irredeemable behavior makes you wonder why you cared about them in the first place"));
		
	//A.listSignificantWords(10);	
		
	}
}
