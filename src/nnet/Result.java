package nnet;

import nnet.math.*;

public class Result
{
	public VMat output=null;
	public double loss;

	@Override
	public String toString()
	{
		return "loss =" + loss + "\n   " + output;
	}
}
