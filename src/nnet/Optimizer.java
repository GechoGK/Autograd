package nnet;

import java.util.*;
import nnet.math.*;

public class Optimizer
{
	private Result result;
	protected double lr=0.01; // learning rate;
	protected LossFunction loss;

	public void train(Module m, VMat dat, VMat tar)
	{

	}
	protected void setResult(VMat output, double loss)
	{
		if (result == null)
			return;
		result.output = output;
		result.loss = loss;
	}
	public void setResultCallback(Result rs)
	{
		this.result = rs;
	}
	public void setLossFunction(LossFunction loss)
	{
		this.loss = loss;
	}
	public Result getResult()
	{
		return result;
	}
}
