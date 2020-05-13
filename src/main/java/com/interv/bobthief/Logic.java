package com.interv.bobthief;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
*
* @author PMMuthama
*/

@RestController
public class Logic 
{

	@GetMapping("/api/loot")
	public ResponseEntity<?> findMaximumValue(@RequestBody List<JsonNode> list_Loot) throws Exception
	{
		try 
		{
			
			List<List<JsonNode>> list_AllCombinations=new ArrayList<List<JsonNode>>();
			List<List<JsonNode>> list_CombinationsW0LessThan10=new ArrayList<List<JsonNode>>();
			List<JsonNode> maxValueLoot=new ArrayList<JsonNode>();
			ObjectMapper objectMapper = new ObjectMapper(); 
		
			//Generate all the possible combinations in the list 
		    for ( long i = 1; i < Math.pow(2, list_Loot.size()); i++ ) 
		    {
		        List<JsonNode> list = new ArrayList<JsonNode>();
		        for ( int j = 0; j < list_Loot.size(); j++ ) 
		        {
		            if ( (i & (long) Math.pow(2, j)) > 0 ) 
		            {
		            	
		                list.add(list_Loot.get(j));
		            }
		        }
		        list_AllCombinations.add(list);
		    }
		   
		    
		    for(int i=0;i<list_AllCombinations.size();i++)
		    {
		    	List<JsonNode> list_JsonNodes=list_AllCombinations.get(i);
		    	int totalWeight=0;
		    	int totalValue=0;
		    	for(JsonNode a_Loot:list_JsonNodes)
		    	{
		    		int weight=a_Loot.get("weight").asInt();
		    		int value=a_Loot.get("value").asInt();
		    		
		    		//calculate the total weights and values for each combination in list_AllCombinations
		    		totalWeight=totalWeight+weight;
		    		totalValue=totalValue+value;
		    	}
		    	ObjectNode jsonTotalValue = objectMapper.createObjectNode();
		    	ObjectNode jsonTotalWeight = objectMapper.createObjectNode();
		    	//factor in only the combinations whose weight is less or equal to 10
		    	if(totalWeight<=10)
		    	{
		    		//create a new jsonObject to show the total value for each list_JsonNodes
		    		jsonTotalValue.put("totalValue",totalValue );
		    		jsonTotalWeight.put("totalWeight", totalWeight);
		    		
		    		/*append a new jsonObject in each list of JsonNodes to indicate the totalValue and totalWeight for each list
		    		 * This is important for final output
		    		 * */
		    		 
		    		list_JsonNodes.add(jsonTotalWeight);
		    		list_JsonNodes.add(jsonTotalValue);
		    		
		    		
		    		//add the combination of totalValue less or equal to 10 to the master list of all combinations less or equal 10
		    		list_CombinationsW0LessThan10.add(list_JsonNodes);
		    	}
		    	
		    }

		    int finalTotalValue=0;
		    int finalTotalWeight=0;
		    for(List<JsonNode> list_JsonNodes:list_CombinationsW0LessThan10)
		    {
		    	//read the final total value for each combination with weight less or equal to 10
		    	 finalTotalValue=list_JsonNodes.get(list_JsonNodes.size()-1).get("totalValue").asInt();
		    	 finalTotalWeight=list_JsonNodes.get(list_JsonNodes.size()-2).get("totalWeight").asInt();
		    	 for(int i=0;i<list_CombinationsW0LessThan10.size();i++)
		    	 {
		    		 /*compare the new totalValue to the previous one, if totalValue is greater 
		    		 then assign that list to list maxValueLoot 
		    		 --iterate until the end of the list and get the combination with the greatest totalValue
		    		 
		    		 */
		    		 List<JsonNode> list_jsonNodeCompare=list_CombinationsW0LessThan10.get(i);
		    		 int finalTotalValueCompare=list_jsonNodeCompare.get(list_jsonNodeCompare.size()-1).get("totalValue").asInt();
		    		 int finalTotalWeightCompare=list_jsonNodeCompare.get(list_jsonNodeCompare.size()-2).get("totalWeight").asInt();
		    		 
		    		 if(finalTotalValueCompare>finalTotalValue)
		    		 {
		    			
		    			 maxValueLoot=list_jsonNodeCompare;
		    			
		    		 }
		    		/*
		    		 else if(finalTotalValueCompare==finalTotalValue)
		    		 {
		    			 if(finalTotalWeightCompare<finalTotalWeight)
		    			 {
		    				 maxValueLoot=list_jsonNodeCompare;
		    			 }
		    			 else 
		    			 {
		    				 maxValueLoot=list_JsonNodes;
						}
		    			 
		    		 }
		    		 */
		    		 
		    	 }
		    
		    }
		    
		    ObjectNode jsonFinalOutput = objectMapper.createObjectNode();
		    
		    //Constructing a beautiful output for a bountiful loot, I also don't condone this kind of looting but it is what it is.
		    jsonFinalOutput.put("totalValue", maxValueLoot.get(maxValueLoot.size()-1).get("totalValue"));
		    jsonFinalOutput.put("totalWeight", maxValueLoot.get(maxValueLoot.size()-2).get("totalWeight"));
		    maxValueLoot.remove(maxValueLoot.size()-1);
		    maxValueLoot.remove(maxValueLoot.size()-1);
		    jsonFinalOutput.putPOJO("bobsLoot",maxValueLoot);
		    
		    System.out.println(jsonFinalOutput);
			return new ResponseEntity<>(jsonFinalOutput,HttpStatus.OK);
			
		} catch (Exception e) 
		{
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
