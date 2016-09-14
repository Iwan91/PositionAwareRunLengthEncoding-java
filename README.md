# Position Aware RunLength Encoding for java
Usage:
```java
//you can use char as values from 0 to 65535
char[] toEncode="AABBWWSD".toCharArray();

//Max length of input char[] can be 65535.
//Return encoded sequence as char[].
//Return char[] length is 3*values.length.
//Sequence is [length1, position1, value1, length2, position2, value2, ...]
char[] encoded=PositionAwareRunLengthEncoding16Bits.encode(toEncode);


//This is faster algorithm to get value at index
//Input char[] must be divisible by 3.
//Max length of input char[] can be 196605.
//Return char as int.
//If found index then return char with value
//else return -1
char index=2;
int valueInt=PositionAwareRunLengthEncoding16Bits.getValueAt(encoded, index);
char valueChar;
if(valueInt==-1)
  System.out.println("Not found value at index="+(int)index);
else
  valueChar=(char)valueInt;

//This is naive algorithm to get value at index
//Input char[] must be divisible by 3.
//Max length of input char[] can be 196605.
//Return char as int.
//If found index then return char with value
//else return -1
int valueInt2=PositionAwareRunLengthEncoding16Bits.getValueAtNaiveAlgorithm(encoded, index);
char valueChar2;
if(valueInt2==-1)
  System.out.println("Not found value at index="+(int)index);
else
  valueChar2=(char)valueInt2;

//This is faster algorithm to change value at index
//Input char[] must be divisible by 3.
//Max length of input char[] can be 196605.
//Return new encoded char[] with changed newValue at index.
//If something go wrong char[] is null
char newValue='U'
char[] encoded2=PositionAwareRunLengthEncoding16Bits.setValueAt(encoded, index, newValue);
if(encoded2==null)
	System.out.println("Something go wrong with re-encoding at index="+(int)index+" with value="+(char)newValue);

//This is naive algorithm to change value at index
//Input char[] must be divisible by 3.
//Max length of input char[] can be 196605.
//Return new encoded char[] with changed newValue at index.
//If something go wrong throw exception java.lang.ArrayIndexOutOfBoundsException
char[] encoded3=PositionAwareRunLengthEncoding16Bits.setValueAtNaiveAlgorithm(encoded, index, newValue);

//Input char[] must be divisible by 3.
//Max length of input char[] can be 196605.
//Return char[] with decoded values.
//Return char[] length is parleValues.length/3.
//Sequence is [value1, value2, ...]
char[] decoded=PositionAwareRunLengthEncoding16Bits.decode(encoded2);
```

###More info:
https://twistedpairdevelopment.wordpress.com/2011/06/10/run-length-encoding-with-positioning-information-in-python/
http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.10.4411&rep=rep1&type=pdf
