package ovh.iwinski.PositionAwareRunLengthEncoding;

/* similar to RLE
   except it also includes the start position of the value
*/
public class PositionAwareRunLengthEncoding16Bits {
	//return array = [name1, length1, name2, length2, ...]
	private static char[] groupBy(char[] values){
		String result = "";
		char last = 0;
		char counter=0;
		for(char i=0;i<values.length;i++){
			if(i==0) {
				last=values[0];
				counter++;
			}
			else{
				if(values[i]!=last){
					result+=last;
					result+=counter;
					counter=1;
					last=values[i];
				}
				else{
					counter++;
				}
			}
			
		}
		result+=last;
		result+=counter;
		return result.toCharArray();
	}
	
	private static char[] encodeWithOffset(char values[], char offset){
		String result="";
		char position=0;
		char[] groups=groupBy(values);
		for(char i=0;i<groups.length;i+=2){
			result+=groups[i+1]; //length
			result+=(char)(position+offset); //position
			result+=groups[i]; //name
			position+=groups[i+1];
		}
			
		return result.toCharArray();
	}
	
	// max size of input array is 65536
	//return array = [length1, position1, name1, length2, position2, name2, ...]
	/**
	 * Encode sequence
	 * @param  values to encode.<br>
	 *         Max length of <strong>values</strong> can be 65536.
	 * @return Return encoded sequence as char[].<br>
	 * 	 	   Return char[] length is 3*values.length.<br>
	 * 		   Sequence is [length1, position1, value1, length2, position2, value2, ...]
	 */
	public static char[] encode(char[] values){
		return encodeWithOffset(values,(char)0);
	}
	
	/**
	 * Decode sequence
	 * @param  parleValues must be divisible by 3.<br>
	 *         Max length of <strong>parleValues</strong> can be 196608.
	 * @return Return char[] with decoded values.<br>
	 * 		   Return char[] length is parleValues.length/3.<br>
	 * 		   Sequence is [value1, value2, ...]
	 */
	public static char[] decode(char[] parleValues) {
		String result="";
		for(char i=0;i<parleValues.length;i+=3){
			for(char j=0;j<parleValues[i];j++){
				result+=parleValues[i+2];
			}
		}
		return result.toCharArray();
	}

	//Binary search probably can speed up searching for value.
	//this method implement naive algorithm for search
	// return char as int. Return -1 when not found index
	/**
	 * Get value at index in sequence
	 * @param  parleValues must be divisible by 3.<br>
	 *         Max length of <strong>parleValues</strong> can be 196608.
	 * @param  index get value at index
	 * @return Return char as int<br>
	 * 		   If found index then return char with value<br>
	 * 		   else return -1<br>
	 */
	public static int getValueAt(char[] parleValues, char index) {	
		for(char i=0;i<parleValues.length;i+=3){
			if(parleValues[i]-1+parleValues[i+1]>=index){
				return parleValues[i+2];
			}
		}
		return -1;
	}
	
	//Here probably we can faster change value if we use binary search instead for
	/**
	 * Set value at index in sequence (faster algorithm)
	 * @param  parleValues must be divisible by 3.<br>
	 *         Max length of <strong>parleValues</strong> can be 196608.
	 * @param  index set value at index
	 * @param  value to set
	 * @return Return new encoded char[] with changed value at index.<br>
	 * 		   If something go wrong char[] is null
	 */
	public static char[] setValueAt(char[] parleValues, char index, char value){
		char[] newValue;
		char[] result = null;
		//instead of below for there could be a binary search to optimize searching
		for(char i=0;i<parleValues.length;i+=3){
			if(parleValues[i]-1+parleValues[i+1]>=index){
				char[] d=decode(new char[]{parleValues[i],parleValues[i+1],parleValues[i+2]});
				
				if(d.length==1){
					//left and right group
					if((i-1>0 && parleValues[i-1]==value) && (i+5<parleValues.length && parleValues[i+5]==value)){
						result=new char[parleValues.length-6];
						System.arraycopy(parleValues, 0, result, 0, i);
						System.arraycopy(parleValues, i+6, result, i, parleValues.length-i-6);
						result[i-3]+=parleValues[i+3]+1;
					}
					else{
						// left group
						if(i-1>0 && parleValues[i-1]==value){
							result=new char[parleValues.length-3];
							System.arraycopy(parleValues, 0, result, 0, i);
							System.arraycopy(parleValues, i+3, result, i, parleValues.length-i-3);
							result[i-3]+=1;
						}
						else{
							//right group
							if(i+5<parleValues.length && parleValues[i+5]==value){
								result=new char[parleValues.length-3];
								System.arraycopy(parleValues, 0, result, 0, i);
								System.arraycopy(parleValues, i+3, result, i, parleValues.length-i-3);
								result[i]+=1;
								result[i+1]-=1;
							}
							else{
								//no left and right group
								result=new char[parleValues.length];
								System.arraycopy(parleValues, 0, result, 0, parleValues.length);
								result[i+2]=value;
							}
						}

					}
				}
				else{
					char position=(char) (index-parleValues[i+1]);
					if(position==0){
						//need check left group if the same!!!
						if(i-1>0 && parleValues[i-1]==value){
							result=new char[parleValues.length];
							System.arraycopy(parleValues, 0, result, 0, parleValues.length);
							result[i]-=1;
							result[i+1]+=1;
							result[i-3]+=1;
						}
						else{
							newValue=encodeWithOffset(new char[]{value},index);
							result=new char[parleValues.length+3];
							System.arraycopy(parleValues, 0, result, 0, i);
							System.arraycopy(newValue, 0, result, i, newValue.length);
							System.arraycopy(parleValues, i, result, i+3, parleValues.length-i );
							result[i+3]-=1;				
							result[i+4]+=1;					
						}
					}
					else{
						if(d.length==position+1){
							//need check right group if the same!!!
							if(i+5<parleValues.length && parleValues[i+5]==value){
								result=new char[parleValues.length];
								System.arraycopy(parleValues, 0, result, 0, parleValues.length);
								result[i]-=1;
								result[i+3]+=1;
								result[i+4]-=1;
							}
							else{
								newValue=encodeWithOffset(new char[]{value},index);
								result=new char[parleValues.length+3];
								System.arraycopy(parleValues, 0, result, 0, i+3);
								System.arraycopy(newValue, 0, result, i+3, newValue.length);
								System.arraycopy(parleValues, i+3, result, i+6, parleValues.length-i-3);
								result[i]-=1;
							}
						}
						else{
							d[position]=value;						
							newValue=encodeWithOffset(d,parleValues[i+1]);
							result=new char[parleValues.length+6];
							System.arraycopy(parleValues, 0, result, 0, i);
							System.arraycopy(newValue, 0, result, i, newValue.length);
							System.arraycopy(parleValues, i+3, result, i+9,parleValues.length-i-3);
							
						}
					}
					
				}

				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Set value at index in sequence (naive algorithm)
	 * @param  parleValues must be divisible by 3.<br>
	 * 		   Max length of <strong>parleValues</strong> can be 196608.
	 * @param  index set value at index
	 * @param  value to set
	 * @return Return new encoded char[] with changed value at index.<br>
	 */
	public static char[] setValueAtNaiveAlgorithm(char[] parleValues, char index, char value){
		char[] tmp=decode(parleValues);
		tmp[index]=value;
		return encode(tmp);
	}
}